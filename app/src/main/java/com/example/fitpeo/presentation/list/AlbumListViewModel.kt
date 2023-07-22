package com.example.fitpeo.presentation.list

import android.content.Context
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(private val useCase: UseCase, private val context: Context) :
    BaseViewModel() {

    private val uiStateFlow = MutableStateFlow<ViewState<List<Album>>>(ViewState.Loading(true))
    fun getviewStateFlow(): StateFlow<ViewState<List<Album>>> = uiStateFlow

//    private val _uiStateFlow = MutableStateFlow<ViewState>(ViewState.Loading(true))
//    val uiStateFlow = _uiStateFlow.asLiveData()


    private val fileNames = listOf("data1.json", "data2.json", "data3.json")


//    fun fetchList(): Flow<PagingData<Album>> {
//        return Pager(PagingConfig(pageSize = 20)) {
//            JsonDataSource(context, fileNames)
//        }.flow.cachedIn(viewModelScope)
//    }

     fun fetchList() = useCase.getListUseCase()

//    fun fetchList() {
//        performCoroutineTask{
//            useCase.getListUseCase().collect { result ->
//                uiStateFlow.emit(when (result) {
//                    is NetworkResult.Success -> ViewState.Success(result.data)
//                    is NetworkResult.Failure -> ViewState.Failure(context.getString(R.string.faild_to_retrive))
//                })
//            }
//        }
//    }

//    fun fetchList() {
//        performCoroutineTask{
//            _uiStateFlow.emit(ViewState.Loading)
//        }
//    }
//    val listData = Pager(PagingConfig(pageSize = 20, maxSize = 100)){
//        JsonDataSource(context, "data1.json")
//    }.flow.cachedIn(viewModelScope)

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