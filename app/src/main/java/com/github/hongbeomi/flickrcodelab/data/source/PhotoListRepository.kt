package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoListRepository {
    suspend fun getAllPhotoList(isForceUpdate: Boolean = false): Flow<List<Photo>>
    suspend fun refreshPhotoList()
    suspend fun loadMorePhotoList(page: Int): Boolean
}