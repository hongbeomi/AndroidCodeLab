package com.github.hongbeomi.flickrcodelab.data.repository

import com.github.hongbeomi.flickrcodelab.data.source.DefaultPhotosRepository
import com.github.hongbeomi.flickrcodelab.data.source.remote.FakePhotosDataSource
import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultPhotosRepositoryTest {

    private val photo1 = Photo(
        id = 1,
        secret = "secret1",
        server = 1
    )
    private val photo2 = Photo(id = 2, secret = "secret2", server = 2)
    private val photo3 = Photo(id = 3, secret = "secret3", server = 2)
    private val remotePhotoList = mutableListOf(photo1, photo2, photo3)
    private val localPhotoList = mutableListOf(photo1)

    private lateinit var defaultPhotosRepository: DefaultPhotosRepository
    private lateinit var photosRemoteDataSource: FakePhotosDataSource
    private lateinit var photosLocalDataSource: FakePhotosDataSource

    @Before
    fun setUp() {
        photosLocalDataSource = FakePhotosDataSource(localPhotoList)
        photosRemoteDataSource = FakePhotosDataSource(remotePhotoList)

        defaultPhotosRepository = DefaultPhotosRepository(
            photosLocalDataSource,
            photosRemoteDataSource
        )
    }

    @Test
    fun givenRefreshPhotos_whenGetAllPhotos_thenUpdateRemotePhotos() = runBlockingTest {
        // given
        defaultPhotosRepository.refreshPhotos()

        //when
        val photoList = defaultPhotosRepository.getAllPhotos()

        // then
        assertThat(photoList.single(), IsEqual(remotePhotoList))
    }

}