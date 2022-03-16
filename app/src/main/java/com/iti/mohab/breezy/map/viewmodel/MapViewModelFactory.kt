package com.iti.mohab.breezy.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.mohab.breezy.datasource.WeatherRepository

class MapViewModelFactory(private val repository:WeatherRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repository) as T
    }
}