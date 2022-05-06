package com.github.hongbeomi.flickrcodelab.domain.repository

interface FlickrRepository {
    suspend fun getRecentPhotos()
}