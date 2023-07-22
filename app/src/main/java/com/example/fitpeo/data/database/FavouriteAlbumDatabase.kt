package com.example.fitpeo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitpeo.domain.model.Album

@Database(entities = [Album::class], version = 1)
abstract class FavouriteAlbumDatabase : RoomDatabase() {
    abstract fun favouriteAlbumDUO(): FavouriteAlbumDUO
}