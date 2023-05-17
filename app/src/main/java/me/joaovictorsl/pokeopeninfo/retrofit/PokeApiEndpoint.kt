package me.joaovictorsl.pokeopeninfo.retrofit

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiEndpoint {

    @GET("pokemon?limit=${Integer.MAX_VALUE}")
    suspend fun getAllPokemon(): PokemonEndpointResult

    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int)//: PokemonByIdEndpointResult

}

data class PokemonEndpointResult(
    @SerializedName("results")
    val results: List<PokemonBasicInfo>
)

data class PokemonBasicInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

//data class PokemonByIdEndpointResult(
//    val sprites: Sprites
//)
