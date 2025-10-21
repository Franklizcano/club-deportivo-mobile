package com.aislados.clubdeportivo.model

import java.time.LocalDate

data class Socio(
    val id: Int?,
    val nombre: String,
    val apellido: String,
    val dni: Int,
    val fechaNacimiento: LocalDate,
    val domicilio: String,
    val telefono: String,
    val email: String,
    val aptoFisico: Boolean
)