package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotosDataSource {
    suspend fun getSearchPhotoList(page: Int = 1): Flow<List<Photo>>
    suspend fun deleteAllPhotoList()
    suspend fun insertPhotoList(value: List<Photo>)
}