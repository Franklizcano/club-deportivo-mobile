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
    fun findCuotaBySocioId(socioId: Long): Cuota?

    @Query("SELECT * FROM cuotas WHERE socioId = :socioId ORDER BY id DESC")
    fun findAllCuotasBySocioId(socioId: Long): List<Cuota>

    @Query("SELECT * FROM cuotas")
    fun getAllCuotas(): List<Cuota>

    @Query("DELETE FROM cuotas WHERE id = :id")
    fun deleteCuotaById(id: Long): Int

    @Query("DELETE FROM cuotas WHERE socioId = :socioId")
    fun deleteCuotasBySocioId(socioId: Long): Int
}