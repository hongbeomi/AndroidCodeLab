package com.github.hongbeomi.flickrcodelab.data.source.remote

import com.github.hongbeomi.flickrcodelab.data.source.PhotosDataSource
import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.flow.Flow

class PhotosRemoteDataSource: PhotosDataSource {

    override suspend fun getRecentPhotoList(): Flow<List<Photo>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllPhotoList() {
        // no-op
    }

    override suspend fun savePhotoList(value: List<Photo>) {
        // no-op
    }

}