package com.github.hongbeomi.flickrcodelab.data.source.remote

import com.github.hongbeomi.flickrcodelab.data.source.PhotosDataSource
import com.github.hongbeomi.flickrcodelab.model.Photo

class FakePhotosDataSource(
    var photoList: MutableList<Photo> = mutableListOf()
) : PhotosDataSource {

    override suspend fun getRecentPhotoList(): Result<List<Photo>> {
        return Result.success(photoList)
    }

    override suspend fun deleteAllPhotoList() {
        photoList.clear()
    }

    override suspend fun savePhotoList(value: List<Photo>) {
        photoList.addAll(value)
    }

}