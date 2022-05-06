package com.github.hongbeomi.flickrcodelab.data.source.local.sqlite

import android.provider.BaseColumns

object FlickrContract {
    object FlickrEntry: BaseColumns {
        const val TABLE_NAME = "flickr_entry"
        const val COLUMN_NAME_PHOTO_ID = "photo_id"
        const val COLUMN_NAME_SECRET = "secret"
        const val COLUMN_NAME_SERVER = "server"
    }
}