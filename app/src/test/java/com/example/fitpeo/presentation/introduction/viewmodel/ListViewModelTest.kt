package com.example.fitpeo.presentation.introduction.viewmodel

import android.content.Context
import com.example.fitpeo.MainCoroutineRule
import com.example.fitpeo.MockFileReader
import com.example.fitpeo.R
import com.example.fitpeo.common.toAlbumList
import com.example.fitpeo.data.repository.model.AlbumDTO
import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.domain.model.NetworkResult
import com.example.fitpeo.domain.repository.AlbumDataBaseRepository
import com.example.fitpeo.domain.usecase.GetRemoteListUseCase
import com.example.fitpeo.domain.usecase.ManageAlbumFavoriteUseCase
import com.example.fitpeo.domain.usecase.UseCase
import com.example.fitpeo.presentation.core.ViewState
import com.example.fitpeo.presentation.list.AlbumListViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@HiltAndroidTest
internal class ListViewModelTest {

    private lateinit var viewModel: AlbumListViewModel

    private lateinit var useCase: UseCase
    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var album: Album
    @Mock
    private lateinit var getRemoteListUseCase: GetRemoteListUseCase
    @Mock
    private lateinit var manageAlbumFavoriteUseCase: ManageAlbumFavoriteUseCase

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        useCase = UseCase(getRemoteListUseCase,manageAlbumFavoriteUseCase)
        viewModel = AlbumListViewModel(useCase,context)
    }

    private fun getListRespose(): List<Album> {

        val fileName = "/ListItemsResponse.json"
        val json = MockFileReader().getResponseFromJson(fileName)

        val itemsListResponse = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            .adapter<List<AlbumDTO>>(
            Types.newParameterizedType(List::class.java, AlbumDTO::class.java)
        ).fromJson(json)

        return itemsListResponse.toAlbumList()
    }
    @Test
    fun fetchListSuccessTest() = coroutineRule.runBlockingTest {
        val flow = flow{
            emit(NetworkResult.Success(getListRespose()))
        }

        Mockito.`when`(useCase.getListUseCase()).thenReturn(flow)

        viewModel.fetchList()

        flow.collect {
            Assert.assertEquals(5000, it.data?.size )
            Assert.assertEquals("accusamus beatae ad facilis cum similique qui sunt", it.data?.get(0)?.title)
        }
    }

    @Test
    fun fetchListLoadingTest() = coroutineRule.runBlockingTest {
        Assert.assertEquals(
            ViewState.Loading(true), viewModel.getviewStateFlow().value)
    }
    @Test
    fun `fetchList() with failure should emit ViewState Failure`() = coroutineRule.runBlockingTest {
        // Mock the failure scenario
        val errorMessage = "Failed to retrieve data"
        val flow = flow{
            emit(NetworkResult.Failure(errorMessage))
        }
        Mockito.`when`(useCase.getListUseCase()).thenReturn(flow)

        // Mock the behavior of context.getString
        Mockito.`when`(context.getString(R.string.faild_to_retrive)).thenReturn(errorMessage)

        // Call the fetchList() function
        viewModel.fetchList()


        // Verify the emitted ViewState
        val expectedViewState = ViewState.Failure(errorMessage)
        val actualViewState = viewModel.getviewStateFlow().value
        Assert.assertEquals(expectedViewState, actualViewState)
    }
    @Test
    fun `handleFavoriteBook should update the isFav property and call the appropriate use case`() = runTest{
        // Create a mock of the AlbumDataBaseRepository
        val mockDbRepository: AlbumDataBaseRepository = Mockito.mock(AlbumDataBaseRepository::class.java)

        // Create a sample album
        val album = Album(1, 21, "This is a book", "www.imgurl.com", "www.thumbnailurl.com", false)

        // Create an instance of ManageAlbumFavoriteUseCase with the mock repository
        val manageAlbumFavoriteUseCase = ManageAlbumFavoriteUseCase(mockDbRepository)

        // Arrange
        val isCurrentlyFav = true
        album.isFav = isCurrentlyFav

        // Set up the mock behavior
        Mockito.`when`(mockDbRepository.removeAlbumFromFavorites(album)).thenReturn(Unit)

        // Act
        manageAlbumFavoriteUseCase.removeAlbumFromFavorites(album)

        // Assert
        Mockito.verify(mockDbRepository).removeAlbumFromFavorites(album)
    }

    @Test
    fun `getAllFavouriteBooks should return the list of favorite books`() = coroutineRule.runBlockingTest {
        // Arrange
        val favoriteBooks = listOf(album)
        Mockito.`when`(useCase.manageAlbumFavoriteUseCase.getAlbumsList()).thenReturn(favoriteBooks)

        // Act
        val result = viewModel.getAllFavouriteAlbums().single()

        // Assert
        Assert.assertEquals(favoriteBooks, result)
    }
//
    @Test
    fun `isFavoriteBook should call the appropriate use case and invoke the callback with the result`() = runBlockingTest {
        // Arrange
        val expectedResult = true
        Mockito.`when`(useCase.manageAlbumFavoriteUseCase.isFavoriteAlbum(album)).thenReturn(expectedResult)
        var actualResult: Boolean? = null

        // Act
        viewModel.isFavoriteAlbum(album) { result ->
            actualResult = result
        }

        // Assert
        Assert.assertEquals(expectedResult, actualResult)
    }
    @After
    fun tearDown() {
    }
}