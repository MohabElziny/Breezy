package com.iti.mohab.breezy.home.viewmodel

import androidx.lifecycle.*
import com.iti.mohab.breezy.datasource.IWeatherRepository
import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: IWeatherRepository) : ViewModel() {

//    fun updateData(lat: String, long: String) {
//        var result: OpenWeatherApi? = null
//        viewModelScope.launch(Dispatchers.IO) {
//            val job = viewModelScope.launch(Dispatchers.IO) {
//                result = repository.updateWeatherFromRemoteDataSource(lat, long)
//            }
//            job.join()
//            getDataFromDatabase(result!!.timezone)
//            this.cancel()
//        }
//    }

    fun getDataFromDatabase(timeZone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _openWeatherAPI.postValue(
                repository.getWeatherFromLocalDataSource(timeZone)
            )
        }
    }

    fun getDataFromRemoteToLocal(lat: String, long: String) {
        var result: OpenWeatherApi? = null
        viewModelScope.launch(Dispatchers.Main) {
            val job =
                viewModelScope.launch(Dispatchers.IO) {
                    result = repository.insertWeatherFromRemoteDataSource(lat, long)
                }
            job.join()
            getDataFromDatabase(result!!.timezone)
            this.cancel()
        }
    }

    private val _openWeatherAPI = MutableLiveData<OpenWeatherApi>()
    val openWeatherAPI: LiveData<OpenWeatherApi> = _openWeatherAPI
}