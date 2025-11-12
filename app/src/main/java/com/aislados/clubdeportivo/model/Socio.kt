package com.aislados.clubdeportivo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import java.time.LocalDate

@Parcelize
@TypeParceler<LocalDate, LocalDateParceler>()
@Entity(tableName = "socios")
data class Socio(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val apellido: String,
    val dni: Int,
    val fechaNacimiento: LocalDate,
    val domicilio: String,
    val telefono: String,
    val email: String,
    val aptoFisico: Boolean
): Parcelable