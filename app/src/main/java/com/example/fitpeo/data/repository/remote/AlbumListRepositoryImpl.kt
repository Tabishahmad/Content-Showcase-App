package com.example.fitpeo.data.repository.remote

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.fitpeo.data.repository.local.JsonDataSource
import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.domain.repository.AlbumListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumListRepositoryImpl @Inject
        constructor(private val apiCall: AlbumDataSource, private val context: Context): AlbumListRepository {
    private val fileNames = listOf("data1.json", "data2.json", "data3.json")

    override fun getAlbumList(): Flow<PagingData<Album>> {
        return Pager(PagingConfig(pageSize = 20)) {
            JsonDataSource(context, fileNames)
        }.flow
    }
}