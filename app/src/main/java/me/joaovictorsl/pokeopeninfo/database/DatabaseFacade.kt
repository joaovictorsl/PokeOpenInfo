package me.joaovictorsl.pokeopeninfo.database

import android.content.Context
import androidx.room.Room
import me.joaovictorsl.pokeopeninfo.exceptions.DatabaseNotInitializedException

object DatabaseFacade {
    private lateinit var db: AppDatabase

    fun getInstance(): AppDatabase {
        if (!::db.isInitialized)
            throw DatabaseNotInitializedException()

        return db
    }

    fun initialize(applicationContext: Context) {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "pokeopeninfo-database"
        )
            .build()
    }

    fun isInitialized() = ::db.isInitialized
}