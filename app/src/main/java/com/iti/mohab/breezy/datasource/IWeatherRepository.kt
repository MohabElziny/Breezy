package com.iti.mohab.breezy.datasource

import androidx.lifecycle.LiveData
import com.iti.mohab.breezy.model.OpenWeatherApi

interface IWeatherRepository {
    suspend fun updateWeatherFromRemoteDataSource(
        lat: String,
        long: String,
        language: String = "en",
        units: String = "metric"
    )

    suspend fun insertWeatherFromRemoteDataSource(
        lat: String,
        long: String,
        language: String = "en",
        units: String = "metric"
    ) : OpenWeatherApi?

    suspend fun getWeatherFromLocalDataSource(
        timeZone: String
    ): LiveData<OpenWeatherApi>

    suspend fun deleteWeatherFromLocalDataSource(
        timeZone: String
    )
}