package com.iti.mohab.breezy.datasource.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iti.mohab.breezy.model.OpenWeatherApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

//@RunWith(AndroidJUnit4::class)
class RetrofitHelperTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

//    @Test
//    fun getCurrentWeather_latAndLong_returnOpenWeatherApiModelHasValue() {
//
//        val retrofitHelper = RetrofitHelper
//        var result: Response<OpenWeatherApi>?
//        runBlocking {
//            result = retrofitHelper.getCurrentWeather(lat, long)
//            assertNotNull(result?.body())
//        }
//    }
}