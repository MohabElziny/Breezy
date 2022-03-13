package com.iti.mohab.breezy.home.view

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.mohab.breezy.R
import com.iti.mohab.breezy.databinding.FragmentHomeBinding
import com.iti.mohab.breezy.datasource.WeatherRepository
import com.iti.mohab.breezy.home.viewmodel.HomeViewModel
import com.iti.mohab.breezy.home.viewmodel.HomeViewModelFactory
import com.iti.mohab.breezy.model.Daily
import com.iti.mohab.breezy.model.Hourly
import com.iti.mohab.breezy.model.OpenWeatherApi
import com.iti.mohab.breezy.util.*
import java.io.IOException
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var tempPerDayAdapter: TempPerDayAdapter
    private lateinit var tempPerTimeAdapter: TempPerTimeAdapter
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(WeatherRepository.getRepository(requireActivity().application))
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isOnline(requireContext())) {
            if (isSharedPreferencesLocationAndTimeZoneNull(requireContext())) {
                if (!isSharedPreferencesLatAndLongNull(requireContext())) {
                    setLatitudeAndLongitudeValuesFromSharedPreferences()
                    viewModel.insertData("$latitude", "$longitude")
                } else if (getIsMap()) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_navigation_home_to_mapsFragment)
                }
            } else {
                setLatitudeAndLongitudeValuesFromSharedPreferences()
                viewModel.updateData("$latitude", "$longitude")
            }
        } else {
            if (!isSharedPreferencesLocationAndTimeZoneNull(requireContext())) {
                val timeZone = getSharedPreferences(requireContext()).getString(
                    getString(R.string.timeZone),
                    ""
                ) ?: ""
                if (!timeZone.isNullOrEmpty()) {
                    viewModel.getDataFromDatabase(timeZone)
                }
            }
        }

        viewModel.openWeatherAPI.observe(viewLifecycleOwner) {
            updateSharedPreferences(
                requireContext(),
                it.lat,
                it.lon,
                getCityText(it.lat, it.lon),
                it.timezone
            )
            setData(it)
            fetchTempPerTimeRecycler(it.hourly as ArrayList<Hourly>)
            fetchTempPerDayRecycler(it.daily as ArrayList<Daily>)
        }
        //tempPerHourAdapter
        initTimeRecyclerView()

        //tempPerDayAdapter
        initDayRecyclerView()

        binding.btnSetting.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_navigation_home_to_settingsFragment);
        }

    }

    private fun getIsMap(): Boolean {
        val sharedPreferences = getSharedPreferences(requireContext())
        return sharedPreferences.getBoolean("isMap", false)
    }

    private fun fetchTempPerDayRecycler(daily: ArrayList<Daily>) {
        tempPerDayAdapter.daily = daily
        tempPerDayAdapter.notifyDataSetChanged()
    }

    private fun fetchTempPerTimeRecycler(hourly: ArrayList<Hourly>) {
        tempPerTimeAdapter.hourly = hourly
        tempPerTimeAdapter.notifyDataSetChanged()
    }

    private fun setData(model: OpenWeatherApi) {
        val weather = model.current.weather[0]
        binding.imageWeatherIcon.setImageResource(getIcon(weather.icon))
        binding.textCurrentDay.text = convertCalenderToDayString(Calendar.getInstance())
        binding.textCurrentDate.text = convertCalenderToDayDate(Calendar.getInstance())
        binding.textCurrentTempreture.text = model.current.temp.toString()
        binding.textTempDescription.text = weather.description
        binding.textHumidity.text = model.current.humidity.toString()
        binding.textPressure.text = model.current.pressure.toString()
        binding.textWindSpeed.text = model.current.windSpeed.toString()
        binding.textCity.text = getCityText(model.lat, model.lon)
//        binding.textCity.text = model.timezone
    }

    private fun getCityText(lat: Double, lon: Double): String {
        var city = "Unknown!"
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)[0]
            if (addresses != null) {
                val state = addresses.adminArea // damietta
                val country = addresses.countryName
                city = "$state, $country"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
//        val knownName = addresses[0].featureName // elglaa
        return city
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initTimeRecyclerView() {
        val tempPerTimeLinearLayoutManager = LinearLayoutManager(HomeFragment().context)
        tempPerTimeLinearLayoutManager.orientation = RecyclerView.HORIZONTAL
        tempPerTimeAdapter = TempPerTimeAdapter(this.requireContext())
        binding.recyclerViewTempPerTime.layoutManager = tempPerTimeLinearLayoutManager
        binding.recyclerViewTempPerTime.adapter = tempPerTimeAdapter
    }

    private fun initDayRecyclerView() {
        val tempPerDayLinearLayoutManager = LinearLayoutManager(HomeFragment().context)
        tempPerDayAdapter = TempPerDayAdapter(this.requireContext())
        binding.recyclerViewTempPerDay.layoutManager = tempPerDayLinearLayoutManager
        binding.recyclerViewTempPerDay.adapter = tempPerDayAdapter
    }

    private fun setLatitudeAndLongitudeValuesFromSharedPreferences() {
        latitude =
            getSharedPreferences(requireContext()).getFloat(
                getString(R.string.lat),
                0.0f
            ).toDouble()
        longitude =
            getSharedPreferences(requireContext()).getFloat(
                getString(R.string.lon),
                0.0f
            ).toDouble()
    }


}

