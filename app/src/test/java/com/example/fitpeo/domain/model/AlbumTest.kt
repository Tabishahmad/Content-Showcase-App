package com.example.fitpeo.domain.model

import com.example.fitpeo.domain.model.Album
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumTest {

    @Test
    fun albumCreationTest() {
        // Create an instance of Album
        val album = Album(1, 21, "This is a book", "www.imgurl.com", "www.thumbnailurl.com", false)

        // Verify the values of the properties
        assertEquals(1, album.albumId)
        assertEquals(21, album.id)
        assertEquals("This is a book", album.title)
        assertEquals("www.imgurl.com", album.url)
        assertEquals("www.thumbnailurl.com", album.thumbnailUrl)
        assertEquals(false, album.isFav)
    }

    @Test
    fun albumIsFavTest() {
        // Create an instance of Album with isFav set to true
        val album = Album(1, 21, "This is a book", "www.imgurl.com", "www.thumbnailurl.com", true)

        // Verify that isFav property is set correctly
        assertEquals(true, album.isFav)
    }
}
