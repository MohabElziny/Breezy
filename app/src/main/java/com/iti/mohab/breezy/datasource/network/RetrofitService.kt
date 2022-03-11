package com.iti.mohab.breezy.datasource.network

import com.iti.mohab.breezy.model.OpenWeatherApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val appId = "44379a6ffaf4fd02206a072b5bffea52"
private const val excludeMinutely = "minutely"
private const val defaultUnits = "metric"
private const val defaultLanguage = "en"

interface RetrofitService {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = excludeMinutely,
        @Query("units") units: String = defaultUnits,
        @Query("lang") lang: String = defaultLanguage,
        @Query("appid") app_id: String = appId
    ): Response<OpenWeatherApi>
}