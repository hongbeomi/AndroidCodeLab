package com.github.hongbeomi.flickrcodelab

import android.app.Application
import com.github.hongbeomi.flickrcodelab.data.source.PhotoListRepository
import com.github.hongbeomi.flickrcodelab.di.ServiceLocator

class FlickrApplication: Application() {

    val photoListRepository: PhotoListRepository
        get() = ServiceLocator.providePhotosRepository(this)

}