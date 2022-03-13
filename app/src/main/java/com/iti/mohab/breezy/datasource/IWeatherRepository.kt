package com.iti.mohab.breezy.datasource

import androidx.lifecycle.LiveData
import com.iti.mohab.breezy.model.OpenWeatherApi

interface IWeatherRepository {
    suspend fun updateWeatherFromRemoteDataSource(
        lat: String,
        long: String,
        language: String = "en",
        units: String = "metric"
    ) : OpenWeatherApi

    suspend fun insertWeatherFromRemoteDataSource(
        lat: String,
        long: String,
        language: String = "en",
        units: String = "metric"
    ) : OpenWeatherApi

    fun getWeatherFromLocalDataSource(
        timeZone: String
    ): OpenWeatherApi

    suspend fun deleteWeathersFromLocalDataSource()
}