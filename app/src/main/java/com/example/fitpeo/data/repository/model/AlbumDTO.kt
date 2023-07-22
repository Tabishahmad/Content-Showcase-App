package com.example.fitpeo.data.repository.model

import com.example.fitpeo.domain.model.Album


data class AlbumDTO(val page: Page) {

    fun toAlbum(): List<Album> {
        val list = page.contentItems.content
        val bookArray: ArrayList<Album> = ArrayList(list.size)
        // Assuming that the albumId is sequential, starting from 1
        var albumIdCounter = 1
        for (item in list) {
            bookArray.add(Album(albumIdCounter++, item.name, item.posterImage))
        }
        return bookArray
    }
}