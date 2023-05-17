package me.joaovictorsl.pokeopeninfo.datasource

import kotlinx.coroutines.CoroutineScope
import me.joaovictorsl.pokeopeninfo.model.Pokemon

abstract class PokemonDataSource(protected val scope: CoroutineScope) {

    abstract suspend fun fetch(): List<Pokemon>

}
