package com.github.hongbeomi.flickrcodelab.model.remote

data class PhotoResponse(
    val id: Long,
    val owner: String,
    val secret: String,
    val server: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int
)
