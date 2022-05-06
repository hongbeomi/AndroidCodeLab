package com.github.hongbeomi.flickrcodelab.data.source.local.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${FlickrContract.FlickrEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FlickrContract.FlickrEntry.COLUMN_NAME_PHOTO_ID} LONG," +
            "${FlickrContract.FlickrEntry.COLUMN_NAME_SECRET} TEXT," +
            "${FlickrContract.FlickrEntry.COLUMN_NAME_SERVER} INTEGER)"

private const val SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS ${FlickrContract.FlickrEntry.TABLE_NAME}"

class FlickrSqliteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_NAME = "flickr"
        const val DATABASE_VERSION = 1
    }

}