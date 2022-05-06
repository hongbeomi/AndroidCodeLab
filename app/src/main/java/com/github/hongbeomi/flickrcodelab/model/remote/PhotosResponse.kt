package com.github.hongbeomi.flickrcodelab.model.remote

data class PhotosResponse(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photoList: List<PhotoResponse>
)