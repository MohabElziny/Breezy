package com.iti.mohab.breezy.datasource.remote

import com.iti.mohab.breezy.model.OpenWeatherApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val appId = "44379a6ffaf4fd02206a072b5bffea52"
private const val excludeMinutely = "minutely"

interface RetrofitService {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = excludeMinutely,
        @Query("units") units: String,
        @Query("lang") lang: String ,
        @Query("appid") app_id: String = appId
    ): Response<OpenWeatherApi>
}