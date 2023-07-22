package com.example.fitpeo.data.repository

import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.domain.repository.AlbumDataBaseRepository


class FakeDBRepository : AlbumDataBaseRepository {
    private val albumsList: MutableList<Album> = mutableListOf()
    private val favoritesSet: MutableSet<Int> = mutableSetOf()

    override suspend fun setAlbumFavorite(album: Album) {
        favoritesSet.add(album.id)
    }

    override suspend fun removeAlbumFromFavorites(album: Album) {
        favoritesSet.remove(album.id)
    }

    override suspend fun getAlbumsList(): List<Album> {
        return albumsList.toList()
    }

    override suspend fun getAlbum(albumId: Int): Album? {
        return albumsList.find { it.id == albumId }
    }

    // Additional methods for testing purposes
    fun addAlbum(album: Album) {
        albumsList.add(album)
    }

    fun addFavorite(albumId: Int) {
        favoritesSet.add(albumId)
    }

    fun clearData() {
        albumsList.clear()
        favoritesSet.clear()
    }
}
