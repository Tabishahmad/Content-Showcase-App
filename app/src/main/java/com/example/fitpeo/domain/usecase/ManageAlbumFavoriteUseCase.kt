package com.example.fitpeo.domain.usecase

import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.domain.repository.AlbumDataBaseRepository
import javax.inject.Inject

class ManageAlbumFavoriteUseCase @Inject constructor(val dbRepository: AlbumDataBaseRepository) {
    suspend fun setAlbumFavorite(album: Album){
        dbRepository.setAlbumFavorite(album)
    }
    suspend fun removeAlbumFromFavorites(album: Album){
        dbRepository.removeAlbumFromFavorites(album)
    }
    suspend fun isFavoriteAlbum(album: Album): Boolean {
        return dbRepository.getAlbum(album.id) != null
    }
    suspend fun getAlbumsList(): List<Album> {
        return dbRepository.getAlbumsList()
    }
}