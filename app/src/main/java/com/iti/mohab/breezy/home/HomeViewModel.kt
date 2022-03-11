package com.iti.mohab.breezy.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iti.mohab.breezy.datasource.IWeatherRepository
import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.runBlocking

class HomeViewModel(private val repository: IWeatherRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun setLocation() {
        val lat = 31.417540
        val long = 31.814444
        runBlocking {
            repository.insertWeatherFromRemoteDataSource(lat, long)
        }
    }

    private val _openWeatherAPI = MutableLiveData<LiveData<OpenWeatherApi>>().apply {
        runBlocking {
            value = repository.getWeatherFromLocalDataSource("Africa/Cairo")
        }
    }

    val openWeatherAPI: LiveData<OpenWeatherApi>? = _openWeatherAPI.value
}