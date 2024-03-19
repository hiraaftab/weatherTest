package com.example.weathertest.api

import com.example.weathertest.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

        @GET("weather")
        suspend fun getCurrentWeather(
            @Query("q") query: String,
            @Query("appid") apiKey: String

        ): WeatherResponse

}