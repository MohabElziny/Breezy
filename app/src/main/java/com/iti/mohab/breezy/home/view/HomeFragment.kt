package com.iti.mohab.breezy.home.view

import android.R.attr
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
    private lateinit var windSpeedUnit: String
    private lateinit var temperatureUnit: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var language: String = "en"
    private var units: String = "metric"

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
                    setValuesFromSharedPreferences()
                    Log.i("mohab", "onViewCreated: $latitude $longitude $language $units")
                    viewModel.getDataFromRemoteToLocal("$latitude", "$longitude", language, units)
                } else if (getIsMap()) {
                    Log.i("zoza", "onViewCreated: ${getIsMap()}")
                    Navigation.findNavController(view)
                        .navigate(R.id.action_navigation_home_to_mapsFragment)
                }
            } else {
                setValuesFromSharedPreferences()
                viewModel.getDataFromRemoteToLocal("$latitude", "$longitude", language, units)
            }
        } else {
            if (!isSharedPreferencesLocationAndTimeZoneNull(requireContext())) {
                viewModel.getDataFromDatabase()
            }
        }
        //tempPerHourAdapter
        initTimeRecyclerView()

        //tempPerDayAdapter
        initDayRecyclerView()

        viewModel.openWeatherAPI.observe(viewLifecycleOwner) {
            updateSharedPreferences(
                requireContext(),
                it.lat,
                it.lon,
                getCityText(requireContext(),it.lat, it.lon),
                it.timezone
            )
            setUnitSetting(units)
            setData(it)
            fetchTempPerTimeRecycler(it.hourly as ArrayList<Hourly>, temperatureUnit)
            fetchTempPerDayRecycler(it.daily as ArrayList<Daily>, temperatureUnit)
        }

        binding.btnSetting.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_navigation_home_to_settingsFragment)
        }

    }

    private fun setUnitSetting(units: String) {
        when (units) {
            "metric" -> {
                temperatureUnit = " °C"
                windSpeedUnit = " m/s"
            }
            "imperial" -> {
                temperatureUnit = " °F"
                windSpeedUnit = " miles/h"
            }
            "standard" -> {
                temperatureUnit = " °K"
                windSpeedUnit = " m/s"
            }
        }
    }

    private fun getIsMap(): Boolean {
        return getSharedPreferences(requireContext()).getBoolean(getString(R.string.isMap), false)
    }

    private fun fetchTempPerDayRecycler(daily: ArrayList<Daily>, temperatureUnit: String) {
        tempPerDayAdapter.apply {
            this.daily = daily
            this.temperatureUnit = temperatureUnit
            notifyDataSetChanged()
        }
    }

    private fun fetchTempPerTimeRecycler(hourly: ArrayList<Hourly>, temperatureUnit: String) {
        tempPerTimeAdapter.apply {
            this.hourly = hourly
            this.temperatureUnit = temperatureUnit
            notifyDataSetChanged()
        }
    }

    private fun setData(model: OpenWeatherApi) {
        val weather = model.current.weather[0]
        binding.apply {
            imageWeatherIcon.setImageResource(getIcon(weather.icon))
            textCurrentDay.text = convertCalenderToDayString(Calendar.getInstance())
            textCurrentDate.text = convertCalenderToDayDate(Calendar.getInstance())
            textCurrentTempreture.text = model.current.temp.toString().plus(temperatureUnit)
            textTempDescription.text = weather.description
            textHumidity.text = model.current.humidity.toString().plus("%")
            textPressure.text = model.current.pressure.toString().plus(" hPa")
            textWindSpeed.text = model.current.windSpeed.toString().plus(windSpeedUnit)
            textCity.text = getCityText(requireContext(),model.lat, model.lon)
        }
//        binding.textCity.text = model.timezone
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

    private fun setValuesFromSharedPreferences() {
        getSharedPreferences(requireContext()).apply {
            latitude = getFloat(getString(R.string.lat), 0.0f).toDouble()
            longitude = getFloat(getString(R.string.lon), 0.0f).toDouble()
            language = getString(getString(R.string.languageSetting), "en") ?: "en"
            units = getString(getString(R.string.unitsSetting), "metric") ?: "metric"
        }
    }

}

