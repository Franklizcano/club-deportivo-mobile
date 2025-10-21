package com.aislados.clubdeportivo.database

import android.database.sqlite.SQLiteDatabase

object CuotaTableHelper {
    const val TABLE_NAME = "cuotas"
    const val COLUMN_ID = "id"
    const val COLUMN_SOCIO_ID = "socio_id"
    const val COLUMN_FECHA_PAGO = "fecha_pago"
    const val COLUMN_FECHA_VENCIMIENTO = "fecha_vencimiento"
    const val COLUMN_MONTO = "monto"
    const val COLUMN_ACTIVIDAD = "actividad"

    fun createTable(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SOCIO_ID INTEGER NOT NULL,
                $COLUMN_FECHA_PAGO DATE NOT NULL,
                $COLUMN_FECHA_VENCIMIENTO DATE NOT NULL,
                $COLUMN_MONTO REAL NOT NULL,
                $COLUMN_ACTIVIDAD VARCHAR(50),
                FOREIGN KEY($COLUMN_SOCIO_ID) REFERENCES ${SocioTableHelper.TABLE_NAME}(${SocioTableHelper.COLUMN_ID})
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    fun dropTable(db: SQLiteDatabase?) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }
}