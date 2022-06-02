package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.domain.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoListDataSource {
    suspend fun getSearchPhotoList(page: Int = 1): Flow<List<com.github.hongbeomi.domain.Photo>>
    suspend fun deleteAllPhotoList()
    suspend fun insertPhotoList(value: List<com.github.hongbeomi.domain.Photo>)
}