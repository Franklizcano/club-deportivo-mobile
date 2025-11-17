package com.aislados.clubdeportivo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aislados.clubdeportivo.model.Cuota

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
    WHERE c.socioId IS NOT NULL
    AND c.id IN (
        SELECT MAX(id)
        FROM cuotas
        WHERE socioId IS NOT NULL
        GROUP BY socioId
    )
    ORDER BY c.fechaVencimiento ASC 
    """)
    fun getLastCuotaForEachSocio(): List<Cuota>?

    @Query("DELETE FROM cuotas WHERE id = :id")
    fun deleteCuotaById(id: Long): Int

    @Query("DELETE FROM cuotas WHERE socioId = :socioId")
    fun deleteCuotasBySocioId(socioId: Long): Int
}