package com.github.hongbeomi.flickrcodelab.data.source.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.hongbeomi.flickrcodelab.data.source.FakePhotoDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class PhotoListLocalDataSourceTest {

    private lateinit var database: FlickrDatabase
    private lateinit var localDataSource: PhotoListLocalDataSource
    private val factory = FakePhotoDataFactory()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FlickrDatabase::class.java
        ).build()

        localDataSource = PhotoListLocalDataSource(database.flickrDao())
    }

    @Before
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenEmpty_WhenGetSearchPhotoList_ThenEmpty() = runTest {
        // when
        val result = localDataSource.getSearchPhotoList().take(1).single()

        // then
        assertThat(result.isNullOrEmpty(), `is`(true))
    }

    @Test
    fun givenInsertPhotoList_WhenGetSearchPhotoList_ThenSavedPhotoList() = runTest {
        // given
        localDataSource.insertPhotoList(
            listOf(factory.photo1, factory.photo2, factory.photo3)
        )

        // when
        val result = localDataSource.getSearchPhotoList().take(1).single()

        // then
        assertThat(result.getOrNull(0), `is`(factory.photo1))
        assertThat(result.getOrNull(1), `is`(factory.photo2))
        assertThat(result.getOrNull(2), `is`(factory.photo3))
    }

    @Test
    fun givenInsertPhotoList_WhenDeleteAllAndGetSearchPhotoList_ThenEmpty() = runTest {
        // given
        localDataSource.insertPhotoList(
            listOf(factory.photo1, factory.photo2, factory.photo3)
        )

        // when
        localDataSource.deleteAllPhotoList()
        val result = localDataSource.getSearchPhotoList().take(1).single()

        // then
        assertThat(result.isNullOrEmpty(), `is`(true))
    }

}