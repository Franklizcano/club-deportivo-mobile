package com.aislados.clubdeportivo.database

import android.database.sqlite.SQLiteDatabase

object UserTableHelper {
    const val TABLE_NAME = "users"
    const val COLUMN_ID = "id"
    const val COLUMN_USERNAME = "username"
    const val COLUMN_PASSWORD = "password"

    fun createTable(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME VARCHAR(100) NOT NULL,
                $COLUMN_PASSWORD VARCHAR(100) NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    fun dropTable(db: SQLiteDatabase?) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }
}
