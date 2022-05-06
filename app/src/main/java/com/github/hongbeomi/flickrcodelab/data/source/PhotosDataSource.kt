package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.flickrcodelab.model.Photo

interface PhotosDataSource {
    suspend fun getRecentPhotoList(): Result<List<Photo>>
    suspend fun deleteAllPhotoList()
    suspend fun savePhotoList(value: List<Photo>)
}