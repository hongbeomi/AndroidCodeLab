package com.github.hongbeomi.flickrcodelab.data.source.remote

import com.github.hongbeomi.flickrcodelab.data.source.PhotosDataSource
import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePhotosDataSource(
    var photoList: MutableList<Photo> = mutableListOf()
) : PhotosDataSource {

    override suspend fun getSearchPhotoList(page: Int): Flow<List<Photo>> {
        return flowOf(photoList)
    }

    override suspend fun deleteAllPhotoList() {
        photoList.clear()
    }

    override suspend fun insertPhotoList(value: List<Photo>) {
        photoList.addAll(value)
    }

}