package com.example.fitpeo.data.model

import com.example.fitpeo.domain.model.Album
import com.example.fitpeo.domain.model.NetworkResult
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.mockito.Mockito.mock
import org.junit.Assert.*

class WebServiceResponseTest {

    @Test
    fun testOnSuccess(){
        val book = mock(Album::class.java)
        var list : ArrayList<Album> = ArrayList(1)
        list.add(book)
        val success = NetworkResult.Success(list)
        assertThat(success, CoreMatchers.instanceOf(NetworkResult.Success::class.java))
    }

    @Test
    fun testOnFailure() {
        val error = NetworkResult.Failure("")
        assertThat(error, CoreMatchers.instanceOf(NetworkResult.Failure::class.java))
    }
}