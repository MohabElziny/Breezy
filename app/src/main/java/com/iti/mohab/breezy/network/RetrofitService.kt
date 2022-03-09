package com.iti.mohab.breezy.network

import com.iti.mohab.breezy.model.OpenWeatherApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val appId = "44379a6ffaf4fd02206a072b5bffea52"
private const val _exclude = "minutely"

interface RetrofitService {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "imperial",
        @Query("exclude") exclude: String = _exclude,
        @Query("appid") appid: String = appId
    ): Response<OpenWeatherApi>
}