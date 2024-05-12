package com.timur.vkpokemon

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.timur.vkpokemon.databinding.FragmentPokemonListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PokemonListFragment : Fragment(), PokemonListAdapter.OnPokemonClickListener {

    private var _binding: FragmentPokemonListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PokemonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PokemonListAdapter(this)
        binding.recyclerViewPokemon.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPokemon.adapter = adapter

        loadPokemons()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPokemons() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = PokemonApiClient.api.getPokemons(limit = 1000, offset = 0)
                Log.d("Pokemons", "$response")
                val pokemonsWithId = response.results.mapIndexed { index, pokemon ->
                    val id = (index + 1)
                    Pokemon(pokemon.name, pokemon.url, id)
                }
                launch(Dispatchers.Main) {
                    adapter.submitList(pokemonsWithId)
                }
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }
    }






    override fun onPokemonClick(pokemon: Pokemon) {
        val detailFragment = PokemonDetailFragment.newInstance(pokemon)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
