package com.iti.mohab.breezy.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mohab.breezy.datasource.WeatherRepository
import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: WeatherRepository) : ViewModel() {

    fun getFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavoritesWeatherFromLocalDataSource().collect {
                _favorites.emit(it)
            }
        }
    }

    fun deleteFavoriteWeather(id: Int) {
        viewModelScope.launch {
            repository.deleteFavoriteWeather(id)
        }
    }

    private var _favorites = MutableStateFlow<List<OpenWeatherApi>>(emptyList())
    val favorites = _favorites


}