package com.iti.mohab.breezy

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
import com.iti.mohab.breezy.databinding.FragmentMapsBinding
import com.iti.mohab.breezy.util.getSharedPreferences

class MapsFragment : Fragment() {
    private var lat = 30.044
    private var lng = 31.235
    private var _binding: FragmentMapsBinding? = null

    private val binding get() = _binding!!

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val cairo = LatLng(lat, lng)
        googleMap.addMarker(MarkerOptions().position(cairo).title("Marker in Cairo"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cairo, 12.0f))
        googleMap.uiSettings.isZoomControlsEnabled = true


        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 18.0f))
            binding.btnDone.visibility = View.VISIBLE
            lat = it.latitude
            lng = it.latitude
            binding.btnDone.setOnClickListener {
                saveLocationInSharedPreferences(lat, lng)
            }
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
    }

    private fun saveLocationInSharedPreferences(long: Double, lat: Double) {
        val editor = getSharedPreferences(this.requireContext()).edit()
        editor.putFloat(getString(R.string.lat), lat.toFloat())
        editor.putFloat(getString(R.string.lon), long.toFloat())
        editor.apply()
        Navigation.findNavController(_binding!!.root)
            .navigate(R.id.action_mapsFragment_to_navigation_home)
    }
}