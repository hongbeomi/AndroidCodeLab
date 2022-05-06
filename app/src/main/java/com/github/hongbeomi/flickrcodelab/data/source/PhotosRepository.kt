package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {
    suspend fun refreshPhotos()
    suspend fun getAllPhotos(isForceUpdate: Boolean = false): Flow<List<Photo>>
}