package com.aislados.clubdeportivo.model

import android.os.Parcel
import kotlinx.parcelize.Parceler
import java.time.LocalDate

object LocalDateParceler : Parceler<LocalDate> {
    override fun create(parcel: Parcel): LocalDate {
        return LocalDate.parse(parcel.readString())
    }

    override fun LocalDate.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this.toString())
    }
}