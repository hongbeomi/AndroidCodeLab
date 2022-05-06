package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotosDataSource {
    suspend fun getRecentPhotoList(): Flow<List<Photo>>
    suspend fun deleteAllPhotoList()
    suspend fun savePhotoList(value: List<Photo>)
}