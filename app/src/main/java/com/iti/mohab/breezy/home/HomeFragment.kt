package com.iti.mohab.breezy.home

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.mohab.breezy.databinding.FragmentHomeBinding
import com.iti.mohab.breezy.datasource.WeatherRepository
import com.iti.mohab.breezy.model.Daily
import com.iti.mohab.breezy.model.Hourly
import com.iti.mohab.breezy.model.OpenWeatherApi
import com.iti.mohab.breezy.util.convertCalenderToDayDate
import com.iti.mohab.breezy.util.convertCalenderToDayString
import com.iti.mohab.breezy.util.getIcon
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var tempPerDayAdapter: TempPerDayAdapter
    private lateinit var tempPerTimeAdapter: TempPerTimeAdapter

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

        viewModel.setLocation()

        viewModel.openWeatherAPI?.observe(viewLifecycleOwner) {
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
        var weather = model.current?.weather?.get(0)
        weather?.icon?.let { getIcon(it) }
            ?.let { binding.imageWeatherIcon.setImageResource(it) }
        binding.textCurrentDay.text = convertCalenderToDayString(Calendar.getInstance())
        binding.textCurrentDate.text = convertCalenderToDayDate(Calendar.getInstance())
        binding.textCurrentTempreture.text = model.current?.temp.toString()
        binding.textTempDescription.text = weather?.description
        binding.textHumidity.text = model.current?.humidity.toString()
        binding.textPressure.text = model.current?.pressure.toString()
        binding.textWindSpeed.text = model.current?.windSpeed.toString()
        setCityText(model.lat!!, model.lon!!)
    }

    private fun setCityText(lat: Double, lon: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        val address: List<String> = addresses[0].getAddressLine(0).split(",")
        val cityName = address[address.size - 2]
//        val stateName: String = addresses[0].getAddressLine(1)
        val countryName: String = address[address.size - 1]
        binding.textCity.text = "$cityName, $countryName"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}