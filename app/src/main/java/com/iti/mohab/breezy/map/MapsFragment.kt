package com.iti.mohab.breezy.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.iti.mohab.breezy.R
import com.iti.mohab.breezy.databinding.FragmentMapsBinding
import com.iti.mohab.breezy.util.getSharedPreferences

class MapsFragment : Fragment() {


    private var lat = 30.044
    private var lon = 31.235
    private var _binding: FragmentMapsBinding? = null

    private val binding get() = _binding!!

    private val callback = OnMapReadyCallback { googleMap ->

        val cairo = LatLng(lat, lon)
//        googleMap.addMarker(MarkerOptions().position(cairo))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cairo, 10.0f))
        googleMap.uiSettings.isZoomControlsEnabled = true

        googleMap.setOnMapClickListener { location ->
//            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(location))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f))
            lat = location.latitude
            lon = location.longitude
            binding.btnDone.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding.btnDone.setOnClickListener {
            saveLocationInSharedPreferences(lon, lat)
        }
    }

    private fun saveLocationInSharedPreferences(long: Double, lat: Double) {
        val editor = getSharedPreferences(this.requireContext()).edit()
        editor.putFloat(getString(R.string.lat), lat.toFloat())
        editor.putFloat(getString(R.string.lon), long.toFloat())
        editor.apply()
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_mapsFragment_to_navigation_home)
    }
}