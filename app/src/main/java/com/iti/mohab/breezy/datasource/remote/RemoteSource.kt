package com.iti.mohab.breezy.datasource.remote

import com.iti.mohab.breezy.model.OpenWeatherApi
import retrofit2.Response

interface RemoteSource {
    suspend fun getCurrentWeather(
        lat: String,
        long: String,
        language: String,
        units: String
    ): Response<OpenWeatherApi>
}