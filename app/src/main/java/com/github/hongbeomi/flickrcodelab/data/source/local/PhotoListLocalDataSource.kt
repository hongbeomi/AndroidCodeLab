package com.github.hongbeomi.flickrcodelab.data.source.local

import com.github.hongbeomi.flickrcodelab.data.source.PhotoListDataSource
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrDatabase
import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.flow.Flow

class PhotoListLocalDataSource(
    private val flickrDatabase: FlickrDatabase
): PhotoListDataSource {

    override suspend fun getSearchPhotoList(page: Int): Flow<List<Photo>> = flickrDatabase.getAll()

    override suspend fun deleteAllPhotoList() {
        flickrDatabase.deleteAll()
    }

    override suspend fun insertPhotoList(value: List<Photo>) {
        flickrDatabase.insertAll(value)
    }

}