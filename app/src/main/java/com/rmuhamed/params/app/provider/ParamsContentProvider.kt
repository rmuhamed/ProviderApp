package com.rmuhamed.params.app.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

internal class ParamsContentProvider : ContentProvider() {
    private val codeTable = 1
    private val codeId = 2

    private lateinit var dbHelper: ProviderDatabaseHelper

    private val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(BuildConfig.APPLICATION_ID, BuildConfig.TABLE_NAME, codeTable)
        addURI(BuildConfig.APPLICATION_ID, "${BuildConfig.TABLE_NAME}/#", codeId)
    }

    override fun onCreate(): Boolean {
        dbHelper =
            ProviderDatabaseHelper(
                context,
                BuildConfig.DB_NAME,
                BuildConfig.TABLE_NAME,
                null,
                BuildConfig.DB_VERSION
            )

        return dbHelper.getDatabase() != null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = BuildConfig.TABLE_NAME

        return queryBuilder.query(
            dbHelper.getDatabase(),
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var rowId: Long
        values?.let {
            rowId = dbHelper.getDatabase()?.insert(BuildConfig.TABLE_NAME, null, values) ?: -1L

            if (rowId > 0) {
                val contentUri =
                    Uri.parse("content://${BuildConfig.APPLICATION_ID}/{${BuildConfig.TABLE_NAME}")
                return ContentUris.withAppendedId(contentUri, rowId)
            }
        }

        throw SQLException("Not possible to add a new record $uri")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int =
        dbHelper.getDatabase()?.update(BuildConfig.TABLE_NAME, values, selection, selectionArgs) ?: 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            codeTable -> "vnd.android.cursor.dir/vnd.${BuildConfig.TABLE_NAME}"
            codeId -> "vnd.android.cursor.item/vnd.${BuildConfig.TABLE_NAME}"
            else -> throw IllegalArgumentException("Unsupported URI $uri")
        }
    }
}