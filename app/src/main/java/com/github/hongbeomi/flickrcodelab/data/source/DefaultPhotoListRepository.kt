package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.flickrcodelab.data.source.remote.EXCEPTION_MESSAGE_LIST_EMPTY
import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext

class DefaultPhotoListRepository(
    private val localPhotosDataSource: PhotosDataSource,
    private val remotePhotosDataSource: PhotosDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PhotoListRepository {

    override suspend fun getAllPhotoList(isForceUpdate: Boolean): Flow<List<Photo>> =
        withContext(dispatcher) {
            if (isForceUpdate) {
                refreshPhotoList()
            }
            localPhotosDataSource.getSearchPhotoList()
        }

    override suspend fun refreshPhotoList() = withContext(dispatcher) {
        val newPhotoList = remotePhotosDataSource.getSearchPhotoList()

        if (newPhotoList.single().isEmpty()) {
            throw IllegalStateException(EXCEPTION_MESSAGE_LIST_EMPTY)
        }

        newPhotoList.collect {
            localPhotosDataSource.deleteAllPhotoList()
            localPhotosDataSource.insertPhotoList(it)
        }
    }

    override suspend fun loadMorePhotoList(page: Int): Boolean = withContext(dispatcher) {
        val newPhotoList = remotePhotosDataSource.getSearchPhotoList(page)

        return@withContext if (newPhotoList.single().isNotEmpty()) {
            newPhotoList.collect { localPhotosDataSource.insertPhotoList(it) }
            true
        } else {
            false
        }
    }

}