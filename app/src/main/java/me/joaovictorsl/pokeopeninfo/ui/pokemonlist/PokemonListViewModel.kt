package me.joaovictorsl.pokeopeninfo.ui.pokemonlist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import me.joaovictorsl.pokeopeninfo.repository.PokemonRepository

class PokemonListViewModel(
    context: Context
) : ViewModel() {
    private val TAG = "PokemonListViewModel"
    private val pkmRepo = PokemonRepository(context, viewModelScope)
    val pokemons = pkmRepo.pokemons

    private var currentFirstPage = 1
    private val MIN_FIRST_PAGE = 1
    private var currentLastPage = 3
    private var MAX_LAST_PAGE = -1

    private var _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private suspend fun setIsLoadingTo(value: Boolean)= withContext(Dispatchers.Main) { _isLoading.value = value }

    private var isSearching = false

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchData()
            fetchBitmaps()
        }
    }

    private suspend fun fetchData() {
        setIsLoadingTo(true)
        pkmRepo.fetchPokemons(currentFirstPage, currentLastPage)
        MAX_LAST_PAGE = if (MAX_LAST_PAGE == -1) pkmRepo.pageCount() else MAX_LAST_PAGE
        setIsLoadingTo(false)
    }

    private suspend fun fetchBitmaps() {
        pkmRepo.fetchImageBitmap()
    }

    fun fetchNextPage() {
        if (otherOperationInProgress() || currentLastPage == MAX_LAST_PAGE) return

        viewModelScope.launch(Dispatchers.IO) {
            currentFirstPage += 1
            currentLastPage += 1
            fetchData()
        }
    }

    fun fetchPrevPage() {
        if (otherOperationInProgress() || currentFirstPage == MIN_FIRST_PAGE) return

        viewModelScope.launch(Dispatchers.IO) {
            currentFirstPage -= 1
            currentLastPage -= 1
            fetchData()
        }
    }

    fun searchPokemon(query: String, afterSearchBehavior: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isBlank()) {
                currentFirstPage = 1
                currentLastPage = 3
                fetchData()
                isSearching = false
            } else {
                isSearching = true
                setIsLoadingTo(true)
                pkmRepo.searchPokemons(query)
                setIsLoadingTo(false)
            }

            withContext(Dispatchers.Main) { afterSearchBehavior() }
        }
    }

    private fun otherOperationInProgress() = _isLoading.value!! || isSearching
}
