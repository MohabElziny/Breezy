package com.iti.mohab.breezy.dialogs.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mohab.breezy.datasource.WeatherRepository
import com.iti.mohab.breezy.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertTimeDialogViewModel(private val repository: WeatherRepository) : ViewModel() {
    fun insertAlert(alert: WeatherAlert) {
        var response: Long? = null
        viewModelScope.launch(Dispatchers.IO) {
            val job = viewModelScope.launch(Dispatchers.IO) {
                Log.i("peter", "insertAlert: ")
                response = repository.insertAlert(alert)
            }
            job.join()
            Log.i("peter", "insertAlert: $response")
            if (response != null) {
                _id.postValue(response!!)
            }
        }
    }

    private var _id = MutableLiveData<Long>()
    val id = _id
}