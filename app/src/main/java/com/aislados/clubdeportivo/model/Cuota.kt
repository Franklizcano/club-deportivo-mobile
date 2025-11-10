package com.aislados.clubdeportivo.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "cuotas",
    foreignKeys = [ForeignKey(
        entity = Socio::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("socioId"),
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = NoSocio::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("noSocioId"),
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index(value = ["socioId"])]
)
data class Cuota(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val socioId: Long? = null,
    val noSocioId: Long? = null,
    val fechaPago: LocalDate,
    val fechaVencimiento: LocalDate,
    val metodoPago: String,
    val monto: Double,
    val actividad: String?
)