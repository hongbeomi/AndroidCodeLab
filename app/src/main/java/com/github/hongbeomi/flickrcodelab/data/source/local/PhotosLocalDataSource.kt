package com.github.hongbeomi.flickrcodelab.data.source.local

import com.github.hongbeomi.flickrcodelab.data.source.PhotosDataSource
import com.github.hongbeomi.flickrcodelab.model.Photo

class PhotosLocalDataSource: PhotosDataSource {

    override suspend fun getRecentPhotoList(): Result<List<Photo>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllPhotoList() {
        TODO("Not yet implemented")
    }

    override suspend fun savePhotoList(value: List<Photo>) {
        TODO("Not yet implemented")
    }

}