package com.github.hongbeomi.flickrcodelab.data.repository

import com.github.hongbeomi.flickrcodelab.domain.model.Photo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultFlickrRepositoryTest {

    private val photo1 = Photo(id = 1, secret = "secret1", server = 1)
    private val photo2 = Photo(id = 2, secret = "secret2", server = 2)
    private val photo3 = Photo(id = 3, secret = "secret3", server = 2)
    private val recentTask = listOf(photo1, photo2, photo3)

    private lateinit var defaultFlickrRepository: DefaultFlickrRepository

    @Before
    fun setUp() {
        defaultFlickrRepository = DefaultFlickrRepository()
    }

    @Test
    fun getRecentPhotos_requestAllPhotosFromRemote() = runBlockingTest {
        val photos: Result<List<Photo>> = defaultFlickrRepository.getRecentPhotos()

        assertThat(photos.getOrNull(), IsEqual(recentTask))
    }

}