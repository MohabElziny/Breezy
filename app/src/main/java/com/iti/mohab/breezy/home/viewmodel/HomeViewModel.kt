package com.iti.mohab.breezy.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.iti.mohab.breezy.datasource.IWeatherRepository
import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class HomeViewModel(private val repository: IWeatherRepository) : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text

    fun updateData(lat: String, long: String) {
        val result: OpenWeatherApi
        runBlocking(Dispatchers.IO) {
            result = repository.updateWeatherFromRemoteDataSource(lat, long)
        }
        getDataFromDatabase(result.timezone)
    }

    fun getDataFromDatabase(timeZone: String) {
        _timezone.value = timeZone
    }


    fun insertData(lat: String, long: String) {
        val result: OpenWeatherApi
        runBlocking(Dispatchers.IO) {
            result = repository.insertWeatherFromRemoteDataSource(lat, long)
        }
        getDataFromDatabase(result.timezone)
    }


    private val _timezone = MutableLiveData<String>()

    private val _openWeatherAPI = _timezone.switchMap { timeZone ->
        repository.getWeatherFromLocalDataSource(timeZone)
    }

    val openWeatherAPI: LiveData<OpenWeatherApi> = _openWeatherAPI
}