package com.github.hongbeomi.flickrcodelab.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class PhotosResponse(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<PhotoResponse>
)