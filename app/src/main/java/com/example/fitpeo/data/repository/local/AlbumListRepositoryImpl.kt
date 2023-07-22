//package com.example.fitpeo.data.repository.local
//
//import android.content.Context
//import com.example.fitpeo.R
//import com.example.fitpeo.common.toAlbumList
//import com.example.fitpeo.data.repository.remote.AlbumDataSource
//import com.example.fitpeo.domain.repository.AlbumListRepository
//import com.example.fitpeo.domain.model.Album
//import com.example.fitpeo.domain.model.NetworkResult
//import javax.inject.Inject
//
//class AlbumListRepositoryImpl @Inject
//        constructor(private val apiCall: AlbumDataSource, private val context: Context): AlbumListRepository {
//
//    override suspend fun getAlbumList(): NetworkResult<Album> {
//        println("getBookList(): NetworkResult<Book>")
//        return try {
//            val response = apiCall.downloadAlbumIdList()
//            if (response.isSuccessful) {
//                NetworkResult.Success(response.body().toAlbumList() ?: emptyList())
//            } else {
//                NetworkResult.Failure(context.getString(R.string.faild_to_retrive))
//            }
//        } catch (e: Exception) {
//            NetworkResult.Failure(context.getString(R.string.faild_to_retrive))
//        }
//    }
//}