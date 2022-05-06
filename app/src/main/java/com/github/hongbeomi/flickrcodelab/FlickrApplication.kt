package com.github.hongbeomi.flickrcodelab

import android.app.Application
import com.github.hongbeomi.flickrcodelab.data.source.PhotosRepository
import com.github.hongbeomi.flickrcodelab.di.ServiceLocator

class FlickrApplication: Application() {

    val photosRepository: PhotosRepository
        get() = ServiceLocator.providePhotosRepository(this)

    override fun onCreate() {
        super.onCreate()
    }

}