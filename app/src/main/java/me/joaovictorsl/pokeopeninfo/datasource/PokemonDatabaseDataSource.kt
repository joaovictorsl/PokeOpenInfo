package me.joaovictorsl.pokeopeninfo.datasource

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import me.joaovictorsl.pokeopeninfo.database.DatabaseFacade
import me.joaovictorsl.pokeopeninfo.model.Pokemon

class PokemonDatabaseDataSource(scope: CoroutineScope) : PokemonDataSource(scope) {
    private val TAG = "PokemonDatabaseFetcher"
    private val PAGE_LENGTH = 50
    private val pkmDao by lazy { DatabaseFacade.getInstance().getPokemonDao() }

    override suspend fun fetch(): List<Pokemon> {
        var pokemonsOnDb: List<Pokemon> = listOf()

        try {
            pokemonsOnDb = pkmDao.getAll()
        } catch (e: IllegalStateException) {
            Log.e(TAG, e.message!!)
        }

        return pokemonsOnDb
    }

    suspend fun fetchPaginated(firstPage: Int, lastPage: Int): List<Pokemon> {
        var pokemonsOnDb: List<Pokemon> = listOf()

        try {
            pokemonsOnDb = pkmDao.getAllOnRange((firstPage - 1) * PAGE_LENGTH, lastPage * PAGE_LENGTH)
        } catch (e: IllegalStateException) {
            Log.e(TAG, e.message!!)
        }

        return pokemonsOnDb
    }

    suspend fun searchPokemon(query: String): List<Pokemon> {
        var results: List<Pokemon> = listOf()

        try {
            results = pkmDao.searchByName(query)
        } catch (e: IllegalStateException) {
            Log.e(TAG, e.message!!)
        }

        return results
    }

}