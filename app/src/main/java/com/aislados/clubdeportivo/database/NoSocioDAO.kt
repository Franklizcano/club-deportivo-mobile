package com.aislados.clubdeportivo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aislados.clubdeportivo.model.NoSocio

@Dao
interface NoSocioDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createNoSocio(noSocio: NoSocio): Long

    @Query("SELECT COUNT(*) > 0 FROM nosocios WHERE dni = :dni LIMIT 1")
    fun existsNoSocio(dni: Int): Boolean

    @Query("SELECT * FROM nosocios WHERE dni = :dni LIMIT 1")
    fun findNoSocioByDni(dni: Int): NoSocio?

    @Query("SELECT * FROM nosocios")
    fun getAllNoSocios(): List<NoSocio>

    @Query("DELETE FROM nosocios WHERE dni = :dni")
    fun deleteNoSocioByDni(dni: Int): Int
}