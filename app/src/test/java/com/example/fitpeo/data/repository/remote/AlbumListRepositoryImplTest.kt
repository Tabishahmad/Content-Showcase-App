package com.example.fitpeo.data.repository.remote

import android.content.Context
import com.example.fitpeo.R
import com.example.fitpeo.common.toAlbumList
import com.example.fitpeo.data.repository.model.AlbumDTO
import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.domain.model.NetworkResult
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class AlbumListRepositoryImplTest {

    @Mock
    private lateinit var apiCall: AlbumDataSource

    @Mock
    private lateinit var context: Context

    private lateinit var repository: AlbumListRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = AlbumListRepositoryImpl(apiCall, context)
    }

    @Test
    fun getAlbumList_successResponse_shouldReturnSuccessResult() {
        runTest {
            // Arrange
            val albumList = listOf(AlbumDTO(1, 21, "Title", "www.imgurl.com", "www.thumbnailurl.com"))
            val response: Response<List<AlbumDTO>> = Response.success(albumList)
            `when`(apiCall.downloadAlbumIdList()).thenReturn(response)
            `when`(context.getString(R.string.faild_to_retrive)).thenReturn("Failed to retrieve")

            // NetworkResult.Success(response.body().toAlbumList() ?: emptyList())
            // Act
            val result = repository.getAlbumList()

            // Assert
            assertEquals(NetworkResult.Success(response.body().toAlbumList()), result)
        }
    }

    @Test
    fun getAlbumList_errorResponse_shouldReturnFailureResult() {
        runBlocking {
            // Arrange
            val response: Response<List<AlbumDTO>> = Response.error(404, okhttp3.ResponseBody.create(null, "Error"))
            `when`(apiCall.downloadAlbumIdList()).thenReturn(response)
            `when`(context.getString(R.string.faild_to_retrive)).thenReturn("Failed to retrieve")

            // Act
            val result = repository.getAlbumList()

            // Assert
            assertEquals(NetworkResult.Failure("Failed to retrieve"), result)
        }
    }

    @Test
    fun getAlbumList_exceptionThrown_shouldReturnFailureResult() {
        runBlocking {
            // Arrange
            `when`(apiCall.downloadAlbumIdList()).thenThrow(RuntimeException())
            `when`(context.getString(R.string.faild_to_retrive)).thenReturn("Failed to retrieve")

            // Act
            val result = repository.getAlbumList()

            // Assert
            assertEquals(NetworkResult.Failure("Failed to retrieve"), result)
        }
    }
}
