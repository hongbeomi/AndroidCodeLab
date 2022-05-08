package com.github.hongbeomi.flickrcodelab.data.source.remote

import com.github.hongbeomi.flickrcodelab.data.source.PhotosDataSource
import com.github.hongbeomi.flickrcodelab.data.source.remote.connection.FlickrNetworkService
import com.github.hongbeomi.flickrcodelab.model.Photo
import com.github.hongbeomi.flickrcodelab.model.remote.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal const val EXCEPTION_MESSAGE_LIST_EMPTY = "Photo List is Empty!"

class PhotosRemoteDataSource(
    private val networkService: FlickrNetworkService
) : PhotosDataSource {

    override suspend fun getSearchPhotoList(page: Int): Flow<List<Photo>> {
        val photoList = networkService.getSoccerPhotos(page).photo
        if (photoList.isEmpty()) {
            throw IllegalStateException(EXCEPTION_MESSAGE_LIST_EMPTY)
        }
        return flow { emit(photoList.map { it.toDomain() }) }
    }

    override suspend fun deleteAllPhotoList() {
        // no-op
    }

    override suspend fun insertPhotoList(value: List<Photo>) {
        // no-op
    }

}