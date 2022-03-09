package com.iti.mohab.breezy.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iti.mohab.breezy.model.OpenWeatherApi
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

//@RunWith(AndroidJUnit4::class)
class RetrofitHelperTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getCurrentWeather_latAndLong_returnOpenWeatherApiModelHasValue() {
        val lat = 31.417540
        val long = 31.814444
        val retrofitHelper = RetrofitHelper
        var result: Response<OpenWeatherApi>?
        runBlocking {
            result = retrofitHelper.getCurrentWeather(lat, long)
            assertNotNull(result?.body())
        }
    }
}