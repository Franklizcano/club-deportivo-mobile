package com.aislados.clubdeportivo.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.aislados.clubdeportivo.model.User
import com.aislados.clubdeportivo.model.UserRole

class UserDAO(private val context: Context) {

    private fun getWritableDatabase(): SQLiteDatabase = DatabaseHelper(context).writableDatabase

    private fun getReadableDatabase(): SQLiteDatabase = DatabaseHelper(context).readableDatabase

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

    private fun findUserByQuery(selection: String, selectionArgs: Array<String>): User? {
        return try {
            getReadableDatabase().use { db ->
                val query = "SELECT * FROM ${UserTableHelper.TABLE_NAME} WHERE $selection"
                db.rawQuery(query, selectionArgs).use { cursor ->
                    if (cursor.moveToFirst()) {
                        val roleString = cursor.getString(cursor.getColumnIndexOrThrow(UserTableHelper.COLUMN_ROLE))
                        User(
                            username = cursor.getString(cursor.getColumnIndexOrThrow(UserTableHelper.COLUMN_USERNAME)),
                            password = cursor.getString(cursor.getColumnIndexOrThrow(UserTableHelper.COLUMN_PASSWORD)),
                            role = UserRole.valueOf(roleString)
                        )
                    } else null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun findUser(username: String): User? {
        val selection = "${UserTableHelper.COLUMN_USERNAME} = ?"
        return findUserByQuery(selection, arrayOf(username))
    }

    fun findUser(username: String, password: String): User? {
        val selection = "${UserTableHelper.COLUMN_USERNAME} = ? AND ${UserTableHelper.COLUMN_PASSWORD} = ?"
        return findUserByQuery(selection, arrayOf(username, password))
    }

    fun existsUser(username: String, password: String): Boolean {
        return findUser(username, password) != null
    }

    fun existsUser(username: String): Boolean {
        return findUser(username) != null
    }
}
