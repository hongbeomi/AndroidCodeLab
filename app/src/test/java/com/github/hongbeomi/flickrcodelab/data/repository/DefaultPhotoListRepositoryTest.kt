package com.github.hongbeomi.flickrcodelab.data.repository

import com.github.hongbeomi.flickrcodelab.data.source.DefaultPhotoListRepository
import com.github.hongbeomi.flickrcodelab.data.source.FakePhotoListDataSource
import com.github.hongbeomi.shared.fixtures.Fixtures
import com.github.hongbeomi.shared.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultPhotoListRepositoryTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private val remotePhotoList = mutableListOf(Fixtures.photo(), Fixtures.photo(), Fixtures.photo())
    private val localPhotoList = mutableListOf(Fixtures.photo())

    private lateinit var defaultPhotosRepository: DefaultPhotoListRepository
    private lateinit var photosRemoteDataSource: FakePhotoListDataSource
    private lateinit var photosLocalDataSource: FakePhotoListDataSource

    @Before
    fun setUp() {
        photosLocalDataSource = FakePhotoListDataSource(localPhotoList)
        photosRemoteDataSource = FakePhotoListDataSource(remotePhotoList)

        defaultPhotosRepository = DefaultPhotoListRepository(
            photosLocalDataSource,
            photosRemoteDataSource,
            coroutineRule.dispatcher
        )
    }

    @Test
    fun givenEmptyPhotoList_WhenGetAllPhotoList_ThenLocalPhotoList() = runTest {
        // when
        val photoList = defaultPhotosRepository.getAllPhotoList()

        // then
        assertThat(photoList.single(), IsEqual(localPhotoList))
    }

    @Test
    fun givenRefreshPhotoList_WhenGetAllPhotoList_ThenUpdateRemotePhotoList() = runTest {
        // given
        defaultPhotosRepository.refreshPhotoList()

        //when
        val photoList = defaultPhotosRepository.getAllPhotoList()

        // then
        assertThat(photoList.single(), IsEqual(remotePhotoList))
    }

    @Test
    fun givenNextPage_WhenLoadMorePhotoList_ThenAddedPhotoList() = runTest {
        // given & when
        defaultPhotosRepository.refreshPhotoList()
        defaultPhotosRepository.loadMorePhotoList(2)

        //then
        val photoList = defaultPhotosRepository.getAllPhotoList()

        assertThat(photoList.single(), IsEqual(remotePhotoList + remotePhotoList))
    }

}