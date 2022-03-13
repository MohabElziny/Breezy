package com.iti.mohab.breezy.datasource.local

import androidx.lifecycle.LiveData
import com.iti.mohab.breezy.model.OpenWeatherApi

class RoomLocalClass(private val weatherDao: WeatherDao) : LocalSource {

    override fun getCurrentWeather(timeZone: String): OpenWeatherApi {
        return weatherDao.getCurrentWeather(timeZone)
    }

    override suspend fun insertCurrentWeather(weather: OpenWeatherApi) {
        weatherDao.insertCurrentWeather(weather)
    }

    override suspend fun updateCurrentWeather(weather: OpenWeatherApi) {
        weatherDao.updateCurrentWeather(weather)
    }

    override suspend fun deleteWeather(timeZone: String) {
        weatherDao.deleteCurrentWeather(timeZone)
    }

}