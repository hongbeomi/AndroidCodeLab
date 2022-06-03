package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.domain.EXCEPTION_MESSAGE_LIST_EMPTY
import com.github.hongbeomi.domain.PhotoListRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext

class DefaultPhotoListRepository(
    private val localPhotoListDataSource: PhotoListDataSource,
    private val remotePhotoListDataSource: PhotoListDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PhotoListRepository {

    override suspend fun getAllPhotoList(isForceUpdate: Boolean): Flow<List<com.github.hongbeomi.domain.Photo>> =
        withContext(dispatcher) {
            if (isForceUpdate) {
                refreshPhotoList()
            }
            localPhotoListDataSource.getSearchPhotoList()
        }

    override suspend fun refreshPhotoList() = withContext(dispatcher) {
        val newPhotoList = remotePhotoListDataSource.getSearchPhotoList()

        if (newPhotoList.single().isEmpty()) {
            throw IllegalStateException(EXCEPTION_MESSAGE_LIST_EMPTY)
        }

        newPhotoList.collect {
            localPhotoListDataSource.deleteAllPhotoList()
            localPhotoListDataSource.insertPhotoList(it)
        }
    }

    override suspend fun loadMorePhotoList(page: Int): Boolean = withContext(dispatcher) {
        val newPhotoList = remotePhotoListDataSource.getSearchPhotoList(page)

        return@withContext if (newPhotoList.single().isNotEmpty()) {
            newPhotoList.collect { localPhotoListDataSource.insertPhotoList(it) }
            true
        } else {
            false
        }
    }

}