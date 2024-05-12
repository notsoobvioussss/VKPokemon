package com.timur.vkpokemon

data class PokemonResponse(
    val results: List<PokemonResult>
)
data class PokemonResult(
    val name: String,
    val url: String,
)