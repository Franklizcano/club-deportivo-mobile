package com.aislados.clubdeportivo.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "cuotas",
    foreignKeys = [ForeignKey(
        entity = Socio::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("socioId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Cuota(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val socioId: Long,
    val fechaPago: LocalDate,
    val fechaVencimiento: LocalDate,
    val monto: Double,
    val actividad: String?
)