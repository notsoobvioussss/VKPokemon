package com.timur.vkpokemon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.timur.vkpokemon.databinding.FragmentPokemonDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PokemonDetailFragment : Fragment() {

    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!
    private val pokemonApiService = PokemonApiClient.api

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokemon = arguments?.getParcelable<Pokemon>("pokemon")

        pokemon?.let {
            binding.textPokemonName.text = it.name
            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${it.id}.png"
            Picasso.get().load(imageUrl).into(binding.imagePokemon)
            getPokemonDetails(it.name.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPokemonDetails(pokemonName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = PokemonApiClient.api2.getPokemonByName(pokemonName)
                val pokemonDetail = response
                pokemonDetail.let {
                    val height = it.height
                    val weight = it.weight
                    GlobalScope.launch(Dispatchers.Main) {
                        binding.textPokemonHeight.text = "Height: $height"
                        binding.textPokemonWeight.text = "Weight: $weight"
                    }
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                // Обработка ошибки HTTP
            } catch (e: Exception) {
                e.printStackTrace()
                // Обработка других исключений
            }
        }
    }

    companion object {
        fun newInstance(pokemon: Pokemon): PokemonDetailFragment {
            val fragment = PokemonDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable("pokemon", pokemon)
            fragment.arguments = bundle
            return fragment
        }
    }
}
