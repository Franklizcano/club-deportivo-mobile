package com.aislados.clubdeportivo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = Socio::class,
            parentColumns = ["id"],
            childColumns = ["socioId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["socioId"])]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val password: String,
    val role: UserRole,
    val socioId: Long? = null
) : Parcelable