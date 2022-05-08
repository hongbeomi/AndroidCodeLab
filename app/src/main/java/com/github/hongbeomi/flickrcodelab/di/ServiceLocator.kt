package com.github.hongbeomi.flickrcodelab.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.github.hongbeomi.flickrcodelab.data.source.DefaultPhotoListRepository
import com.github.hongbeomi.flickrcodelab.data.source.PhotoListRepository
import com.github.hongbeomi.flickrcodelab.data.source.local.PhotosLocalDataSource
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrDatabase
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrSqliteHelper
import com.github.hongbeomi.flickrcodelab.data.source.remote.PhotosRemoteDataSource
import com.github.hongbeomi.flickrcodelab.data.source.remote.connection.FlickrNetworkService

object ServiceLocator {

    private var database: FlickrDatabase? = null
    private var network: FlickrNetworkService? = null

    @Volatile
    var photoListRepository: PhotoListRepository? = null
        @VisibleForTesting set

    private val lock = Any()

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            database?.apply {
                deleteAll()
                close()
            }
            database = null
            network = null
            photoListRepository = null
        }
    }

    fun providePhotosRepository(context: Context): PhotoListRepository {
        synchronized(this) {
            return photoListRepository ?: createPhotoListRepository(context)
        }
    }

    private fun createPhotoListRepository(context: Context): PhotoListRepository {
        val newRepo = DefaultPhotoListRepository(
            localPhotosDataSource = createPhotosLocalDataSource(context),
            remotePhotosDataSource = createPhotosRemoteDataSource(context)
        )
        photoListRepository = newRepo
        return newRepo
    }

    private fun createPhotosRemoteDataSource(context: Context): PhotosRemoteDataSource {
        val result = network ?: createNetworkService(context)
        return PhotosRemoteDataSource(result)
    }

    private fun createNetworkService(context: Context) : FlickrNetworkService {
        val result = FlickrNetworkService(context)
        network = result
        return result
    }

    private fun createPhotosLocalDataSource(context: Context): PhotosLocalDataSource {
        val dao = database ?: createDatabase(context)
        return PhotosLocalDataSource(dao)
    }

    private fun createDatabase(context: Context): FlickrDatabase {
        val result = FlickrDatabase(
            FlickrSqliteHelper(context)
        )
        database = result
        return result
    }

}