package com.github.hongbeomi.flickrcodelab.data.source.remote

import com.github.hongbeomi.flickrcodelab.data.source.PhotosDataSource
import com.github.hongbeomi.flickrcodelab.model.Photo

class PhotosRemoteDataSource: PhotosDataSource {

    override suspend fun getRecentPhotoList(): Result<List<Photo>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllPhotoList() {
        // no-op
    }

    override suspend fun savePhotoList(value: List<Photo>) {
        // no-op
    }

}