package com.example.fitpeo.domain.repository


import androidx.paging.PagingData
import com.example.fitpeo.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumListRepository {
    fun getAlbumList(): Flow<PagingData<Album>>
    fun searchAlbums(query: String): Flow<PagingData<Album>>
}