package me.joaovictorsl.pokeopeninfo.recyclerview.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import me.joaovictorsl.pokeopeninfo.R
import me.joaovictorsl.pokeopeninfo.databinding.PokemonItemBinding
import me.joaovictorsl.pokeopeninfo.getFileAsBitmap
import me.joaovictorsl.pokeopeninfo.model.Pokemon
import me.joaovictorsl.pokeopeninfo.ui.pokemondetail.PokemonDetailActivity
import java.io.File
import java.io.FileInputStream

class PokemonAdapter(
    pokemonList: List<Pokemon> = listOf()
) : RecyclerView.Adapter<PokemonAdapter.PokemonHolder>() {
    private val mPokemonList = pokemonList.toMutableList()

    class PokemonHolder(private val binding: PokemonItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: Pokemon) {
            binding.pokemonItemName.text = pokemon.name
            val context = binding.root.context

            val pkmLocalImgList = context.fileList()
            val pkmImgFilename = "${pokemon.pokeApiId}.png"
            val fileNotAvailable = pkmImgFilename !in pkmLocalImgList
            val imgUrlNotAvailable = pokemon.imgUrl == null

            binding.pokemonItemImg.apply {
                if (fileNotAvailable && imgUrlNotAvailable)
                    visibility = View.GONE
                else {
                    if (fileNotAvailable)
                        load(pokemon.imgUrl)
                    else
                        load(getFileAsBitmap(context, pkmImgFilename))
                }
            }

            setOnClickLister(pokemon)
        }

        private fun setOnClickLister(pkm: Pokemon) {
            binding.root.setOnClickListener {
                val context = binding.root.context
                val pokemonExtraKey = context.getString(R.string.pokemon_extra_key)

                val intent = Intent(context, PokemonDetailActivity::class.java).apply {
                    putExtra(pokemonExtraKey, pkm)
                }

                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonHolder {

        val binding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PokemonHolder(binding)
    }

    override fun getItemCount() = mPokemonList.size

    override fun onBindViewHolder(holder: PokemonHolder, position: Int) {
        holder.bind(mPokemonList[position])
    }

    fun update(updatedPkm: Pokemon, position: Int) {
        mPokemonList[position] = updatedPkm
        notifyItemChanged(position)
    }

    fun add(newPkm: Pokemon) {
        mPokemonList.add(newPkm)
        notifyItemInserted(mPokemonList.size - 1)
    }

    fun addAll(newPkms: List<Pokemon>) {
        val start = mPokemonList.size
        mPokemonList.addAll(newPkms)
        notifyItemRangeInserted(start, newPkms.size)
    }

    fun getPokemonList() = mPokemonList.toList()

    fun setPokemonList(newPokemonList: List<Pokemon>) {
        val diffCallback = PokemonDiffUtilCallback(mPokemonList, newPokemonList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mPokemonList.clear()
        mPokemonList.addAll(newPokemonList)
        diffResult.dispatchUpdatesTo(this)
    }

    class PokemonDiffUtilCallback(
        private val oldList: List<Pokemon>,
        private val newList: List<Pokemon>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].pokeApiId == newList[newItemPosition].pokeApiId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldPkm = oldList[oldItemPosition]
            val newPkm = newList[newItemPosition]

            return oldPkm == newPkm
        }

    }

}
