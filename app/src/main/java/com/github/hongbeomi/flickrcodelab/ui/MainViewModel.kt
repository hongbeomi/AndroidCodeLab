package com.github.hongbeomi.flickrcodelab.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.hongbeomi.flickrcodelab.data.source.PhotoListRepository
import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    private val photoListRepository: PhotoListRepository
) : ViewModel() {

    private val _mainUiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)
    val mainUiState: StateFlow<MainUiState> get() = _mainUiState

    private val viewModelScope: CoroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _mainUiState.value = MainUiState.Error(throwable)
    }

    init {
        refresh(isForceUpdate = true)
    }

    fun refresh(isForceUpdate: Boolean = false) {
        viewModelScope.launch(exceptionHandler) {
            _mainUiState.value = MainUiState.Loading

            photoListRepository
                .getAllPhotoList(isForceUpdate)
                .distinctUntilChanged()
                .collect { _mainUiState.value = MainUiState.Success(it) }
        }
    }

    fun loadMore() {
        viewModelScope.launch(exceptionHandler) {

        }
    }

    override fun onCleared() {
        viewModelScope.coroutineContext.cancel()
        super.onCleared()
    }

    sealed class MainUiState {
        class Success(val photoList: List<Photo> = emptyList()) : MainUiState()
        object Loading : MainUiState()
        object LoadingMore: MainUiState()
        class Error(val exception: Throwable) : MainUiState()
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