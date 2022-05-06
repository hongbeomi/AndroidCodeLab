package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class DefaultPhotosRepository(
    private val localPhotosDataSource: PhotosDataSource,
    private val remotePhotosDataSource: PhotosDataSource
) : PhotosRepository {

    override suspend fun refreshPhotos() {
        val newPhotoList = remotePhotosDataSource.getRecentPhotoList()

        newPhotoList
            .catch { throw it }
            .collect {
                localPhotosDataSource.deleteAllPhotoList()
                localPhotosDataSource.savePhotoList(it)
            }
    }

    override suspend fun getAllPhotos(isForceUpdate: Boolean): Flow<List<Photo>> {
        if (isForceUpdate) {
            refreshPhotos()
        }
        return localPhotosDataSource.getRecentPhotoList()
    }

}