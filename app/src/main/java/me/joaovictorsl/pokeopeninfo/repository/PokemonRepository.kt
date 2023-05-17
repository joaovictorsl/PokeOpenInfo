package me.joaovictorsl.pokeopeninfo.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.joaovictorsl.pokeopeninfo.database.DatabaseFacade
import me.joaovictorsl.pokeopeninfo.datasource.PokemonApiDataSource
import me.joaovictorsl.pokeopeninfo.datasource.PokemonBitmapFetcher
import me.joaovictorsl.pokeopeninfo.datasource.PokemonDatabaseDataSource
import me.joaovictorsl.pokeopeninfo.model.Pokemon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.joaovictorsl.pokeopeninfo.extensions.updateOnDbIfNeeded
import kotlin.math.nextUp

var alreadyFetchedRemote = false

class PokemonRepository(
    context: Context,
    private val scope: CoroutineScope
    ) {
    private val TAG = "PokemonRepository"
    private val _pokemons = MutableLiveData<List<Pokemon>>()
    val pokemons: LiveData<List<Pokemon>>
        get() = _pokemons

    private val pkmDbDataSource by lazy { PokemonDatabaseDataSource(scope) }
    private val pkmApiDataSource by lazy { PokemonApiDataSource(scope) }
    private val pkmBitmapDataSource by lazy { PokemonBitmapFetcher(context) }
    private val pkmDao by lazy { DatabaseFacade.getInstance().getPokemonDao() }

    suspend fun pageCount() = (pkmDao.count() / 100.0).nextUp().toInt()

    suspend fun fetchPokemons(firstPage: Int, lastPage: Int) {
        fetchDatabase(firstPage, lastPage)

        if (!alreadyFetchedRemote) {
            val remotePkm = fetchRemote()
            val dbModified = pkmDao.updateOnDbIfNeeded(remotePkm)
            if (dbModified) fetchDatabase(firstPage, lastPage)

            alreadyFetchedRemote = true
        }
    }

    private suspend fun fetchDatabase(firstPage: Int, lastPage: Int) {
        val localPkm = pkmDbDataSource.fetchPaginated(firstPage, lastPage)
        withContext(Dispatchers.Main) { _pokemons.value = localPkm }
    }

    private suspend fun fetchRemote(): List<Pokemon> {
        return pkmApiDataSource.fetch()
    }

    suspend fun fetchImageBitmap() {
        val pkmOnDb = pkmDao.getAll()

        var first = 0
        var last = 50
        val lastValidIdx = pkmOnDb.lastIndex

        while(first != last) {

            pkmBitmapDataSource.fetch(pkmOnDb.subList(first, last))
            Log.i(TAG, "Batch first: $first  last: $last lastValidIdx: $lastValidIdx")

            first = last
            last = if (last + 50 > lastValidIdx) lastValidIdx else last + 50
        }
    }

    suspend fun searchPokemons(query: String) {
        val goodQuery = query.trim()
            // Replaces in sequence whitespaces with only one whitespace.
            .replace("\\s+".toRegex(), " ")

        val queryResult = pkmDbDataSource.searchPokemon(goodQuery)
        withContext(Dispatchers.Main) { _pokemons.value = queryResult }
    }
}
