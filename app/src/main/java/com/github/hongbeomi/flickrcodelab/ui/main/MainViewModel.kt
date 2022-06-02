package com.github.hongbeomi.flickrcodelab.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.hongbeomi.flickrcodelab.data.source.PhotoListRepository
import com.github.hongbeomi.domain.Photo
import com.github.hongbeomi.flickrcodelab.utils.Pager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MainViewModel(
    private val photoListRepository: PhotoListRepository
) : ViewModel() {

    private val debounceTimeoutMillis = 1000L

    private val _mainUiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)
    val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()

    private val mainUiAction: MutableSharedFlow<MainUiAction> = MutableSharedFlow()

    private val pager = Pager()
    private val viewModelScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _mainUiState.update { MainUiState.Error(throwable) }
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            mainUiAction
                .onEach {
                    if (it is MainUiAction.Refresh) {
                        _mainUiState.update { MainUiState.Loading }
                    }
                }
                .debounce(debounceTimeoutMillis)
                .collect {
                    when (it) {
                        is MainUiAction.LoadMore -> loadMore()
                        is MainUiAction.Refresh -> refresh(it.isForceUpdate)
                    }
                }
        }
        refresh(true)
    }

    private fun refresh(isForceUpdate: Boolean) {
        _mainUiState.update { MainUiState.Loading }

        viewModelScope.launch(exceptionHandler) {
            pager.reset()
            pager.load {
                photoListRepository
                    .getAllPhotoList(isForceUpdate)
                    .distinctUntilChanged()
                    .collect { photoList ->
                        _mainUiState.update { MainUiState.Success(photoList) }
                    }
                (_mainUiState.value as? MainUiState.Success)?.photoList?.isNotEmpty()
            }
        }
    }

    private fun loadMore() {
        viewModelScope.launch(exceptionHandler) {
            pager.load {
                photoListRepository.loadMorePhotoList(it)
            }
        }
    }

    fun setUiAction(action: MainUiAction) {
        viewModelScope.launch(exceptionHandler) {
            mainUiAction.emit(action)
        }
    }

    override fun onCleared() {
        pager.reset()
        viewModelScope.coroutineContext.cancel()
        super.onCleared()
    }

    sealed interface MainUiState {
        data class Success(val photoList: List<com.github.hongbeomi.domain.Photo> = emptyList()) : MainUiState
        object Loading : MainUiState
        data class Error(val exception: Throwable) : MainUiState

        fun isLoading() = this is Loading
        fun isError() = this is Error
        fun isSuccess() = this is Success
    }

    sealed interface MainUiAction {
        data class Refresh(val isForceUpdate: Boolean) : MainUiAction
        object LoadMore : MainUiAction
    }

}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val photoListRepository: PhotoListRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(photoListRepository) as T
    }

}