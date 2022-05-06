package com.github.hongbeomi.flickrcodelab.data.remote.response

data class PhotosResponse(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photos: List<PhotoResponse>
)
