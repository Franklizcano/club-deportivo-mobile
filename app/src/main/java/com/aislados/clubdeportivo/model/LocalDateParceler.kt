package com.aislados.clubdeportivo.model

import android.os.Parcel
import kotlinx.parcelize.Parceler
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateParceler : Parceler<LocalDate> {
    override fun create(parcel: Parcel): LocalDate {
        return LocalDate.parse(parcel.readString(), DateTimeFormatter.ISO_LOCAL_DATE)
    }

    override fun LocalDate.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }
}