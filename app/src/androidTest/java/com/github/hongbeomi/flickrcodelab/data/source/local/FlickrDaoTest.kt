package com.github.hongbeomi.flickrcodelab.data.source.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.hongbeomi.flickrcodelab.data.source.FakePhotoDataFactory
import com.github.hongbeomi.flickrcodelab.model.local.toEntity
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
    private val factory = FakePhotoDataFactory()

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
        database.flickrDao().insertAll(
            listOf(
                factory.photo1.toEntity(),
                factory.photo2.toEntity(),
                factory.photo3.toEntity(),
            )
        )

        // when
        val result = database.flickrDao().getAll().take(1).single()
        val entity1 = result.getOrNull(0)
        val entity2 = result.getOrNull(1)
        val entity3 = result.getOrNull(2)

        // then
        assertThat(entity1?.id, `is`(factory.photo1.id))
        assertThat(entity1?.farm, `is`(factory.photo1.farm))
        assertThat(entity1?.secret, `is`(factory.photo1.secret))
        assertThat(entity1?.server, `is`(factory.photo1.server))

        assertThat(entity2?.id, `is`(factory.photo2.id))
        assertThat(entity2?.farm, `is`(factory.photo2.farm))
        assertThat(entity2?.secret, `is`(factory.photo2.secret))
        assertThat(entity2?.server, `is`(factory.photo2.server))

        assertThat(entity3?.id, `is`(factory.photo3.id))
        assertThat(entity3?.farm, `is`(factory.photo3.farm))
        assertThat(entity3?.secret, `is`(factory.photo3.secret))
        assertThat(entity3?.server, `is`(factory.photo3.server))
    }

    @Test
    fun givenInsertPhotoList_WhenDeleteAllAndGetAll_ThenEmpty() = runTest {
        // given
        database.flickrDao().insertAll(
            listOf(
                factory.photo1.toEntity(),
                factory.photo2.toEntity(),
                factory.photo3.toEntity(),
            )
        )

        // when
        database.flickrDao().deleteAll()
        val result = database.flickrDao().getAll().take(1).single()

        // then
        assertThat(result.isNullOrEmpty(), `is`(true))
    }

}