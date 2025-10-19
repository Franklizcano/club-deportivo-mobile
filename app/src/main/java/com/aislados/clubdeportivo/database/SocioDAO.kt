package com.aislados.clubdeportivo.database

class SocioDAO(private val context: android.content.Context) {

    fun getWritableDatabase(): android.database.sqlite.SQLiteDatabase {
        return DatabaseHelper(context).writableDatabase
    }

    fun getReadableDatabase(): android.database.sqlite.SQLiteDatabase {
        return DatabaseHelper(context).readableDatabase
    }

    fun createSocio(socio: com.aislados.clubdeportivo.model.Socio): Boolean {
        var db: android.database.sqlite.SQLiteDatabase? = null
        return try {
            db = getWritableDatabase()
            val contentValues = android.content.ContentValues().apply {
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
}