package com.iti.mohab.breezy.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.iti.mohab.breezy.model.OpenWeatherApi

@Dao
interface WeatherDao {
    @Query("select * from weather where timezone = :timeZone")
    fun getCurrentWeather(timeZone: String): OpenWeatherApi

    @Insert(onConflict = IGNORE)
    suspend fun insertCurrentWeather(weather: OpenWeatherApi)

    @Update
    suspend fun updateCurrentWeather(weather: OpenWeatherApi)

    @Query("DELETE FROM weather WHERE timezone = :timeZone")
    suspend fun deleteCurrentWeather(timeZone: String)
}