package com.iti.mohab.breezy.datasource.network

import com.iti.mohab.breezy.model.OpenWeatherApi
import retrofit2.Response

interface RemoteSource {
    suspend fun getCurrentWeather(
        lat: Double,
        long: Double,
        language: String,
         units: String
    ): Response<OpenWeatherApi>
}