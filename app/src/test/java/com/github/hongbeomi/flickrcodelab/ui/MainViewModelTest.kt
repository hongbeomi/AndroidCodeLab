package com.github.hongbeomi.flickrcodelab.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.hongbeomi.flickrcodelab.TestCoroutineRule
import com.github.hongbeomi.flickrcodelab.data.source.FakePhotoDataFactory
import com.github.hongbeomi.flickrcodelab.data.source.FakePhotoListRepository
import com.github.hongbeomi.flickrcodelab.data.source.remote.EXCEPTION_MESSAGE_LIST_EMPTY
import com.github.hongbeomi.flickrcodelab.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var repository: FakePhotoListRepository
    private val factory = FakePhotoDataFactory()

    @Before
    fun setUp() {
        repository = FakePhotoListRepository()
        viewModel = MainViewModel(repository)
    }

    @Test
    fun givenNotPhotoList_WhenInitializeViewModel_ThenLoadingAndEmptyError() = runTest {
        // then
        val result = viewModel.mainUiState.take(2).toList()

        val loading = result.first() as MainViewModel.MainUiState.Loading
        val error = result.last() as MainViewModel.MainUiState.Error

        assertThat(loading, IsEqual(MainViewModel.MainUiState.Loading))
        assertThat(error.exception.message, IsEqual(EXCEPTION_MESSAGE_LIST_EMPTY))
    }

    @Test
    fun givenError_WhenInitializeViewModel_ThenLoadingAndError() = runTest {
        // given
        repository.setReturnError(true)

        // then
        val result = viewModel.mainUiState.take(2).toList()

        val loading = result.first() as MainViewModel.MainUiState.Loading
        val error = result.last() as MainViewModel.MainUiState.Error

        assertThat(loading, IsEqual(MainViewModel.MainUiState.Loading))
        assertThat(error.exception.message, IsEqual("Test Exception"))
    }

    @Test
    fun givenNewPhotoList_WhenRefreshViewModel_ThenLoadingAndRemotePhotoList() = runTest {
        // given
        val remoteData = listOf(factory.photo1, factory.photo2, factory.photo3)
        repository.addPhotoList(*remoteData.toTypedArray())

        // when
        viewModel.setUiAction(MainViewModel.MainUiAction.Refresh(true))

        // then
        val result = viewModel.mainUiState.take(2).toList()

        val loading = result.first() as MainViewModel.MainUiState.Loading
        val success = result.last() as MainViewModel.MainUiState.Success

        assertThat(loading,  IsEqual(MainViewModel.MainUiState.Loading))
        assertThat(success.photoList, IsEqual(remoteData))
    }

    @Test
    fun givenNextPage_WhenLoadMore_ThenLoadingMoreAndAddedPhotoList() = runTest {
        // given
        val remoteData = listOf(factory.photo1, factory.photo2, factory.photo3)
        repository.addPhotoList(*remoteData.toTypedArray())

        // loading, success
        viewModel.setUiAction(MainViewModel.MainUiAction.Refresh(true))

        val result = viewModel.mainUiState.take(2).toList()

        val loading = result.first() as MainViewModel.MainUiState.Loading
        val success = result.last() as MainViewModel.MainUiState.Success

        assertThat(loading,  IsEqual(MainViewModel.MainUiState.Loading))
        assertThat(success.photoList, IsEqual(remoteData))

        // then
        viewModel.setUiAction(MainViewModel.MainUiAction.LoadMore)

        advanceUntilIdle()
        val success1 = viewModel.mainUiState.value as MainViewModel.MainUiState.Success
        assertThat(success1.photoList, IsEqual(remoteData + remoteData))
    }

}