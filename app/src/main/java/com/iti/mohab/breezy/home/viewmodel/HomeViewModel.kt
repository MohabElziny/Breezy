package com.iti.mohab.breezy.home.viewmodel

import androidx.lifecycle.*
import com.iti.mohab.breezy.datasource.IWeatherRepository
import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(private val repository: IWeatherRepository) : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text

    fun updateData(lat: String, long: String) {
        var result: OpenWeatherApi? = null
        viewModelScope.launch(Dispatchers.IO) {
            val job = viewModelScope.launch(Dispatchers.IO) {
                result = repository.updateWeatherFromRemoteDataSource(lat, long)
            }
            job.join()
            getDataFromDatabase(result!!.timezone)
        }
    }

    fun getDataFromDatabase(timeZone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _openWeatherAPI.postValue(
                repository.getWeatherFromLocalDataSource(timeZone)
            )
        }
    }

    fun insertData(lat: String, long: String) {
        var result: OpenWeatherApi? = null

        val job =
            viewModelScope.launch(Dispatchers.IO) {
                result = repository.insertWeatherFromRemoteDataSource(lat, long)
            }

        viewModelScope.launch(Dispatchers.Main) {
            job.join()
            getDataFromDatabase(result!!.timezone)
        }
    }

    private val _openWeatherAPI = MutableLiveData<OpenWeatherApi>()
    val openWeatherAPI: LiveData<OpenWeatherApi> = _openWeatherAPI
}