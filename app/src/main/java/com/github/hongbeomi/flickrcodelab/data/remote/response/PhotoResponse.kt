package com.github.hongbeomi.flickrcodelab.data.remote.response


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

