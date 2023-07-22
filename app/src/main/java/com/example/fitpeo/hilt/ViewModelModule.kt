package com.example.fitpeo.hilt

import android.content.Context
import com.example.fitpeo.data.repository.remote.AlbumListRepositoryImpl
import com.example.fitpeo.data.repository.remote.AlbumDataSource
import com.example.fitpeo.domain.repository.AlbumListRepository
import com.example.fitpeo.domain.repository.AlbumDataBaseRepository
import com.example.fitpeo.domain.usecase.UseCase
import com.example.fitpeo.domain.usecase.GetRemoteListUseCase
import com.example.fitpeo.domain.usecase.ManageAlbumFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideIListRepository(dataSource: AlbumDataSource, context: Context): AlbumListRepository {
        return AlbumListRepositoryImpl(dataSource,context)
    }
    @Provides
    fun provideAlbumUseCase(albumListRepository: AlbumListRepository,
                            dbRepository: AlbumDataBaseRepository):UseCase{
        return UseCase(GetRemoteListUseCase(albumListRepository),
            ManageAlbumFavoriteUseCase(dbRepository))
    }
}