package com.github.hongbeomi.flickrcodelab.model

data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photoList: List<Photo>
)