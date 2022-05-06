package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.flickrcodelab.model.Photo

class DefaultPhotosRepository(
    private val localPhotosDataSource: PhotosDataSource,
    private val remotePhotosDataSource: PhotosDataSource
) : PhotosRepository {

    override suspend fun refreshPhotos() {
        val newPhotoList = remotePhotosDataSource.getRecentPhotoList()

        newPhotoList
            .onSuccess {
                localPhotosDataSource.deleteAllPhotoList()
                localPhotosDataSource.savePhotoList(it)
            }
            .onFailure { throw it }
    }

    override suspend fun getAllPhotos(isForceUpdate: Boolean): Result<List<Photo>> {
        return localPhotosDataSource.getRecentPhotoList()
    }

}