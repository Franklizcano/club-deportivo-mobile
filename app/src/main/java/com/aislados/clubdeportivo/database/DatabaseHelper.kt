package com.aislados.clubdeportivo.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "club_deportivo.db"
        const val DATABASE_VERSION = 4
    }

    override fun onCreate(db: SQLiteDatabase?) {
        UserTableHelper.createTable(db)
        SocioTableHelper.createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        UserTableHelper.dropTable(db)
        SocioTableHelper.dropTable(db)
        onCreate(db)
    }
}
