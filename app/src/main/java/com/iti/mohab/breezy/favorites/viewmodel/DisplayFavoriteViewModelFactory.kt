package com.iti.mohab.breezy.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.mohab.breezy.datasource.WeatherRepository

class DisplayFavoriteViewModelFactory(private val repository: WeatherRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DisplayFavoriteWeatherViewModel(repository) as T
    }
}