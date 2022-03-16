package com.iti.mohab.breezy.favorites.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.mohab.breezy.R
import com.iti.mohab.breezy.databinding.FragmentFavoritesBinding
import com.iti.mohab.breezy.datasource.WeatherRepository
import com.iti.mohab.breezy.favorites.viewmodel.FavoritesViewModel
import com.iti.mohab.breezy.favorites.viewmodel.FavoritesViewModelFactory
import com.iti.mohab.breezy.model.OpenWeatherApi
import com.iti.mohab.breezy.util.getSharedPreferences
import kotlinx.coroutines.flow.collect

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(WeatherRepository.getRepository(requireActivity().application))
    }

    private lateinit var favoriteAdapter: FavoriteAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val previousId = Navigation.findNavController(view).previousBackStackEntry?.destination?.id

        initFavoritesRecyclerView()

        binding.floatingActionButton.setOnClickListener {
            val action = FavoritesFragmentDirections.actionNavigationDashboardToMapsFragment(true)
            findNavController().navigate(action)
        }

//        if (previousId == R.id.mapsFragment) {
//            val latLon = arguments?.getString(getString(R.string.latlon))?.split(",")
//            val lat = latLon?.get(0)
//            val lon = latLon?.get(1)
//
//
//        } else {
            viewModel.getFavorites()
//        }

        lifecycleScope.launchWhenStarted {
            viewModel.favorites.collect {
                if (!it.isNullOrEmpty()) {
                    binding.textEmptyList.visibility = View.GONE
                }
                fetchFavoritesRecycler(it)
            }
        }
    }

    private fun fetchFavoritesRecycler(list: List<OpenWeatherApi>?) {
        favoriteAdapter.favoriteList = list ?: emptyList()
        favoriteAdapter.notifyDataSetChanged()
    }

    private fun initFavoritesRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(FavoritesFragment().context)
        favoriteAdapter = FavoriteAdapter(this.requireContext())
        binding.favoriteRecyclerView.layoutManager = linearLayoutManager
        binding.favoriteRecyclerView.adapter = favoriteAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}