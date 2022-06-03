package com.github.hongbeomi.domain

import com.github.hongbeomi.domain.Photo
import kotlinx.coroutines.flow.Flow

const val EXCEPTION_MESSAGE_LIST_EMPTY = "Photo List is Empty!"

interface PhotoListRepository {
    suspend fun getAllPhotoList(isForceUpdate: Boolean = false): Flow<List<Photo>>
    suspend fun refreshPhotoList()
    suspend fun loadMorePhotoList(page: Int): Boolean
}