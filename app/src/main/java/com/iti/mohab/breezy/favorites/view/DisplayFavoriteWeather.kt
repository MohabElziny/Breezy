package com.iti.mohab.breezy.favorites.view

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iti.mohab.breezy.R
import com.iti.mohab.breezy.databinding.DisplayFavoriteWeatherFragmentBinding
import com.iti.mohab.breezy.datasource.WeatherRepository
import com.iti.mohab.breezy.favorites.viewmodel.DisplayFavoriteViewModelFactory
import com.iti.mohab.breezy.favorites.viewmodel.DisplayFavoriteWeatherViewModel
import com.iti.mohab.breezy.home.view.HomeFragment
import com.iti.mohab.breezy.home.view.TempPerDayAdapter
import com.iti.mohab.breezy.home.view.TempPerTimeAdapter
import com.iti.mohab.breezy.model.Daily
import com.iti.mohab.breezy.model.Hourly
import com.iti.mohab.breezy.model.OpenWeatherApi
import com.iti.mohab.breezy.util.*
import java.util.*

class DisplayFavoriteWeather : Fragment() {
    private lateinit var tempPerDayAdapter: TempPerDayAdapter
    private lateinit var tempPerTimeAdapter: TempPerTimeAdapter
    private lateinit var windSpeedUnit: String
    private lateinit var temperatureUnit: String
    private val viewModel: DisplayFavoriteWeatherViewModel by viewModels {
        DisplayFavoriteViewModelFactory(WeatherRepository.getRepository(requireActivity().application))
    }

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var language: String = "en"
    private var units: String = "metric"

    private lateinit var binding: DisplayFavoriteWeatherFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DisplayFavoriteWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackButton()
        binding.btnBack.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_displayFavoriteWeather_to_navigation_dashboard)
        }

        //tempPerHourAdapter
        initTimeRecyclerView()

        //tempPerDayAdapter
        initDayRecyclerView()
        val id = requireArguments().getInt("id")
        if (isOnline(requireContext())) {
            getOnlineNeeds()
            viewModel.updateWeather(latitude, longitude, units, language, id)
        } else {
            val snackBar = Snackbar.make(binding.root, "You are offline", Snackbar.LENGTH_LONG)
            snackBar.view.setBackgroundColor(Color.RED)
            snackBar.show()
            viewModel.getWeather(id)
        }
        viewModel.weather.observe(viewLifecycleOwner) {
            setUnitSetting(units)
            it?.let { it1 -> setData(it1) }
            fetchTempPerTimeRecycler(it?.hourly as ArrayList<Hourly>, temperatureUnit)
            fetchTempPerDayRecycler(it.daily as ArrayList<Daily>, temperatureUnit)
        }
    }

    private fun getOnlineNeeds() {
        latitude = requireArguments().getDouble("lat")
        longitude = requireArguments().getDouble("lon")
        units = getSharedPreferences(requireContext()).getString(
            getString(R.string.unitsSetting),
            "metric"
        )!!
        language = getSharedPreferences(requireContext()).getString(
            getString(R.string.languageSetting),
            "en"
        )!!
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

    private fun setUnitSetting(units: String) {
        if (language == "en") {
            setEnglishUnits(units)
        } else {
            setArabicUnit(units)
        }
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
            textCurrentDay.text = convertCalenderToDayString(Calendar.getInstance(), language)
            textCurrentDate.text =
                convertLongToDayDate(Calendar.getInstance().timeInMillis, language)
            textTempDescription.text = weather.description
            textCity.text = getCityText(requireContext(), model.lat, model.lon, language)
            if (language == "ar") {
                bindArabicUnits(model)
            } else {
                bindEnglishUnits(model)
            }
        }
    }

    private fun handleBackButton() {
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                Navigation.findNavController(v)
                    .navigate(R.id.action_displayFavoriteWeather_to_navigation_dashboard)
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })
    }


    private fun setArabicUnit(units: String) {
        when (units) {
            "metric" -> {
                temperatureUnit = " ????"
                windSpeedUnit = " ??/??"
            }
            "imperial" -> {
                temperatureUnit = " ????"
                windSpeedUnit = " ??????/??"
            }
            "standard" -> {
                temperatureUnit = " ????"
                windSpeedUnit = " ??/??"
            }
        }
    }

    private fun setEnglishUnits(units: String) {
        when (units) {
            "metric" -> {
                temperatureUnit = " ??C"
                windSpeedUnit = " m/s"
            }
            "imperial" -> {
                temperatureUnit = " ??F"
                windSpeedUnit = " miles/h"
            }
            "standard" -> {
                temperatureUnit = " ??K"
                windSpeedUnit = " m/s"
            }
        }
    }

    private fun bindArabicUnits(model: OpenWeatherApi) {
        binding.apply {
            textCurrentTempreture.text =
                convertNumbersToArabic(model.current.temp.toInt()).plus(temperatureUnit)
            textHumidity.text = convertNumbersToArabic(model.current.humidity)
                .plus("??")
            textPressure.text = convertNumbersToArabic(model.current.pressure)
                .plus(" ????")
            textClouds.text = convertNumbersToArabic(model.current.clouds)
                .plus("??")
            textVisibility.text = convertNumbersToArabic(model.current.visibility)
                .plus("??")
            textUvi.text = convertNumbersToArabic(model.current.uvi.toInt())
            textWindSpeed.text =
                convertNumbersToArabic(model.current.windSpeed).plus(windSpeedUnit)
        }
    }

    private fun bindEnglishUnits(model: OpenWeatherApi) {
        binding.apply {
            textCurrentTempreture.text = model.current.temp.toInt().toString().plus(temperatureUnit)
            textHumidity.text = model.current.humidity.toString().plus("%")
            textPressure.text = model.current.pressure.toString().plus(" hPa")
            textClouds.text = model.current.clouds.toString().plus("%")
            textVisibility.text = model.current.visibility.toString().plus("m")
            textUvi.text = model.current.uvi.toString()
            textWindSpeed.text = model.current.windSpeed.toString().plus(windSpeedUnit)
        }
    }

}