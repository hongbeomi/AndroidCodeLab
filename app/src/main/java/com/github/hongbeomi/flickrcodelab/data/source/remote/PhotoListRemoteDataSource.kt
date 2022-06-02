package com.github.hongbeomi.flickrcodelab.data.source.remote

import com.github.hongbeomi.flickrcodelab.data.source.PhotoListDataSource
import com.github.hongbeomi.flickrcodelab.data.source.remote.connection.FlickrNetworkService
import com.github.hongbeomi.domain.Photo
import com.github.hongbeomi.flickrcodelab.model.remote.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal const val EXCEPTION_MESSAGE_LIST_EMPTY = "Photo List is Empty!"

class PhotoListRemoteDataSource(
    private val networkService: FlickrNetworkService
) : PhotoListDataSource {

    override suspend fun getSearchPhotoList(page: Int): Flow<List<com.github.hongbeomi.domain.Photo>> {
        val photoList = networkService.getSoccerPhotos(page).photo
        return flow {
            emit(photoList.map { it.toDomain() })
        }
    }

    override suspend fun deleteAllPhotoList() {
        // no-op
    }

    override suspend fun insertPhotoList(value: List<com.github.hongbeomi.domain.Photo>) {
        // no-op
    }

}