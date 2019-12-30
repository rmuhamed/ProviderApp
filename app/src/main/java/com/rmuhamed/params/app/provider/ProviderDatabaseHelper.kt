package com.rmuhamed.params.app.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rmuhamed.params.providerlibrary.ParamContract

internal class ProviderDatabaseHelper(
    context: Context?,
    name: String?,
    tableName: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    private var cachedDBInstance: SQLiteDatabase? = null

    private val createStatement =
        "CREATE TABLE $tableName (" +
                "${ParamContract.ID_PARAM_KEY} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${ParamContract.NAME_PARAM_KEY} TEXT NOT NULL, " +
                "${ParamContract.FORMAT_PARAM_KEY} TEXT NOT NULL" +
                ")"

    private val dropStatement = "DROP TABLE IF EXISTS $tableName"

    override fun onCreate(db: SQLiteDatabase?) {
       db?.execSQL(createStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(dropStatement)
        onCreate(db)
    }

    fun getDatabase(): SQLiteDatabase? {
        if (cachedDBInstance == null) {
            cachedDBInstance = writableDatabase
        }

        return cachedDBInstance
    }
}