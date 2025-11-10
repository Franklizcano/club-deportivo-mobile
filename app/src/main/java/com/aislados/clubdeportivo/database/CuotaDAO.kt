package com.aislados.clubdeportivo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aislados.clubdeportivo.model.Cuota
import java.time.LocalDate

@Dao
interface CuotaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createCuota(cuota: Cuota): Long

    @Query("SELECT * FROM cuotas WHERE socioId = :socioId ORDER BY id DESC LIMIT 1")
    fun findCuotaBySocioIdOrderByIdDesc(socioId: Long): Cuota?

    @Query("SELECT * FROM cuotas WHERE socioId = :socioId ORDER BY id DESC")
    fun findAllCuotasBySocioId(socioId: Long): List<Cuota>

    @Query("""
    SELECT c.* FROM cuotas c
    INNER JOIN (
        SELECT socioId, MAX(fechaVencimiento) as maxFecha
        FROM cuotas
        WHERE fechaVencimiento < :fechaActual
        GROUP BY socioId
    ) latest ON c.socioId = latest.socioId AND c.fechaVencimiento = latest.maxFecha
    """)
    fun getCuotasBySocioIdLastExpired(fechaActual: LocalDate): List<Cuota>

    @Query("DELETE FROM cuotas WHERE id = :id")
    fun deleteCuotaById(id: Long): Int

    @Query("DELETE FROM cuotas WHERE socioId = :socioId")
    fun deleteCuotasBySocioId(socioId: Long): Int
}