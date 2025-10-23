package com.aislados.clubdeportivo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aislados.clubdeportivo.model.Socio

@Dao
interface SocioDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createSocio(socio: Socio): Long
    @Query("SELECT COUNT(*) > 0 FROM socios WHERE dni = :dni LIMIT 1")
    fun existsSocio(dni: Int): Boolean

    @Query("SELECT * FROM socios WHERE dni = :dni LIMIT 1")
    fun findSocioByDni(dni: Int): Socio?

    @Query("SELECT * FROM socios")
    fun getAllSocios(): List<Socio>

    @Query("DELETE FROM socios WHERE dni = :dni")
    fun deleteSocioByDni(dni: Int): Int
}