package com.aislados.clubdeportivo.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.aislados.clubdeportivo.model.Socio

class SocioDAO(private val context: Context) {

    fun getWritableDatabase(): SQLiteDatabase = DatabaseHelper(context).writableDatabase

    fun getReadableDatabase(): SQLiteDatabase = DatabaseHelper(context).readableDatabase

    fun createSocio(socio: Socio): Boolean {
        var db: SQLiteDatabase? = null
        return try {
            db = getWritableDatabase()
            val contentValues = ContentValues().apply {
                put(SocioTableHelper.COLUMN_NOMBRE, socio.nombre)
                put(SocioTableHelper.COLUMN_APELLIDO, socio.apellido)
                put(SocioTableHelper.COLUMN_DNI, socio.dni)
                put(SocioTableHelper.COLUMN_FECHA_NACIMIENTO, socio.fechaNacimiento.toString())
                put(SocioTableHelper.COLUMN_DOMICILIO, socio.domicilio)
                put(SocioTableHelper.COLUMN_TELEFONO, socio.telefono)
                put(SocioTableHelper.COLUMN_EMAIL, socio.email)
                put(SocioTableHelper.COLUMN_APTO_FISICO, if (socio.aptoFisico) 1 else 0)
            }
            val result = db.insert(SocioTableHelper.TABLE_NAME, null, contentValues)
            result != -1L
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db?.close()
        }
    }

    fun existsSocio(dni: Int): Boolean {
        val db = getReadableDatabase()
        return try {
            val count = android.database.DatabaseUtils.queryNumEntries(
                db,
                SocioTableHelper.TABLE_NAME,
                "${SocioTableHelper.COLUMN_DNI} = ?",
                arrayOf(dni.toString())
            )
            count > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    fun findSocioByDni(dni: Int): Socio? {
        return try {
            getReadableDatabase().use { db ->
                db.query(
                    SocioTableHelper.TABLE_NAME,
                    null,
                    "${SocioTableHelper.COLUMN_DNI} = ?",
                    arrayOf(dni.toString()),
                    null, null, null
                ).use { cursor ->
                    if (cursor.moveToFirst()) {
                        Socio(
                            id = cursor.getInt(cursor.getColumnIndexOrThrow(SocioTableHelper.COLUMN_ID)),
                            nombre = cursor.getString(cursor.getColumnIndexOrThrow(SocioTableHelper.COLUMN_NOMBRE)),
                            apellido = cursor.getString(cursor.getColumnIndexOrThrow(SocioTableHelper.COLUMN_APELLIDO)),
                            dni = cursor.getInt(cursor.getColumnIndexOrThrow(SocioTableHelper.COLUMN_DNI)),
                            fechaNacimiento = java.time.LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(SocioTableHelper.COLUMN_FECHA_NACIMIENTO))),
                            domicilio = cursor.getString(cursor.getColumnIndexOrThrow(SocioTableHelper.COLUMN_DOMICILIO)),
                            telefono = cursor.getString(cursor.getColumnIndexOrThrow(SocioTableHelper.COLUMN_TELEFONO)),
                            email = cursor.getString(cursor.getColumnIndexOrThrow(SocioTableHelper.COLUMN_EMAIL)),
                            aptoFisico = cursor.getInt(cursor.getColumnIndexOrThrow(SocioTableHelper.COLUMN_APTO_FISICO)) == 1
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