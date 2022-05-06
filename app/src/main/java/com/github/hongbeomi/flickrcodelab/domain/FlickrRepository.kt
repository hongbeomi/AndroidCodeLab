package com.github.hongbeomi.flickrcodelab.domain

interface FlickrRepository {
    suspend fun getRecentPhotos()
}