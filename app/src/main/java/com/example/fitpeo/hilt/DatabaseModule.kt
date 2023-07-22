package com.example.fitpeo.hilt

import android.content.Context
import androidx.room.Room
import com.example.fitpeo.common.DATABASE_TABLE_NAME
import com.example.fitpeo.data.database.FavouriteAlbumDUO
import com.example.fitpeo.data.database.FavouriteAlbumDatabase
import com.example.fitpeo.data.repository.DBRepositoryImpl
import com.example.fitpeo.domain.repository.AlbumDataBaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideFavouriteAlbumDUO(favouriteAlbumDUO: FavouriteAlbumDatabase): FavouriteAlbumDUO {
        return favouriteAlbumDUO.favouriteAlbumDUO()
    }
    @Provides
    fun provideDatabaseRepo(favouriteAlbumDUO: FavouriteAlbumDUO):AlbumDataBaseRepository{
        return DBRepositoryImpl(favouriteAlbumDUO)
    }
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): FavouriteAlbumDatabase {
        return Room.databaseBuilder(
            appContext,
            FavouriteAlbumDatabase::class.java,
            DATABASE_TABLE_NAME
        ).build()
    }
}