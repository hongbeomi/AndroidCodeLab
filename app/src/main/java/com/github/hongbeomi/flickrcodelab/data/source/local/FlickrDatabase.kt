package com.github.hongbeomi.flickrcodelab.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.hongbeomi.flickrcodelab.model.local.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1, exportSchema = false)
abstract class FlickrDatabase : RoomDatabase() {
    abstract fun flickrDao(): FlickrDao
}