package com.iti.mohab.breezy.datasource.local

import com.iti.mohab.breezy.model.OpenWeatherApi
import com.iti.mohab.breezy.model.WeatherAlert
import kotlinx.coroutines.flow.Flow

class RoomLocalClass(private val weatherDao: WeatherDao) : LocalSource {

    override fun getCurrentWeather(): OpenWeatherApi {
        return weatherDao.getCurrentWeather()
    }

    override fun getFavoriteWeather(id: Int): OpenWeatherApi {
        return weatherDao.getFavoriteWeather(id)
    }

    override suspend fun insertAlert(alert: WeatherAlert) {
        weatherDao.insertAlert(alert)
    }

    override fun getAlertsList(): Flow<List<WeatherAlert>> {
        return weatherDao.getAlertsList()
    }

    override suspend fun deleteAlert(id: Int) {
        weatherDao.deleteAlert(id)
    }

    override suspend fun insertCurrentWeather(weather: OpenWeatherApi) {
        weatherDao.insertWeather(weather)
    }

    override suspend fun updateWeather(weather: OpenWeatherApi) {
        weatherDao.updateWeather(weather)
    }

    override suspend fun deleteWeathers() {
        weatherDao.deleteCurrentWeather()
    }

    override fun getFavoritesWeather(): Flow<List<OpenWeatherApi>> {
        return weatherDao.getFavoritesWeather()
    }

    override suspend fun deleteFavoriteWeather(id: Int) {
        weatherDao.deleteFavoriteWeather(id)
    }

}