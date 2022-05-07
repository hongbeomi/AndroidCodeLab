package com.github.hongbeomi.flickrcodelab.data.source.remote

import com.github.hongbeomi.flickrcodelab.data.source.PhotosDataSource
import com.github.hongbeomi.flickrcodelab.data.source.remote.connection.FlickrNetworkService
import com.github.hongbeomi.flickrcodelab.model.Photo
import com.github.hongbeomi.flickrcodelab.model.remote.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PhotosRemoteDataSource(
    private val networkService: FlickrNetworkService
): PhotosDataSource {

    override suspend fun getRecentPhotoList(): Flow<List<Photo>> {
        val photoList = networkService.getRecentPhotos().photo
        return flow { emit(photoList.map { it.toDomain() }) }
    }

    override suspend fun deleteAllPhotoList() {
        // no-op
    }

    override suspend fun savePhotoList(value: List<Photo>) {
        // no-op
    }

}