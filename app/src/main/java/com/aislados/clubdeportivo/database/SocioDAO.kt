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

    @Query("""
    SELECT CASE
        WHEN COUNT(*) > 0 AND MAX(fechaVencimiento) < DATE('now') THEN 1
        ELSE 0
    END
    FROM cuotas
    WHERE socioId = :socioId
    """)
    fun tieneCuotaVencida(socioId: Long): Boolean

    @Query("SELECT * FROM socios WHERE socioId = :socioId")
    fun findSocioBySocioId(socioId: Long): Socio?

    @Query("SELECT * FROM socios WHERE id = :id")
    fun findSocioById(id: Long): Socio?

    @Query("DELETE FROM socios WHERE dni = :dni")
    fun deleteSocioByDni(dni: Int): Int
}