package me.joaovictorsl.pokeopeninfo.ui.pokemonlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PokemonListVMFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PokemonListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PokemonListViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}