package com.iti.mohab.breezy.datasource.local

import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.flow.Flow

class RoomLocalClass(private val weatherDao: WeatherDao) : LocalSource {

    override fun getCurrentWeather(): OpenWeatherApi {
        return weatherDao.getCurrentWeather()
    }

    override suspend fun insertCurrentWeather(weather: OpenWeatherApi) {
        weatherDao.insertWeather(weather)
    }

    override suspend fun updateCurrentWeather(weather: OpenWeatherApi) {
        weatherDao.updateCurrentWeather(weather)
    }

    override suspend fun deleteWeathers() {
        weatherDao.deleteCurrentWeather()
    }

    override fun getFavoritesWeather(): Flow<List<OpenWeatherApi>> {
        return weatherDao.getFavoritesWeather()
    }

}