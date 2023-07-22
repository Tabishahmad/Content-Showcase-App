package com.example.fitpeo.data.repository.local

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.fitpeo.common.MAX_PAGE
import com.example.fitpeo.data.repository.model.AlbumDTO
import com.example.fitpeo.domain.model.Album
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class JsonDataSource @Inject constructor(private val context: Context, private val fileNames: List<String>
) : PagingSource<Int, Album>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        val pageNumber = params.key ?: 0
        val currentFileName = fileNames.getOrNull(pageNumber)

        return try {
            currentFileName?.let { fileName ->

                val json = loadJsonFromAsset(context, fileName)
                val items = parseJson(json)

                val prevKey = if (pageNumber > 0) pageNumber - 1 else null
                val nextKey = if (pageNumber < fileNames.size - 1) pageNumber + 1 else null
                println("pageNumber fileName " +fileName + " pageNumber " + pageNumber )
                LoadResult.Page(
                    data = items,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } ?: LoadResult.Error(IllegalArgumentException("Invalid page number: $pageNumber"))
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    private fun loadJsonFromAsset(context: Context, fileName: String): String {
        println("pageNumber loadJsonFromAsset " +fileName )
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    private fun parseJson(jsonString: String): List<Album> {
        val gson = Gson()
        val itemArrayType = object : TypeToken<AlbumDTO>() {}.type
        val dto : AlbumDTO = gson.fromJson(jsonString, itemArrayType)
        return dto.toAlbum()
    }
}
