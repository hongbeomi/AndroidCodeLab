package com.github.hongbeomi.flickrcodelab.data.repository

import com.github.hongbeomi.flickrcodelab.data.source.DefaultPhotoListRepository
import com.github.hongbeomi.flickrcodelab.data.source.FakePhotoDataFactory
import com.github.hongbeomi.flickrcodelab.data.source.remote.FakePhotosDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultPhotoListRepositoryTest {

    private val factory = FakePhotoDataFactory()
    private val remotePhotoList = mutableListOf(factory.photo1, factory.photo2, factory.photo3)
    private val localPhotoList = mutableListOf(factory.photo1)

    private lateinit var defaultPhotosRepository: DefaultPhotoListRepository
    private lateinit var photosRemoteDataSource: FakePhotosDataSource
    private lateinit var photosLocalDataSource: FakePhotosDataSource

    @Before
    fun setUp() {
        photosLocalDataSource = FakePhotosDataSource(localPhotoList)
        photosRemoteDataSource = FakePhotosDataSource(remotePhotoList)

        defaultPhotosRepository = DefaultPhotoListRepository(
            photosLocalDataSource,
            photosRemoteDataSource
        )
    }

    @Test
    fun givenNotRefreshPhotos_whenGetAllPhotos_thenLocalPhotos() = runTest {
        // when
        val photoList = defaultPhotosRepository.getAllPhotoList()

        // then
        assertThat(photoList.single(), IsEqual(localPhotoList))
    }

    @Test
    fun givenRefreshPhotos_whenGetAllPhotos_thenUpdateRemotePhotos() = runTest {
        // given
        defaultPhotosRepository.refreshPhotoList()

        //when
        val photoList = defaultPhotosRepository.getAllPhotoList()

        // then
        assertThat(photoList.single(), IsEqual(remotePhotoList))
    }

}