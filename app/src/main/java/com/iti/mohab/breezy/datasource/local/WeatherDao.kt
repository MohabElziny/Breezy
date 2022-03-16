package com.iti.mohab.breezy.datasource.local

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("select * from weather where isFavorite = 0")
    fun getCurrentWeather(): OpenWeatherApi

    @Insert(onConflict = IGNORE)
    suspend fun insertWeather(weather: OpenWeatherApi)

    @Update
    suspend fun updateCurrentWeather(weather: OpenWeatherApi)

    @Query("DELETE FROM weather where isFavorite = 0")
    suspend fun deleteCurrentWeather()

    @Query("select * from weather where isFavorite = 1")
    fun getFavoritesWeather(): Flow<List<OpenWeatherApi>>

    @Query("DELETE FROM weather where id = :id")
    suspend fun deleteFavoriteWeather(id: Int)
}