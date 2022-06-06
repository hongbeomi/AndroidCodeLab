package com.github.hongbeomi.shared.repository

import com.github.hongbeomi.domain.EXCEPTION_MESSAGE_LIST_EMPTY
import com.github.hongbeomi.domain.Photo
import com.github.hongbeomi.domain.PhotoListRepository
import com.github.hongbeomi.shared.wrapEspressoIdlingResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class FakePhotoListRepository : PhotoListRepository {

    // network
    private var photoListServiceData: MutableList<Photo> = mutableListOf()

    // local
    private val photoListLocalFlow: MutableStateFlow<List<Photo>> = MutableStateFlow(emptyList())

    private var shouldReturnError = false
    private var isUseIdlingResource = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun setUseIdlingResource(value: Boolean) {
        isUseIdlingResource = value
    }

    override suspend fun refreshPhotoList() {
        wrapEspressoIdlingResource(isUseIdlingResource) {
            if (shouldReturnError) {
                throw Exception("Test Exception")
            }
            val newPhotoListFlow = flow {
                val photoList = photoListServiceData
                delay(500)
                emit(photoList)
            }

            newPhotoListFlow.collect {
                if (it.isEmpty()) {
                    throw IllegalStateException(EXCEPTION_MESSAGE_LIST_EMPTY)
                }
                photoListLocalFlow.value = it
            }
        }
    }

    override suspend fun loadMorePhotoList(page: Int): Boolean {
        wrapEspressoIdlingResource(isUseIdlingResource) {
            photoListLocalFlow.update {
                it + photoListServiceData
            }
            return true
        }
    }

    override suspend fun getAllPhotoList(isForceUpdate: Boolean): Flow<List<Photo>> {
        wrapEspressoIdlingResource(isUseIdlingResource) {
            if (isForceUpdate) {
                refreshPhotoList()
            }
            return photoListLocalFlow
        }
    }

    fun addPhotoList(vararg photo: Photo) {
        photoListServiceData.addAll(photo)
    }

}