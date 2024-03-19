package com.example.weathertest.repo

import com.example.weathertest.api.WeatherService
import com.example.weathertest.data.WeatherResponse
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherRepository {

    override suspend fun getCurrentWeather(query: String): WeatherResponse {
        return weatherService.getCurrentWeather(query, "7512c77cf9cfaabd1ff48adba5f95091")
    }
}