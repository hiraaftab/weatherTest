package com.example.weathertest.viewModel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.example.weathertest.data.WeatherResponse

import com.example.weathertest.repo.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    init {
        Log.d("", "ViewModel created")
        // Log the repository or other dependencies if needed
    }

    val weatherData = MutableLiveData<WeatherResponse>()
    val errorMessage = MutableLiveData<String>()

    fun getCurrentWeather(query: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentWeather(query)
                weatherData.value = response
            } catch (e: Exception) {
                errorMessage?.value = e.message
            }
        }
    }
}