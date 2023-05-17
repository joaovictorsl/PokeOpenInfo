package me.joaovictorsl.pokeopeninfo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.joaovictorsl.pokeopeninfo.model.Pokemon

@Dao
interface PokemonDao {

    @Insert
    suspend fun insert(vararg pokemons: Pokemon)

    @Update
    suspend fun update(vararg target: Pokemon)

    @Query("SELECT * FROM pokemon WHERE pokeApiId = :pokeApiId")
    suspend fun getById(pokeApiId: Int): Pokemon?

    @Query("SELECT * FROM pokemon")
    suspend fun getAll(): List<Pokemon>

    @Query("SELECT * FROM pokemon LIMIT :lastRecord OFFSET :firstRecord")
    suspend fun getAllOnRange(firstRecord: Int, lastRecord: Int): List<Pokemon>

    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<Pokemon>

    @Query("SELECT COUNT(pokeApiId) FROM pokemon")
    suspend fun count(): Int
}
