package me.joaovictorsl.pokeopeninfo.database

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.joaovictorsl.pokeopeninfo.database.converters.BitmapConverter
import me.joaovictorsl.pokeopeninfo.database.dao.PokemonDao
import me.joaovictorsl.pokeopeninfo.model.Pokemon

@androidx.room.Database(entities = [Pokemon::class], version = 1)
@TypeConverters(BitmapConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPokemonDao(): PokemonDao

}