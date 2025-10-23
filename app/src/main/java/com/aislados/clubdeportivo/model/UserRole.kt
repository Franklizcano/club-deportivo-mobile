package com.aislados.clubdeportivo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class UserRole: Parcelable {
    ADMIN,
    SOCIO
}