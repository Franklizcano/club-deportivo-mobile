package com.aislados.clubdeportivo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aislados.clubdeportivo.model.Cuota
import com.aislados.clubdeportivo.model.NoSocio
import com.aislados.clubdeportivo.model.Socio
import com.aislados.clubdeportivo.model.User

@Database(entities = [User::class, Socio::class, Cuota::class, NoSocio::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun socioDao(): SocioDAO
    abstract fun cuotaDao(): CuotaDAO
    abstract fun noSocioDao(): NoSocioDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "club_deportivo_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Permite ejecutar queries sincrónicas en el hilo principal
                    .build()
                INSTANCE = instance
                instance
            }

        }
    }
}
