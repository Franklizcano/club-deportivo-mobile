package com.aislados.clubdeportivo.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.aislados.clubdeportivo.model.User
import com.aislados.clubdeportivo.model.UserRole

class UserDAO(private val context: Context) {

    private fun getWritableDatabase(): SQLiteDatabase {
        return DatabaseHelper(context).writableDatabase
    }

    private fun getReadableDatabase(): SQLiteDatabase {
        return DatabaseHelper(context).readableDatabase
    }

    fun createUser(user: User): Boolean {
        var db: SQLiteDatabase? = null
        return try {
            db = getWritableDatabase()
            val contentValues = ContentValues().apply {
                put(UserTableHelper.COLUMN_USERNAME, user.username)
                put(UserTableHelper.COLUMN_PASSWORD, user.password)
                put(UserTableHelper.COLUMN_ROLE, user.role.name)
            }
            val result = db.insert(UserTableHelper.TABLE_NAME, null, contentValues)
            result != -1L
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db?.close()
        }
    }

    //TODO: modificar el codigo para tambien validar usuario y contraseña en el login
    fun findUser(username: String): User? {
        var db: SQLiteDatabase? = null
        var cursor: android.database.Cursor? = null

        return try {
            db = getReadableDatabase()
            val query = "SELECT * FROM ${UserTableHelper.TABLE_NAME} WHERE ${UserTableHelper.COLUMN_USERNAME} = ?"
            cursor = db.rawQuery(query, arrayOf(username))

            if (cursor.moveToFirst()) {
                val roleString = cursor.getString(cursor.getColumnIndexOrThrow(UserTableHelper.COLUMN_ROLE))
                User(
                    username = cursor.getString(cursor.getColumnIndexOrThrow(UserTableHelper.COLUMN_USERNAME)),
                    password = cursor.getString(cursor.getColumnIndexOrThrow(UserTableHelper.COLUMN_PASSWORD)),
                    role = UserRole.valueOf(roleString)
                )
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            cursor?.close()
            db?.close()
        }
    }

    fun existsUser(username: String): Boolean {
        return findUser(username) != null
    }
}
