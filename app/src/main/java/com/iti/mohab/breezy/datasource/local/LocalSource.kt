package com.iti.mohab.breezy.datasource.local

import androidx.room.Query
import com.iti.mohab.breezy.model.OpenWeatherApi
import com.iti.mohab.breezy.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    fun getCurrentWeather(): OpenWeatherApi

    suspend fun insertCurrentWeather(weather: OpenWeatherApi):Long

    suspend fun updateWeather(weather: OpenWeatherApi)

    suspend fun deleteWeathers()

    fun getFavoritesWeather(
    ): Flow<List<OpenWeatherApi>>

    suspend fun deleteFavoriteWeather(id:Int)

    fun getFavoriteWeather(id:Int): OpenWeatherApi

    suspend fun insertAlert(alert: WeatherAlert):Long

    fun getAlertsList(): Flow<List<WeatherAlert>>

    suspend fun deleteAlert(id: Int)

    fun getAlert(id: Int): WeatherAlert


}