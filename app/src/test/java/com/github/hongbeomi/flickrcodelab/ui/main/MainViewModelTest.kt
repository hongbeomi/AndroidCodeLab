package com.github.hongbeomi.flickrcodelab.ui.main

import com.github.hongbeomi.domain.EXCEPTION_MESSAGE_LIST_EMPTY
import com.github.hongbeomi.shared.fixtures.Fixtures
import com.github.hongbeomi.shared.repository.FakePhotoListRepository
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
    val coroutineRule = com.github.hongbeomi.shared.TestCoroutineRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var repository: FakePhotoListRepository

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
        val remoteData = listOf(Fixtures.photo(), Fixtures.photo(), Fixtures.photo())
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
        val remoteData = listOf(Fixtures.photo(), Fixtures.photo(), Fixtures.photo())
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