package com.github.hongbeomi.flickrcodelab.data.source

import com.github.hongbeomi.flickrcodelab.model.Photo

class FakePhotoDataFactory {

    val photo1 = Photo(id = "1", farm = 1, secret = "secret1", server = 1)
    val photo2 = Photo(id = "2", farm = 2, secret = "secret2", server = 2)
    val photo3 = Photo(id = "3", farm = 3, secret = "secret3", server = 2)

}