package com.example.weathertest.repo

import com.example.weathertest.data.WeatherResponse

interface WeatherRepository {
    suspend fun getCurrentWeather(query: String): WeatherResponse
}