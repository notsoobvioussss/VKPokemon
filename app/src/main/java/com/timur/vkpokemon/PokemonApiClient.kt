package com.timur.vkpokemon

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object PokemonApiClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val api: PokemonApi= retrofit.create(PokemonApi::class.java)
    val api2: PokemonApiService = retrofit.create(PokemonApiService::class.java)
}
