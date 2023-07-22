package com.example.fitpeo.data.repository.remote

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import com.example.fitpeo.data.repository.local.JsonDataSource
import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.domain.repository.AlbumListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlbumListRepositoryImpl @Inject
        constructor(private val context: Context,private val fileNames: List<String>): AlbumListRepository {

    override fun getAlbumList(): Flow<PagingData<Album>> {
        return Pager(PagingConfig(pageSize = 20)) {
            JsonDataSource(context, fileNames)
        }.flow
    }
    override fun searchAlbums(query: String): Flow<PagingData<Album>> {
        return Pager(PagingConfig(pageSize = 20)) {
            JsonDataSource(context, fileNames)
        }.flow.map { pagingData ->
            pagingData.filter { album ->
                album.name.contains(query, ignoreCase = true)
            }
        }
    }
}