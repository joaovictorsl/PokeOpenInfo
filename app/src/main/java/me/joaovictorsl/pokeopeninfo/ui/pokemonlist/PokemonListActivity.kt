package me.joaovictorsl.pokeopeninfo.ui.pokemonlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import me.joaovictorsl.pokeopeninfo.database.DatabaseFacade
import me.joaovictorsl.pokeopeninfo.databinding.ActivityPokemonListBinding
import me.joaovictorsl.pokeopeninfo.recyclerview.adapter.PokemonAdapter
import me.joaovictorsl.pokeopeninfo.recyclerview.scrolllistener.PaginationScrollListener

class PokemonListActivity : AppCompatActivity() {
    private val TAG = "MainActivityTag"
    private lateinit var pkmAdapter: PokemonAdapter
    private lateinit var viewModel: PokemonListViewModel
    private val binding by lazy { ActivityPokemonListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        DatabaseFacade.initialize(applicationContext)

        configViewModel()
        configRecyclerView()
        configSearchView()
    }

    private fun configViewModel() {
        viewModel = ViewModelProvider(this, PokemonListVMFactory(applicationContext))[PokemonListViewModel::class.java]

        viewModel.pokemons.observe(this) { pkms ->
            pkmAdapter.setPokemonList(pkms)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showSpinnerOnly(isLoading)
        }
    }

    private fun showSpinnerOnly(shouldShowSpinnerOnly: Boolean) {
        // if (shouldShowSpinnerOnly) {
        //     binding.pokemonList.visibility = View.GONE
        //     binding.pokemonListSpinner.visibility = View.VISIBLE
        // } else {
        //     binding.pokemonList.visibility = View.VISIBLE
        //     binding.pokemonListSpinner.visibility = View.GONE
        // }
    }

    private fun configRecyclerView() {
        pkmAdapter = PokemonAdapter()
        binding.pokemonList.adapter = pkmAdapter

        binding.pokemonList.addOnScrollListener(PaginationScrollListener(
            fetchNext = { viewModel.fetchNextPage() },
            fetchPrev = { viewModel.fetchPrevPage() })
        )
    }

    private fun configSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                search()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search()
                return true
            }

            private fun search() {
                val query = binding.searchView.query.toString()

                viewModel.searchPokemon(query) { binding.pokemonList.layoutManager?.scrollToPosition(0) }
            }
        })
    }

}
