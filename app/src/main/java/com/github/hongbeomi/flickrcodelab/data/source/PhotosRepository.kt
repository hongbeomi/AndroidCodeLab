package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.flickrcodelab.model.Photo

interface PhotosRepository {
    suspend fun refreshPhotos()
    suspend fun getAllPhotos(isForceUpdate: Boolean = false): Result<List<Photo>>
}