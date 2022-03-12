package com.iti.mohab.breezy.datasource.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

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