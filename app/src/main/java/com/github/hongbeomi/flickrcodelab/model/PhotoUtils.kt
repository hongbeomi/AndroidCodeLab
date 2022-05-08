package com.github.hongbeomi.flickrcodelab.model

internal fun Photo.getImageUrl(): String {
    return "https://live.staticflickr.com/$server/${id}_$secret.jpg"
}