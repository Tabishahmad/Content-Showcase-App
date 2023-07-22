package com.example.fitpeo.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkResultTest {

    @Test
    fun successTest() {
        val data = listOf("Item 1", "Item 2", "Item 3")
        val result: NetworkResult<String> = NetworkResult.Success(data)

        assertEquals(data, (result as NetworkResult.Success).data)
    }

    @Test
    fun failureTest() {
        val errorMessage = "Network request failed"
        val result: NetworkResult<String> = NetworkResult.Failure(errorMessage)

        assertEquals(errorMessage, (result as NetworkResult.Failure).failMessage)
    }
}
