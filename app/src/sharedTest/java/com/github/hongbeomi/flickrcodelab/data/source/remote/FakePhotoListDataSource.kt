package com.github.hongbeomi.flickrcodelab.data.source.remote

import com.github.hongbeomi.flickrcodelab.data.source.PhotoListDataSource
import com.github.hongbeomi.domain.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePhotoListDataSource(
    private var photoList: MutableList<Photo> = mutableListOf()
) : PhotoListDataSource {

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