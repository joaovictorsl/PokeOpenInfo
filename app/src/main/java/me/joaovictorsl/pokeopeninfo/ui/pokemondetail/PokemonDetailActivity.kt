package me.joaovictorsl.pokeopeninfo.ui.pokemondetail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import me.joaovictorsl.pokeopeninfo.R
import me.joaovictorsl.pokeopeninfo.databinding.ActivityPokemonDetailBinding
import me.joaovictorsl.pokeopeninfo.getFileAsBitmap
import me.joaovictorsl.pokeopeninfo.model.Pokemon

class PokemonDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPokemonDetailBinding.inflate(layoutInflater) }
    private val ivPkmImg by lazy { binding.pokemonDetailImg }
    private val tvPkmName by lazy { binding.pokemonDetailName }
    private val pokemon by lazy { loadPokemonFromExtra() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setPokemonInfo()
    }

    private fun setPokemonInfo() {
        val filename = "${pokemon.pokeApiId}.png"
        val data: Any? = if (filename in fileList())
            getFileAsBitmap(this, filename)
        else
            pokemon.imgUrl

        ivPkmImg.load(data)
        tvPkmName.text = pokemon.name
    }

    private fun loadPokemonFromExtra(): Pokemon {
        val pkmExtraKey = getString(R.string.pokemon_extra_key)

        val pkmFromExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.extras?.getParcelable(pkmExtraKey, Pokemon::class.java)
        else
            intent.extras?.getParcelable(pkmExtraKey) as Pokemon?

        return pkmFromExtra ?: Pokemon(0, "Unknown Pokemon")
    }
}