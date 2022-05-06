package com.github.hongbeomi.flickrcodelab.data.source.local

import com.github.hongbeomi.flickrcodelab.data.source.PhotosDataSource
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrDao
import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.flow.Flow

class PhotosLocalDataSource(
    private val flickrDao: FlickrDao
): PhotosDataSource {

    override suspend fun getRecentPhotoList(): Flow<List<Photo>> = flickrDao.getAll()

    override suspend fun deleteAllPhotoList() {
        flickrDao.deleteAll()
    }

    override suspend fun savePhotoList(value: List<Photo>) {
        flickrDao.insertAll(value)
    }

}