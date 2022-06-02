package com.github.hongbeomi.flickrcodelab.model.remote

import com.github.hongbeomi.domain.Photo
import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(
    val id: String,
    val owner: String,
    val secret: String,
    val server: Int,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int
)

fun PhotoResponse.toDomain(): Photo {
    return Photo(
        id = id,
        farm = farm,
        secret = secret,
        server = server
    )
}
