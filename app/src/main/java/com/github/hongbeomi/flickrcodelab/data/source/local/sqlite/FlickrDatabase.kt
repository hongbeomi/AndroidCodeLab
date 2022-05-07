package com.github.hongbeomi.flickrcodelab.data.source.local.sqlite

import android.content.ContentValues
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrContract.FlickrEntry.COLUMN_NAME_FARM
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrContract.FlickrEntry.COLUMN_NAME_PHOTO_ID
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrContract.FlickrEntry.COLUMN_NAME_SECRET
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrContract.FlickrEntry.COLUMN_NAME_SERVER
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrContract.FlickrEntry.TABLE_NAME
import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FlickrDatabase(
    flickrSqliteHelper: FlickrSqliteHelper
) {

    private val writeDb = flickrSqliteHelper.writableDatabase
    private val readDb = flickrSqliteHelper.readableDatabase

    private val _itemList: MutableStateFlow<List<Photo>> = MutableStateFlow(listOf())
    private val itemList: StateFlow<List<Photo>> get() = _itemList

    fun getAll(): Flow<List<Photo>> = itemList

    fun deleteAll() {
        writeDb.delete(TABLE_NAME, null, null)
        updateItemList()
    }

    fun insertAll(newPhotoList: List<Photo>) {
        newPhotoList.forEach {
            val contentValues = it.toContentValues()
            val result = writeDb.insert(
                TABLE_NAME,
                null,
                contentValues
            )
            if (result == -1L) {
                throw IllegalStateException("Failed photo insert to database")
            }
        }
        updateItemList()
    }

    fun update(newPhotoList: List<Photo>) {
        val where = "$COLUMN_NAME_PHOTO_ID ?"

        newPhotoList.forEach {
            val content = it.toContentValues()
            writeDb.update(
                TABLE_NAME,
                content,
                where,
                arrayOf(it.id)
            )
        }
        updateItemList()
    }

    private fun updateItemList() {
        val projection = arrayOf(
            COLUMN_NAME_PHOTO_ID,
            COLUMN_NAME_SECRET,
            COLUMN_NAME_SERVER,
            COLUMN_NAME_FARM
        )
        val items = mutableListOf<Photo>()

        try {
            val cursor = readDb.query(TABLE_NAME, projection, null, emptyArray(), null, null, null)
            with(cursor) {
                while (moveToNext()) {
                    val photoId = getString(getColumnIndexOrThrow(COLUMN_NAME_PHOTO_ID))
                    val secret = getString(getColumnIndexOrThrow(COLUMN_NAME_SECRET))
                    val server = getInt(getColumnIndexOrThrow(COLUMN_NAME_SERVER))
                    val farm = getInt(getColumnIndexOrThrow(COLUMN_NAME_FARM))

                    items.add(
                        Photo(id = photoId, secret = secret, server = server, farm = farm)
                    )
                }
            }
            cursor.close()
        } catch (e: Exception) {
            println(e)
        }
        _itemList.value = items
    }

    fun close() {
        writeDb.close()
        readDb.close()
    }

    private fun Photo.toContentValues(): ContentValues {
        return ContentValues().apply {
            put(COLUMN_NAME_PHOTO_ID, id)
            put(COLUMN_NAME_SECRET, secret)
            put(COLUMN_NAME_SERVER, server)
            put(COLUMN_NAME_FARM, farm)
        }
    }

}