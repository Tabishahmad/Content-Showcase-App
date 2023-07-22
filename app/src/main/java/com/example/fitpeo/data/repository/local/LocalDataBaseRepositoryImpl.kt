package com.example.fitpeo.data.repository

import com.example.fitpeo.data.database.FavouriteAlbumDUO
import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.domain.repository.AlbumDataBaseRepository
import javax.inject.Inject

class DBRepositoryImpl @Inject constructor(private val favouriteAlbumDUO:FavouriteAlbumDUO):AlbumDataBaseRepository {
    override suspend fun setAlbumFavorite(album: Album) {
        favouriteAlbumDUO.markFavouriteAlbum(album)
    }

    override suspend fun removeAlbumFromFavorites(album: Album) {
        favouriteAlbumDUO.removeAlbumFromFavorites(album)
    }

    override suspend fun getAlbumsList(): List<Album> {
        return favouriteAlbumDUO.getAllFavoriteAlbums()
    }

    override suspend fun getAlbum(albumId:Int): Album {
        return favouriteAlbumDUO.getAlbum(albumId)
    }

}