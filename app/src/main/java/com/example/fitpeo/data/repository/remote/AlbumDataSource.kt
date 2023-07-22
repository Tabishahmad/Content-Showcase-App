package com.example.fitpeo.data.repository.remote

import com.example.fitpeo.data.repository.model.AlbumDTO
import retrofit2.Response
import retrofit2.http.GET

interface AlbumDataSource {
    @GET("photos")
    suspend fun downloadAlbumIdList() : Response<List<AlbumDTO>>
}