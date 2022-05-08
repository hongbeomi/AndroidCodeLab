package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.flickrcodelab.data.source.remote.EXCEPTION_MESSAGE_LIST_EMPTY
import com.github.hongbeomi.flickrcodelab.model.Photo
import com.github.hongbeomi.flickrcodelab.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class FakePhotoListRepository : PhotoListRepository {

    // network
    private var photosServiceData: MutableList<Photo> = mutableListOf()

    // local
    private val photosLocalFlow: MutableStateFlow<List<Photo>> = MutableStateFlow(emptyList())

    private var shouldReturnError = false
    private var isUseIdlingResource = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun setUseIdlingResource(value: Boolean) {
        isUseIdlingResource = value
    }

    override suspend fun refreshPhotoList() {
        if (shouldReturnError) {
            throw Exception("Test Exception")
        }
        val newPhotoListFlow = flow {
            val photoList = photosServiceData
            emit(photoList)
        }

        newPhotoListFlow.collect {
            delay(500)
            if (it.isEmpty()) {
                throw IllegalStateException(EXCEPTION_MESSAGE_LIST_EMPTY)
            }
            photosLocalFlow.value = it
        }
    }

    override suspend fun loadMorePhotoList(page: Int): Boolean {
        photosLocalFlow.update {
            it + photosServiceData
        }
        return true
    }

    override suspend fun getAllPhotoList(isForceUpdate: Boolean): Flow<List<Photo>> {
        wrapEspressoIdlingResource(isUseIdlingResource) {
            if (isForceUpdate) {
                refreshPhotoList()
            }
            return photosLocalFlow
        }
    }

    fun addPhotoList(vararg photo: Photo) {
        photosServiceData.addAll(photo)
    }

}