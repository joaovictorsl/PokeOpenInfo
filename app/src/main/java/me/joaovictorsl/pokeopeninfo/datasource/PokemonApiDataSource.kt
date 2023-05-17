package me.joaovictorsl.pokeopeninfo.datasource

import android.util.Log
import kotlinx.coroutines.*
import me.joaovictorsl.pokeopeninfo.model.Pokemon
import me.joaovictorsl.pokeopeninfo.retrofit.PokeApiEndpoint
import me.joaovictorsl.pokeopeninfo.retrofit.getRetrofitInstance
import java.util.*

//fun ableToFetchData(context: Context): Boolean {
//    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val network = connectivityManager.activeNetwork
//    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
//    var isAble = false
//
//    networkCapabilities?.apply {
//        val ethernet = hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
//        val cellular = hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
//        val wifi = hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
//
//        if (ethernet || cellular || wifi) isAble = true
//    }
//
//    return isAble
//}

class PokemonApiDataSource(scope: CoroutineScope) : PokemonDataSource(scope) {
    private val TAG = "PokemonApiDataSource"
    private val pkmService = getRetrofitInstance("https://pokeapi.co/api/v2/").create(PokeApiEndpoint::class.java)

    override suspend fun fetch(): List<Pokemon> {
        var pkm: List<Pokemon> = listOf()

        try {
            val response = pkmService.getAllPokemon()
            pkm = response.results.map {
                val id = it.url.split('/')[6].toInt()
                val formattedName = formatPokemonName(it.name)
                val imgUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
                Pokemon(id, formattedName, imgUrl = imgUrl)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        }

        return pkm
    }

    private fun formatPokemonName(name: String): String {
        return name.split('-').joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }
}