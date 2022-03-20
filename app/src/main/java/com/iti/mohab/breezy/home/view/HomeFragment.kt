package com.iti.mohab.breezy.home.view

import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iti.mohab.breezy.R
import com.iti.mohab.breezy.broadcastreceiver.ConnectivityReceiver
import com.iti.mohab.breezy.databinding.FragmentHomeBinding
import com.iti.mohab.breezy.datasource.MyLocationProvider
import com.iti.mohab.breezy.datasource.WeatherRepository
import com.iti.mohab.breezy.home.viewmodel.HomeViewModel
import com.iti.mohab.breezy.home.viewmodel.HomeViewModelFactory
import com.iti.mohab.breezy.model.Daily
import com.iti.mohab.breezy.model.Hourly
import com.iti.mohab.breezy.model.OpenWeatherApi
import com.iti.mohab.breezy.util.*
import java.util.*


class HomeFragment : Fragment(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var tempPerDayAdapter: TempPerDayAdapter
    private lateinit var tempPerTimeAdapter: TempPerTimeAdapter
    private lateinit var windSpeedUnit: String
    private lateinit var temperatureUnit: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var language: String = "en"
    private var units: String = "metric"
    private var flagNoConnection: Boolean = false

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            WeatherRepository.getRepository(requireActivity().application),
            MyLocationProvider(this)
        )
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

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireContext().registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        if (!flagNoConnection) {
            if (isSharedPreferencesLocationAndTimeZoneNull(requireContext())) {
                if (!isSharedPreferencesLatAndLongNull(requireContext())) {
                    setValuesFromSharedPreferences()
                    viewModel.getDataFromRemoteToLocal("$latitude", "$longitude", language, units)
                } else if (getIsMap()) {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_navigation_home_to_mapsFragment)
                } else {
                    //dialog to get fresh location
                    val location = MyLocationProvider(this)
                    if (location.checkPermission() && location.isLocationEnabled()) {
                        viewModel.getFreshLocation()
                    } else {
                        binding.homeView.visibility = View.GONE
                        binding.cardLocation.visibility = View.VISIBLE
                        if (!location.checkPermission()) {
                            binding.textDialog.text = getString(R.string.location_permission)
                        } else if (!location.isLocationEnabled()) {
                            binding.textDialog.text = getString(R.string.location_enabled)
                        }
                        binding.btnEnable.setOnClickListener {
                            viewModel.getFreshLocation()
                        }
                    }
                }
            } else {
                setValuesFromSharedPreferences()
                viewModel.getDataFromRemoteToLocal("$latitude", "$longitude", language, units)
            }
        }

        //tempPerHourAdapter
        initTimeRecyclerView()

        //tempPerDayAdapter
        initDayRecyclerView()

        initSwipeRefresh()

        viewModel.observeLocation().observe(viewLifecycleOwner) {
            binding.homeView.visibility = View.VISIBLE
            binding.cardLocation.visibility = View.GONE
            if (it[0] != 0.0 && it[1] != 0.0) {
                latitude = it[0]
                longitude = it[1]
                val local = getCurrentLocale(requireContext())
                language = getSharedPreferences(requireContext()).getString(
                    getString(R.string.languageSetting), local?.language
                )!!
                units = getSharedPreferences(requireContext()).getString(
                    getString(R.string.unitsSetting),
                    "metric"
                )!!
                viewModel.getDataFromRemoteToLocal(
                    "$latitude",
                    "$longitude",
                    language,
                    units
                )
            }
        }

        viewModel.openWeatherAPI.observe(viewLifecycleOwner) {
            binding.swiperefresh.isRefreshing = false
            updateSharedPreferences(
                requireContext(),
                it.lat,
                it.lon,
                getCityText(requireContext(), it.lat, it.lon, language),
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

    private fun initSwipeRefresh() {
        binding.swiperefresh.setOnRefreshListener {
            if (!getIsMap()) {
                binding.swiperefresh.isRefreshing = true
                viewModel.getFreshLocation()
            }else{
                binding.swiperefresh.isRefreshing = false
            }
        }
    }

    private fun getIsMap(): Boolean {
        return getSharedPreferences(requireContext()).getBoolean(getString(R.string.isMap), false)
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

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            if (flagNoConnection) {
                val snackBar = Snackbar.make(binding.root, "Back Online", Snackbar.LENGTH_SHORT)
                snackBar.view.setBackgroundColor(Color.GREEN)
                snackBar.show()
                flagNoConnection = false
                refreshFragment()
            }
        } else {
            flagNoConnection = true
            val snackBar = Snackbar.make(binding.root, "You are offline", Snackbar.LENGTH_LONG)
            snackBar.view.setBackgroundColor(Color.RED)
            snackBar.show()
            getLocalData()
        }
    }

    private fun getLocalData() {
        if (!isSharedPreferencesLocationAndTimeZoneNull(requireContext())) {
            viewModel.getDataFromDatabase()
        }
    }

    private fun refreshFragment() {
        val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
        }
        ft.detach(this).attach(this).commit()
    }

    private fun setArabicUnit(units: String) {
        when (units) {
            "metric" -> {
                temperatureUnit = " °م"
                windSpeedUnit = " م/ث"
            }
            "imperial" -> {
                temperatureUnit = " °ف"
                windSpeedUnit = " ميل/س"
            }
            "standard" -> {
                temperatureUnit = " °ك"
                windSpeedUnit = " م/ث"
            }
        }
    }

    private fun setEnglishUnits(units: String) {
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

    private fun bindArabicUnits(model: OpenWeatherApi) {
        binding.apply {
            textCurrentTempreture.text =
                convertNumbersToArabic(model.current.temp).plus(temperatureUnit)
            textHumidity.text = convertNumbersToArabic(model.current.humidity)
                .plus("٪")
            textPressure.text = convertNumbersToArabic(model.current.pressure)
                .plus(" هب")
            textClouds.text = convertNumbersToArabic(model.current.clouds)
                .plus("٪")
            textVisibility.text = convertNumbersToArabic(model.current.visibility)
                .plus("م")
            textUvi.text = convertNumbersToArabic(model.current.uvi)
            textWindSpeed.text =
                convertNumbersToArabic(model.current.windSpeed).plus(windSpeedUnit)
        }
    }

    private fun bindEnglishUnits(model: OpenWeatherApi) {
        binding.apply {
            textCurrentTempreture.text = model.current.temp.toString().plus(temperatureUnit)
            textHumidity.text = model.current.humidity.toString().plus("%")
            textPressure.text = model.current.pressure.toString().plus(" hPa")
            textClouds.text = model.current.clouds.toString().plus("%")
            textVisibility.text = model.current.visibility.toString().plus("m")
            textUvi.text = model.current.uvi.toString()
            textWindSpeed.text = model.current.windSpeed.toString().plus(windSpeedUnit)
        }
    }
}

