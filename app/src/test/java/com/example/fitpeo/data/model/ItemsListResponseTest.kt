package com.example.fitpeo.data.model

import com.example.fitpeo.MockFileReader
import com.example.fitpeo.data.repository.model.AlbumDTO
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert
import org.junit.Test

class ItemsListResponseTest {

    @Test
    fun createMovieItemFromJson() {
        val fileName = "/ListItemsResponse.json"
        val json = MockFileReader().getResponseFromJson(fileName)

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter<List<AlbumDTO>>(
            Types.newParameterizedType(List::class.java, AlbumDTO::class.java)
        )
        val itemsListResponse = adapter.fromJson(json)

        Assert.assertEquals(5000, itemsListResponse?.size)
        Assert.assertEquals(1, itemsListResponse?.get(0)?.id)

        val bookItem = itemsListResponse?.get(0)

        Assert.assertEquals("accusamus beatae ad facilis cum similique qui sunt",bookItem?.title)

    }

}