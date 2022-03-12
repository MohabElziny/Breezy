package com.iti.mohab.breezy.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iti.mohab.breezy.datasource.IWeatherRepository
import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.*

class HomeViewModel(private val repository: IWeatherRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun updateData(lat: String, long: String) {
        runBlocking {
            repository.updateWeatherFromRemoteDataSource(lat, long)
        }
    }

    fun getDataFromDatabase(timeZone: String) {
        _openWeatherAPI.apply {
            runBlocking {
                _openWeatherAPI.value = repository.getWeatherFromLocalDataSource(timeZone)
                Log.i("TAG", "getDataFromDatabase: ${_openWeatherAPI.value?.value}")
            }
        }
    }


    fun insertData(lat: String, long: String): OpenWeatherApi {
        var result: OpenWeatherApi?
        runBlocking {
            result = repository.insertWeatherFromRemoteDataSource(lat, long)
        }
        return result!!
    }

    private var _openWeatherAPI = MutableLiveData<LiveData<OpenWeatherApi>>().apply {
        runBlocking {
            value = repository.getWeatherFromLocalDataSource("Etc/GMT-2")
        }
    }

    val openWeatherAPI: LiveData<OpenWeatherApi>? = _openWeatherAPI.value
}