package com.iti.mohab.breezy.home.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.iti.mohab.breezy.datasource.IWeatherRepository
import com.iti.mohab.breezy.datasource.MyLocationProvider
import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(private val repository: IWeatherRepository,private val myLocationProvider:MyLocationProvider) : ViewModel() {

    fun getDataFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            _openWeatherAPI.postValue(
                repository.getCurrentWeatherFromLocalDataSource()
            )
        }
    }

    fun getDataFromRemoteToLocal(lat: String, long: String, language: String, units: String) {
        var result: OpenWeatherApi? = null
        viewModelScope.launch(Dispatchers.Main) {
            val job =
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        result =
                            repository.insertCurrentWeatherFromRemoteToLocal(lat, long, language, units)
                    } catch (e: Exception) {
                        Log.i("zoz", "getDataFromRemoteToLocal: ${e.message}")
                    }

                }
            job.join()
            result?.let { getDataFromDatabase() }
            this.cancel()
        }
    }

    fun getFreshLocation() {
        myLocationProvider.getFreshLocation()
    }

    fun observeLocation():LiveData<ArrayList<Double>>{
        return myLocationProvider.locationList
    }

    private val _openWeatherAPI = MutableLiveData<OpenWeatherApi>()
    val openWeatherAPI: LiveData<OpenWeatherApi> = _openWeatherAPI
}