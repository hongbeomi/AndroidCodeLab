package com.github.hongbeomi.flickrcodelab.data.source.local

import com.github.hongbeomi.flickrcodelab.data.source.PhotoListDataSource
import com.github.hongbeomi.flickrcodelab.data.source.local.room.FlickrDao
import com.github.hongbeomi.flickrcodelab.model.Photo
import com.github.hongbeomi.flickrcodelab.model.local.toDomain
import com.github.hongbeomi.flickrcodelab.model.local.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhotoListLocalDataSource(
    private val flickrDao: FlickrDao
) : PhotoListDataSource {

    override suspend fun getSearchPhotoList(page: Int): Flow<List<Photo>> =
        flickrDao
            .getAll()
            .map {
                it.map { item -> item.toDomain() }
            }

    override suspend fun deleteAllPhotoList() {
        flickrDao.deleteAll()
    }

    override suspend fun insertPhotoList(value: List<Photo>) {
        flickrDao.insertAll(value.map { it.toEntity() })
    }

}