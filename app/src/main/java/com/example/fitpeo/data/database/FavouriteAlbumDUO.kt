package com.example.fitpeo.data.database

import androidx.room.*
import com.example.fitpeo.common.DATABASE_TABLE_NAME
import com.example.fitpeo.domain.model.Album


@Dao
interface FavouriteAlbumDUO {

    @Query("SELECT * FROM $DATABASE_TABLE_NAME")
    suspend fun getAllFavoriteAlbums(): List<Album>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markFavouriteAlbum(album: Album)

    @Delete
    suspend fun removeAlbumFromFavorites(album: Album)

    @Query("SELECT * FROM $DATABASE_TABLE_NAME WHERE id = :albumId")
    suspend fun getAlbum(albumId: Int): Album
}