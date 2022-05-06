package com.github.hongbeomi.flickrcodelab.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class FlickrResponse(
    val photos: PhotosResponse
)