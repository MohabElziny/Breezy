package com.iti.mohab.breezy.datasource

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.iti.mohab.breezy.datasource.local.LocalSource
import com.iti.mohab.breezy.datasource.local.RoomLocalClass
import com.iti.mohab.breezy.datasource.local.WeatherDatabase
import com.iti.mohab.breezy.datasource.remote.RemoteSource
import com.iti.mohab.breezy.datasource.remote.RetrofitHelper
import com.iti.mohab.breezy.model.OpenWeatherApi
import com.iti.mohab.breezy.util.getSharedPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class WeatherRepository(
    private val weatherRemoteDataSource: RemoteSource,
    private val weatherLocalDataSource: LocalSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IWeatherRepository {

    companion object {
        @Volatile
        private var INSTANCE: WeatherRepository? = null

        fun getRepository(app: Application): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                WeatherRepository(
                    RetrofitHelper,
                    RoomLocalClass(WeatherDatabase.getDatabase(app).weatherDao())
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    override suspend fun updateWeatherFromRemoteDataSource(
        lat: String,
        long: String,
        language: String,
        units: String
    ) {
        val remoteWeather = weatherRemoteDataSource.getCurrentWeather(lat, long, language, units)
        if (remoteWeather.isSuccessful) {
            remoteWeather.body()?.let {
                weatherLocalDataSource.updateCurrentWeather(it)
            }
        } else {
            throw Exception("${remoteWeather.errorBody()}")
        }
    }

    override suspend fun insertWeatherFromRemoteDataSource(
        lat: String,
        long: String,
        language: String,
        units: String
    ): OpenWeatherApi? {
        val remoteWeather = weatherRemoteDataSource.getCurrentWeather(lat, long, language, units)
        return if (remoteWeather.isSuccessful) {
            remoteWeather.body()?.let {
                weatherLocalDataSource.insertCurrentWeather(it)
            }
            remoteWeather.body()
        } else {
            null
            //            throw Exception("${remoteWeather.errorBody()}")
        }
    }

    override suspend fun getWeatherFromLocalDataSource(
        timeZone: String
    ): LiveData<OpenWeatherApi> {
        return weatherLocalDataSource.getCurrentWeather(timeZone)
    }

    override suspend fun deleteWeatherFromLocalDataSource(timeZone: String) {
        weatherLocalDataSource.deleteWeather(timeZone)
    }
}