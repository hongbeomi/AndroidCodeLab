package com.github.hongbeomi.flickrcodelab.data.source.local.sqlite

import android.content.ContentValues
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrContract.FlickrEntry.COLUMN_NAME_PHOTO_ID
import com.github.hongbeomi.flickrcodelab.data.source.local.sqlite.FlickrContract.FlickrEntry.TABLE_NAME
import com.github.hongbeomi.flickrcodelab.model.Photo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class FilckrDao(
    flickrSqliteHelper: FlickrSqliteHelper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val writeDb = flickrSqliteHelper.writableDatabase
    private val readDb = flickrSqliteHelper.readableDatabase

    private val _itemList: MutableStateFlow<List<Photo>> = MutableStateFlow(listOf())
    val itemList: StateFlow<List<Photo>> get() = _itemList

    fun getAll(): Flow<List<Photo>> = itemList

    suspend fun deleteAll() = withContext(dispatcher) {
        writeDb.delete(TABLE_NAME, null, null)
        updateItemList()
    }

    suspend fun insertAll(newPhotoList: List<Photo>) = withContext(dispatcher) {
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

    suspend fun update(newPhotoList: List<Photo>) = withContext(dispatcher) {
        val where = "$COLUMN_NAME_PHOTO_ID ?"

        newPhotoList.forEach {
            val content = it.toContentValues()
            writeDb.update(
                TABLE_NAME,
                content,
                where,
                arrayOf(it.id.toString())
            )
        }
        updateItemList()
    }

    private suspend fun updateItemList() = withContext(dispatcher) {
        val projection = arrayOf(
            COLUMN_NAME_PHOTO_ID,
            FlickrContract.FlickrEntry.COLUMN_NAME_SECRET,
            FlickrContract.FlickrEntry.COLUMN_NAME_SERVER
        )
        val cursor = readDb.query(TABLE_NAME, projection, null, emptyArray(), null, null, null)

        val items = mutableListOf<Photo>()

        with(cursor) {
            while (moveToNext()) {
                val photoId = getLong(
                    getColumnIndexOrThrow(
                        COLUMN_NAME_PHOTO_ID
                    )
                )
                val secret =
                    getString(getColumnIndexOrThrow(FlickrContract.FlickrEntry.COLUMN_NAME_SECRET))
                val server =
                    getInt(getColumnIndexOrThrow(FlickrContract.FlickrEntry.COLUMN_NAME_SERVER))

                items.add(
                    Photo(id = photoId, secret = secret, server = server)
                )
            }
            close()
        }
        _itemList.value = items
    }

    private fun Photo.toContentValues(): ContentValues {
        return ContentValues().apply {
            put(COLUMN_NAME_PHOTO_ID, id)
            put(FlickrContract.FlickrEntry.COLUMN_NAME_SECRET, secret)
            put(FlickrContract.FlickrEntry.COLUMN_NAME_SERVER, server)
        }
    }

}