package me.joaovictorsl.pokeopeninfo.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getRetrofitInstance(path : String) : Retrofit {
    return Retrofit.Builder()
        .baseUrl(path)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}