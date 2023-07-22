package com.example.fitpeo.domain.repository


import androidx.paging.PagingData
import com.example.fitpeo.domain.model.Album
import kotlinx.coroutines.flow.Flow

fun interface AlbumListRepository {
    fun getAlbumList(): Flow<PagingData<Album>>
}