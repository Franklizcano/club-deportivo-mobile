package com.aislados.clubdeportivo.model

data class User(
    val username: String,
    val password: String,
    val role: UserRole
)