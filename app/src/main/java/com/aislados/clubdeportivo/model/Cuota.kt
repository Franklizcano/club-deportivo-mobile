package com.aislados.clubdeportivo.model

import java.time.LocalDate

data class Cuota(
    val id: Int?,
    val socioId: Int,
    val fechaPago: LocalDate,
    val fechaVencimiento: LocalDate,
    val monto: Double,
    val actividad: String?
)