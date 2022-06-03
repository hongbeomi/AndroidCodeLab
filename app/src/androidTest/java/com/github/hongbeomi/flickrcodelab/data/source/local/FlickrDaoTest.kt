package com.github.hongbeomi.flickrcodelab.data.source.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.hongbeomi.flickrcodelab.data.source.local.room.FlickrDatabase
import com.github.hongbeomi.flickrcodelab.model.local.toEntity
import com.github.hongbeomi.shared.fixtures.Fixtures
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4::class)
class FlickrDaoTest {

    private lateinit var database: FlickrDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FlickrDatabase::class.java
        ).build()
    }

    @Before
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenEmpty_WhenGetAll_ThenEmptyPhotoList() = runTest {
        // when
        val photoList = database.flickrDao().getAll()

        // then
        val result = photoList.take(1).single()
        assertThat(result.isNullOrEmpty(), `is`(true))
    }

    @Test
    fun givenInsertPhotoList_WhenGetAll_ThenPhotoList() = runTest {
        // given
        val photoList = listOf(
            Fixtures.photo().toEntity(),
            Fixtures.photo().toEntity(),
            Fixtures.photo().toEntity()
        )
        database.flickrDao().insertAll(photoList)

        // when
        val result = database.flickrDao().getAll().take(1).single()
        val entity1 = result.getOrNull(0)
        val entity2 = result.getOrNull(1)
        val entity3 = result.getOrNull(2)

        // then
        assertThat(entity1?.id, `is`(photoList[0].id))
        assertThat(entity1?.farm, `is`(photoList[0].farm))
        assertThat(entity1?.secret, `is`(photoList[0].secret))
        assertThat(entity1?.server, `is`(photoList[0].server))

        assertThat(entity2?.id, `is`(photoList[1].id))
        assertThat(entity2?.farm, `is`(photoList[1].farm))
        assertThat(entity2?.secret, `is`(photoList[1].secret))
        assertThat(entity2?.server, `is`(photoList[1].server))

        assertThat(entity3?.id, `is`(photoList[2].id))
        assertThat(entity3?.farm, `is`(photoList[2].farm))
        assertThat(entity3?.secret, `is`(photoList[2].secret))
        assertThat(entity3?.server, `is`(photoList[2].server))
    }

    @Test
    fun givenInsertPhotoList_WhenDeleteAllAndGetAll_ThenEmpty() = runTest {
        // given
        database.flickrDao().insertAll(
            listOf(
                Fixtures.photo().toEntity(),
                Fixtures.photo().toEntity(),
                Fixtures.photo().toEntity(),
            )
        )

        // when
        database.flickrDao().deleteAll()
        val result = database.flickrDao().getAll().take(1).single()

        // then
        assertThat(result.isNullOrEmpty(), `is`(true))
    }

}