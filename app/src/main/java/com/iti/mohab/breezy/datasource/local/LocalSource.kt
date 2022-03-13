package com.iti.mohab.breezy.datasource.local

import androidx.lifecycle.LiveData
import com.iti.mohab.breezy.model.OpenWeatherApi

interface LocalSource {
    fun getCurrentWeather(timeZone: String): OpenWeatherApi

    suspend fun insertCurrentWeather(weather: OpenWeatherApi)

    suspend fun updateCurrentWeather(weather: OpenWeatherApi)

    suspend fun deleteWeather(timeZone: String)
}