package com.github.hongbeomi.flickrcodelab.data.repository

import com.github.hongbeomi.flickrcodelab.domain.model.Photo
import com.github.hongbeomi.flickrcodelab.domain.repository.FlickrRepository

class DefaultFlickrRepository: FlickrRepository {

    override suspend fun getRecentPhotos(): Result<List<Photo>> {
        TODO("Not yet implemented")
    }

}