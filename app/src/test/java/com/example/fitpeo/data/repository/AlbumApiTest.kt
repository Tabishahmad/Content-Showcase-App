package com.example.fitpeo.data.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fitpeo.MainCoroutineRule
import com.example.fitpeo.MockFileReader
import com.example.fitpeo.data.repository.remote.AlbumDataSource
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class AlbumApiTest {

    lateinit var mockWebServer: MockWebServer
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var webClient: AlbumDataSource

    @Before
    fun initService(){
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val moshi = GsonConverterFactory.create()

        webClient = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(moshi)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(AlbumDataSource::class.java)
    }

    @Throws(IOException::class)
    fun mockResponseFromJson(fileName: String) {
        val mockResponse = MockResponse()
        mockWebServer.enqueue(
            mockResponse.setBody(
                MockFileReader().getResponseFromJson(fileName)
            )
        )
    }

    @Test
    fun testMovieListFromServer() {
        runBlocking {
            mockResponseFromJson("/ListItemsResponse.json")
            val movieListItemResponse = webClient.downloadAlbumIdList()

            val movieItemsListResponse = movieListItemResponse.body()

            Assert.assertEquals(movieItemsListResponse?.size, 5000)
        }
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}