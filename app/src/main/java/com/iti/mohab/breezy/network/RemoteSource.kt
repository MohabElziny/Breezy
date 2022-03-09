package com.iti.mohab.breezy.network

import com.iti.mohab.breezy.model.OpenWeatherApi
import retrofit2.Response

interface RemoteSource {
    suspend fun getCurrentWeather(
        lat: Double,
        long: Double,
        language: String = "en",
         units: String = "imperial",
    ): Response<OpenWeatherApi>
}