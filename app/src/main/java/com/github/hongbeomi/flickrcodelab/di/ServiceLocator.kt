package com.github.hongbeomi.flickrcodelab.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.github.hongbeomi.flickrcodelab.data.source.DefaultPhotoListRepository
import com.github.hongbeomi.domain.PhotoListRepository
import com.github.hongbeomi.flickrcodelab.data.source.local.room.FlickrDatabase
import com.github.hongbeomi.flickrcodelab.data.source.local.PhotoListLocalDataSource
import com.github.hongbeomi.flickrcodelab.data.source.remote.PhotoListRemoteDataSource
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
            database?.flickrDao()?.deleteAll()
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
            localPhotoListDataSource = createPhotosLocalDataSource(context),
            remotePhotoListDataSource = createPhotosRemoteDataSource(context)
        )
        photoListRepository = newRepo
        return newRepo
    }

    private fun createPhotosRemoteDataSource(context: Context): PhotoListRemoteDataSource {
        val result = network ?: createNetworkService(context)
        return PhotoListRemoteDataSource(result)
    }

    private fun createNetworkService(context: Context) : FlickrNetworkService {
        val result = FlickrNetworkService(context)
        network = result
        return result
    }

    private fun createPhotosLocalDataSource(context: Context): PhotoListLocalDataSource {
        val dao = database ?: createDatabase(context)
        return PhotoListLocalDataSource(dao.flickrDao())
    }

    private fun createDatabase(context: Context): FlickrDatabase {
        val result = Room.databaseBuilder(
            context,
            FlickrDatabase::class.java,
            "flickr-database"
        ).build()
        database = result
        return result
    }

}