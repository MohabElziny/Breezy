package com.iti.mohab.breezy.favorites.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mohab.breezy.datasource.WeatherRepository
import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DisplayFavoriteWeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    fun getWeather(id: Int) {
        var result: OpenWeatherApi? = null
        val job = viewModelScope.launch(Dispatchers.IO) {
            result = repository.getFavoriteWeather(id)
        }
        viewModelScope.launch(Dispatchers.Main) {
            job.join()
            job.cancel()
            _weather.postValue(result)
            this.cancel()
        }
    }

    fun updateWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String,
        id: Int
    ) {
        var result: OpenWeatherApi? = null
        val job = viewModelScope.launch(Dispatchers.IO) {
            result =
                repository.updateFavoriteWeather("$latitude", "$longitude", units, language, id)
        }
        viewModelScope.launch(Dispatchers.Main) {
            job.join()
            job.cancel()
            _weather.postValue(result)
            this.cancel()
        }
    }

    private var _weather = MutableLiveData<OpenWeatherApi?>()
    val weather = _weather

}