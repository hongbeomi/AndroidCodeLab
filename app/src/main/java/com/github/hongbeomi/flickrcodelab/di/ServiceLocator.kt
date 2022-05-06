package com.github.hongbeomi.flickrcodelab.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.github.hongbeomi.flickrcodelab.data.source.DefaultPhotosRepository
import com.github.hongbeomi.flickrcodelab.data.source.PhotosRepository
import com.github.hongbeomi.flickrcodelab.data.source.local.PhotosLocalDataSource
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrDao
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrSqliteHelper
import com.github.hongbeomi.flickrcodelab.data.source.remote.PhotosRemoteDataSource
import com.github.hongbeomi.flickrcodelab.data.source.remote.connection.FlickrNetworkService

object ServiceLocator {

    private var database: FlickrDao? = null
    private var network: FlickrNetworkService? = null

    @Volatile
    var photosRepository: PhotosRepository? = null
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
            photosRepository = null
        }
    }

    fun providePhotosRepository(context: Context): PhotosRepository {
        synchronized(this) {
            return photosRepository ?: createPhotosRepository(context)
        }
    }

    private fun createPhotosRepository(context: Context): PhotosRepository {
        val newRepo = DefaultPhotosRepository(
            localPhotosDataSource = createPhotosLocalDataSource(context),
            remotePhotosDataSource = createPhotosRemoteDataSource(context)
        )
        photosRepository = newRepo
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
        val dao = database ?: createDao(context)
        return PhotosLocalDataSource(dao)
    }

    private fun createDao(context: Context): FlickrDao {
        val result = FlickrDao(
            FlickrSqliteHelper(context)
        )
        database = result
        return result
    }

}