package com.aislados.clubdeportivo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aislados.clubdeportivo.model.User

@Dao
interface UserDAO {
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    fun findUser(username: String): User?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    fun findUser(username: String, password: String): User?

    fun existsUser(username: String): Boolean {
        return findUser(username) != null
    }

    fun existsUser(username: String, password: String): Boolean {
        return findUser(username, password) != null
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(user: User)

    @Query("DELETE FROM users WHERE username = :username")
    fun deleteUser(username: String)
}
