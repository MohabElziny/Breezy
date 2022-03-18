package com.iti.mohab.breezy.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mohab.breezy.datasource.WeatherRepository
import com.iti.mohab.breezy.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AlertsViewModel(private val repository: WeatherRepository) : ViewModel() {

    private var _alerts = MutableStateFlow<List<WeatherAlert>>(emptyList())
    val alerts = _alerts

    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAlertsList().collect {
                _alerts.emit(it)
            }
        }
    }

    fun deleteFavoriteWeather(id: Int) {
        viewModelScope.launch {
            repository.deleteAlert(id)
        }
    }
}