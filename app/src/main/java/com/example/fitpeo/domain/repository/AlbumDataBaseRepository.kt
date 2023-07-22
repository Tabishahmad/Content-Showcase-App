package com.example.fitpeo.domain.repository

import com.example.fitpeo.domain.model.Album


interface AlbumDataBaseRepository {
    suspend fun setAlbumFavorite(album: Album)
    suspend fun removeAlbumFromFavorites(album: Album)
    suspend fun getAlbumsList(): List<Album>
    suspend fun getAlbum(albumId: Int):Album?
}