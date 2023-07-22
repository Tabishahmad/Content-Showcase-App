package com.example.fitpeo.domain.usecase

import androidx.paging.PagingData
import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.domain.model.NetworkResult
import com.example.fitpeo.domain.repository.AlbumListRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetRemoteListUseCase @Inject constructor (
    private val repository: AlbumListRepository
) {
    fun getAlbumList(): Flow<PagingData<Album>> = repository.getAlbumList()
    fun searchAlbums(query : String): Flow<PagingData<Album>> = repository.searchAlbums(query)
}