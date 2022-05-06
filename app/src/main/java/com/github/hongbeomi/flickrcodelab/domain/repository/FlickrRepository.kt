package com.github.hongbeomi.flickrcodelab.domain.repository

import com.github.hongbeomi.flickrcodelab.domain.model.Photo

interface FlickrRepository {
    suspend fun getRecentPhotos(): Result<List<Photo>>
}