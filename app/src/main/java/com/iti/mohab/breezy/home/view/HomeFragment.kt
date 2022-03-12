package com.iti.mohab.breezy.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var tempPerDayAdapter: TempPerDayAdapter
    private lateinit var tempPerTimeAdapter: TempPerTimeAdapter
    private lateinit var latitude: String
    private lateinit var longitude: String

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
//        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isOnline(requireContext())) {
            if (isSharedPreferencesLocationAndTimeZoneNull(requireContext())) {
                if (!isSharedPreferencesLatAndLongNull(requireContext())) {
                    latitude =
                        getSharedPreferences(requireContext()).getString(
                            getString(R.string.lat),
                            ""
                        )
                            ?: ""
                    longitude =
                        getSharedPreferences(requireContext()).getString(
                            getString(R.string.lon),
                            ""
                        )
                            ?: ""
                    val result = viewModel.insertData(latitude, longitude)
                    Log.i("zoz", "onViewCreated: ${result.timezone}")
                    viewModel.getDataFromDatabase(result.timezone)
                    updateSharedPreferences(
                        requireContext(),
                        result.lat!!,
                        result.lon!!,
//                getCityText(it.lat!!, it.lon!!),
                        result.timezone,
                        result.timezone
                    )
                }
            } else {
                latitude =
                    getSharedPreferences(requireContext()).getString(getString(R.string.lat), "")
                        ?: ""
                longitude =
                    getSharedPreferences(requireContext()).getString(getString(R.string.lon), "")
                        ?: ""
                viewModel.updateData(latitude, longitude)
            }
        } else {
            if (!isSharedPreferencesLocationAndTimeZoneNull(requireContext())) {
                viewModel.getDataFromDatabase(
                    getSharedPreferences(requireContext()).getString(
                        getString(R.string.timeZone),
                        ""
                    ) ?: ""
                )
            }
        }

        viewModel.openWeatherAPI?.observe(viewLifecycleOwner) {
            Log.i("zoz", "onViewCreated: $it")
            updateSharedPreferences(
                requireContext(),
                it.lat!!,
                it.lon!!,
//                getCityText(it.lat!!, it.lon!!),
                it.timezone,
                it.timezone
            )
            setData(it)
            fetchTempPerTimeRecycler(it.hourly as ArrayList<Hourly>)
            fetchTempPerDayRecycler(it.daily as ArrayList<Daily>)
        }

        //tempPerTimeAdapter
        val tempPerTimeLinearLayoutManager = LinearLayoutManager(HomeFragment().context)
        tempPerTimeLinearLayoutManager.orientation = RecyclerView.HORIZONTAL
        tempPerTimeAdapter = TempPerTimeAdapter(this.requireContext())
        binding.recyclerViewTempPerTime.layoutManager = tempPerTimeLinearLayoutManager
        binding.recyclerViewTempPerTime.adapter = tempPerTimeAdapter


        //tempPerDayAdapter
        val tempPerDayLinearLayoutManager = LinearLayoutManager(HomeFragment().context)
        tempPerDayAdapter = TempPerDayAdapter(this.requireContext())
        binding.recyclerViewTempPerDay.layoutManager = tempPerDayLinearLayoutManager
        binding.recyclerViewTempPerDay.adapter = tempPerDayAdapter

//        viewModel.text.observe(viewLifecycleOwner) {
//            binding.textHome.text = it
//        }
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
        val weather = model.current?.weather?.get(0)
        weather?.icon?.let { getIcon(it) }
            ?.let { binding.imageWeatherIcon.setImageResource(it) }
        binding.textCurrentDay.text = convertCalenderToDayString(Calendar.getInstance())
        binding.textCurrentDate.text = convertCalenderToDayDate(Calendar.getInstance())
        binding.textCurrentTempreture.text = model.current?.temp.toString()
        binding.textTempDescription.text = weather?.description
        binding.textHumidity.text = model.current?.humidity.toString()
        binding.textPressure.text = model.current?.pressure.toString()
        binding.textWindSpeed.text = model.current?.windSpeed.toString()
//        binding.textCity.text = getCityText(model.lat!!, model.lon!!)
        binding.textCity.text = model.timezone
    }

//    private fun getCityText(lat: String, lon: String): String {
//        val geocoder = Geocoder(requireContext(), Locale.getDefault())
//        val addresses: List<Address> =
//            geocoder.getFromLocation(lat.fullTrim().toDouble(), lon.fullTrim().toDouble(), 1)
//        val state = addresses[0].adminArea // damietta
//        val country = addresses[0].countryName
////        val knownName = addresses[0].featureName // elglaa
//        return "$state, $country"
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

