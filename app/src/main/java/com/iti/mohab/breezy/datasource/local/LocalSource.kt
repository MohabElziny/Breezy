package com.iti.mohab.breezy.datasource.local

import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    fun getCurrentWeather(): OpenWeatherApi

    suspend fun insertCurrentWeather(weather: OpenWeatherApi)

    suspend fun updateCurrentWeather(weather: OpenWeatherApi)

    suspend fun deleteWeathers()

    fun getFavoritesWeather(
    ): Flow<List<OpenWeatherApi>>


}