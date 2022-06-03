package com.github.hongbeomi.flickrcodelab.data.source.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.hongbeomi.flickrcodelab.data.source.local.room.FlickrDatabase
import com.github.hongbeomi.shared.fixtures.Fixtures
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
        val photoList = listOf(
            Fixtures.photo(),
            Fixtures.photo(),
            Fixtures.photo()
        )
        localDataSource.insertPhotoList(photoList)

        // when
        val result = localDataSource.getSearchPhotoList().take(1).single()

        // then
        assertThat(result.getOrNull(0), `is`(photoList[0]))
        assertThat(result.getOrNull(1), `is`(photoList[1]))
        assertThat(result.getOrNull(2), `is`(photoList[2]))
    }

    @Test
    fun givenInsertPhotoList_WhenDeleteAllAndGetSearchPhotoList_ThenEmpty() = runTest {
        // given
        localDataSource.insertPhotoList(
            listOf(Fixtures.photo(), Fixtures.photo(), Fixtures.photo())
        )

        // when
        localDataSource.deleteAllPhotoList()
        val result = localDataSource.getSearchPhotoList().take(1).single()

        // then
        assertThat(result.isNullOrEmpty(), `is`(true))
    }

}