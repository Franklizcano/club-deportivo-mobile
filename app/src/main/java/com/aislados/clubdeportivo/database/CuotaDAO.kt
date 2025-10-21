package com.aislados.clubdeportivo.database

import android.content.Context
import com.aislados.clubdeportivo.model.Cuota

class CuotaDAO(private val context: Context) {
    fun getWritableDatabase() = DatabaseHelper(context).writableDatabase

    fun getReadableDatabase() = DatabaseHelper(context).readableDatabase

    fun createCuota(cuota: Cuota) {
        try {
            getWritableDatabase().use { db ->
                val contentValues = android.content.ContentValues().apply {
                    put(CuotaTableHelper.COLUMN_SOCIO_ID, cuota.socioId)
                    put(CuotaTableHelper.COLUMN_FECHA_PAGO, cuota.fechaPago.toString())
                    put(CuotaTableHelper.COLUMN_FECHA_VENCIMIENTO, cuota.fechaVencimiento.toString())
                    put(CuotaTableHelper.COLUMN_MONTO, cuota.monto)
                    put(CuotaTableHelper.COLUMN_ACTIVIDAD, cuota.actividad)
                }
                db.insert(CuotaTableHelper.TABLE_NAME, null, contentValues)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun findCuotaBySocioId(socioId: Int): Cuota? {
        return try {
            getReadableDatabase().use { db ->
                db.query(
                    CuotaTableHelper.TABLE_NAME,
                    null,
                    "${CuotaTableHelper.COLUMN_SOCIO_ID} = ?",
                    arrayOf(socioId.toString()),
                    null, null, "${CuotaTableHelper.COLUMN_ID} DESC"
                ).use { cursor ->
                    if (cursor.moveToFirst()) {
                        Cuota(
                            id = cursor.getInt(cursor.getColumnIndexOrThrow(CuotaTableHelper.COLUMN_ID)),
                            socioId = socioId,
                            fechaPago = java.time.LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(CuotaTableHelper.COLUMN_FECHA_PAGO))),
                            fechaVencimiento = java.time.LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(CuotaTableHelper.COLUMN_FECHA_VENCIMIENTO))),
                            monto = cursor.getDouble(cursor.getColumnIndexOrThrow(CuotaTableHelper.COLUMN_MONTO)),
                            actividad = cursor.getString(cursor.getColumnIndexOrThrow(CuotaTableHelper.COLUMN_ACTIVIDAD))
                        )
                    } else null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}