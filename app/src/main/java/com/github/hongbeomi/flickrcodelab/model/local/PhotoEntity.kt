package com.github.hongbeomi.flickrcodelab.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.hongbeomi.flickrcodelab.model.Photo

@Entity
data class PhotoEntity(
    @PrimaryKey val uid: Int? = null,
    @ColumnInfo val id: String,
    @ColumnInfo val farm: Int,
    @ColumnInfo val secret: String,
    @ColumnInfo val server: Int
)

fun PhotoEntity.toDomain(): Photo {
    return Photo(
        id = id,
        farm = farm,
        secret = secret,
        server = server
    )
}

fun Photo.toEntity(): PhotoEntity {
    return PhotoEntity(
        id = id,
        farm = farm,
        secret = secret,
        server = server
    )
}