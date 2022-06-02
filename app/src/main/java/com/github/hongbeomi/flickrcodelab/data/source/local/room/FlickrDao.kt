package com.github.hongbeomi.flickrcodelab.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.hongbeomi.flickrcodelab.model.local.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlickrDao {
    @Query("SELECT * FROM photoEntity")
    fun getAll(): Flow<List<PhotoEntity>>

    @Insert
    fun insertAll(newPhotoList: List<PhotoEntity>)

    @Query("DELETE FROM photoEntity")
    fun deleteAll()

}