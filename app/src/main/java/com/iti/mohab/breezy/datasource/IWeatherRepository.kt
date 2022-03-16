package com.iti.mohab.breezy.datasource

import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun insertFavoriteWeatherFromRemoteToLocal(
        lat: String,
        long: String,
        language: String = "en",
        units: String = "metric"
    )

    suspend fun insertCurrentWeatherFromRemoteToLocal(
        lat: String,
        long: String,
        language: String = "en",
        units: String = "metric"
    ) : OpenWeatherApi

    fun getCurrentWeatherFromLocalDataSource(): OpenWeatherApi

    suspend fun deleteWeathersFromLocalDataSource()

    fun getFavoritesWeatherFromLocalDataSource(): Flow<List<OpenWeatherApi>>
}