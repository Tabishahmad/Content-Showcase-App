package com.example.fitpeo.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.TestDispatcherRule
import com.example.fitpeo.domain.model.Album
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class FavouriteAlbumDUOTest : TestCase() {

    private lateinit var favouriteBookDUO: FavouriteAlbumDUO
    private lateinit var favouriteBookDatabase: FavouriteAlbumDatabase

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    @Before
    public override fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        // initialize the db and dao variable
        favouriteBookDatabase = Room.inMemoryDatabaseBuilder(context,FavouriteAlbumDatabase::class.java).build()
        favouriteBookDUO = favouriteBookDatabase.favouriteAlbumDUO()
    }

    @Test
    fun addItem_shouldReturn_theItem_inFlow() = runTest {
        val item1 = Album(1, 22, "A1B2", "Test Book", "http://www.example.com")
        val item2 = Album(1, 23, "A2B3", "Test Book 2", "http://www.example.com")
        favouriteBookDUO.markFavouriteAlbum(item1)
        favouriteBookDUO.markFavouriteAlbum(item2)

        val list = favouriteBookDUO.getAllFavoriteAlbums()
        assert(list.contains(item1))
        assert(list.contains(item2))
    }

    @Test
    fun deletedItem_shouldNot_be_present_inFlow() = runTest() {
        val item1 = Album(1, 22, "A1B2", "Test Book", "http://www.example.com")
        val item2 = Album(1, 23, "A2B3", "Test Book 2", "http://www.example.com")
        favouriteBookDUO.markFavouriteAlbum(item1)
        favouriteBookDUO.markFavouriteAlbum(item2)
        favouriteBookDUO.removeAlbumFromFavorites(item2)

        val list = favouriteBookDUO.getAllFavoriteAlbums()
        assert(list.size == 1)
        assert(list.contains(item1))
        }
//
    @Test
    fun updateItem_shouldReturn_theItem_inFlow() = runTest {
        val item1 = Album(1, 22, "A1B2", "Test Book", "http://www.example.com")
        val item2 = Album(1, 23, "A2B3", "Test Book 2", "http://www.example.com")
        val item3 = Album(1,23,"A2B3","Test Book 3","http://www.example.com")

        favouriteBookDUO.markFavouriteAlbum(item1)
        favouriteBookDUO.markFavouriteAlbum(item2)
        favouriteBookDUO.markFavouriteAlbum(item3)

        val list = favouriteBookDUO.getAllFavoriteAlbums()
        assert(list.size == 2)
        assert(list.contains(item3))
    }

    @After
    public fun closeDatabase() {
        favouriteBookDatabase.close()
    }
}