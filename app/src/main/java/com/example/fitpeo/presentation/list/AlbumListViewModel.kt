package com.example.fitpeo.presentation.list

import android.content.Context
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fitpeo.R
import com.example.fitpeo.data.repository.local.JsonDataSource
import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.domain.model.NetworkResult
import com.example.fitpeo.domain.usecase.UseCase
import com.example.fitpeo.presentation.core.ViewState
import com.example.fitpeo.presentation.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(private val useCase: UseCase, private val context: Context) :
    BaseViewModel() {

    private val _uiStateFlow = MutableStateFlow<ViewState>(ViewState.Loading)
    val uiStateFlow = _uiStateFlow.asLiveData()

    fun fetchList() {
        performCoroutineTask{
            _uiStateFlow.value = ViewState.Loading
            try {
                useCase.getListUseCase.getAlbumList().collect { pagingData ->
                    _uiStateFlow.value = ViewState.Success(pagingData)
                }
            } catch (e: Exception) {
                _uiStateFlow.value = ViewState.Failure(context.getString(R.string.failed_to_retrieve))
            }
        }
    }
    fun setSearchQuery(query: String?) {
        viewModelScope.launch {
            trySendQuery(query)
        }
    }

    private fun trySendQuery(query: String?) {
        searchQueryChannel.trySend(query ?: "")
    }

    private val searchQueryChannel = ConflatedBroadcastChannel<String>()

    val searchLiveData: LiveData<ViewState> = liveData {
        emit(ViewState.Loading)
        try {
            searchQueryChannel.asFlow()
                .debounce(300) // Add a debounce to avoid making too many network requests
                .flatMapLatest { query ->
                    if (query.isNullOrEmpty()) {
                        useCase.getListUseCase.getAlbumList()
                    } else {
                        useCase.getListUseCase.searchAlbums(query)
                    }
                }
                .map { pagingData ->
                    ViewState.Success(pagingData)
                }
                .catch { e ->
                    ViewState.Failure(e.localizedMessage ?: "Failed to retrieve data")
                }
                .collect { value ->
                    emit(value)
                }
        } catch (e: Exception) {
            emit(ViewState.Failure(e.localizedMessage ?: "Failed to retrieve data"))
        }
    }

    fun handleFavoriteAlbum(view: View, album: Album) {
        performCoroutineTask {
            val button: ImageButton = view as ImageButton
            val isCurrentlyFav = album.isFav
            album.isFav = !isCurrentlyFav
            if (isCurrentlyFav) {
                useCase.manageAlbumFavoriteUseCase.removeAlbumFromFavorites(album)
                button.setImageResource(R.drawable.ic_favorite_border)
            } else {
                useCase.manageAlbumFavoriteUseCase.setAlbumFavorite(album)
                button.setImageResource(R.drawable.ic_favorite)
            }
        }
    }

    fun getAllFavouriteAlbums(): Flow<List<Album>> {
        return flow {
            emit(useCase.manageAlbumFavoriteUseCase.getAlbumsList())
        }
    }

    fun isFavoriteAlbum(album: Album, callback: (Boolean) -> Unit) {
        performCoroutineTask {
            var result = useCase.manageAlbumFavoriteUseCase.isFavoriteAlbum(album)
            callback(result)
        }
    }
}