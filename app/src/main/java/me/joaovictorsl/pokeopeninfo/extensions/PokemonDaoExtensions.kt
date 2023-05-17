package me.joaovictorsl.pokeopeninfo.extensions

import me.joaovictorsl.pokeopeninfo.database.DatabaseFacade
import me.joaovictorsl.pokeopeninfo.database.dao.PokemonDao
import me.joaovictorsl.pokeopeninfo.exceptions.DatabaseNotInitializedException
import me.joaovictorsl.pokeopeninfo.model.Pokemon

suspend fun PokemonDao.updateOnDbIfNeeded(upToDateList: List<Pokemon>): Boolean {
    if (upToDateList.isEmpty()) return false
    if (!DatabaseFacade.isInitialized()) throw DatabaseNotInitializedException()

    val needsUpdate = mutableListOf<Pokemon>()
    val newPkmList = mutableListOf<Pokemon>()
    var dbWasModified = false
    val pokemonsOnDb = getAll().toMutableList()

    upToDateList.forEach { updatedPkm ->
        val idxOnDbPkm = pokemonsOnDb.binarySearch(updatedPkm)

        if (idxOnDbPkm == -1)
            newPkmList.add(updatedPkm)
        else {
            val onDbPkm = pokemonsOnDb[idxOnDbPkm]
            val isUpdated = onDbPkm.isUpToDate(updatedPkm)

            if (!isUpdated)
                needsUpdate.add(updatedPkm)
        }
    }

    if (needsUpdate.isNotEmpty()) {
        update(*needsUpdate.toTypedArray())
        dbWasModified = true
    }

    if (newPkmList.isNotEmpty()) {
        insert(*newPkmList.toTypedArray())
        dbWasModified = true
    }

    return dbWasModified
}