package com.aislados.clubdeportivo.database

import android.database.sqlite.SQLiteDatabase

object SocioTableHelper {
    const val TABLE_NAME = "socios"
    const val COLUMN_ID = "id"
    const val COLUMN_NOMBRE = "nombre"
    const val COLUMN_APELLIDO = "apellido"
    const val COLUMN_DNI = "dni"
    const val COLUMN_FECHA_NACIMIENTO = "fecha_nacimiento"
    const val COLUMN_DOMICILIO = "domicilio"
    const val COLUMN_TELEFONO = "telefono"
    const val COLUMN_EMAIL = "email"
    const val COLUMN_APTO_FISICO = "apto_fisico"

    fun createTable(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOMBRE VARCHAR(100) NOT NULL,
                $COLUMN_APELLIDO VARCHAR(100) NOT NULL,
                $COLUMN_DNI VARCHAR(20) NOT NULL,
                $COLUMN_FECHA_NACIMIENTO DATE NOT NULL,
                $COLUMN_DOMICILIO VARCHAR(200),
                $COLUMN_TELEFONO VARCHAR(20),
                $COLUMN_EMAIL VARCHAR(100),
                $COLUMN_APTO_FISICO BOOLEAN NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    fun dropTable(db: SQLiteDatabase?) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }
}