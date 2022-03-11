package com.iti.mohab.breezy.datasource

import androidx.lifecycle.LiveData
import com.iti.mohab.breezy.model.OpenWeatherApi

interface IWeatherRepository {
    suspend fun updateWeatherFromRemoteDataSource(
        lat: Double,
        long: Double,
        language: String = "en",
        units: String = "imperial"
    )

    suspend fun insertWeatherFromRemoteDataSource(
        lat: Double,
        long: Double,
        language: String = "en",
        units: String = "metric"
    )

    suspend fun getWeatherFromLocalDataSource(
        timeZone: String
    ): LiveData<OpenWeatherApi>

    suspend fun deleteWeatherFromLocalDataSource(
        timeZone: String
    )
}