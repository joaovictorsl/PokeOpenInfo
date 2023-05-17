package me.joaovictorsl.pokeopeninfo.retrofit

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface PokemonImageEndpoint {

    @GET("{id}.png")
    @Streaming
    suspend fun getImage(@Path("id") id: Int): ResponseBody

}