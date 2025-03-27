package com.example.weatherapp

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository
): ViewModel() {
    //Weather data exposed to the UI
    private val _weatherData = MutableLiveData<WeatherResponse?>()
    val weatherData: LiveData<WeatherResponse?> = _weatherData

    //Loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    //Error handling
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadWeatherData(activity: Activity) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            weatherRepository.getWeatherForCurrentLocation(
                activity = activity,
                onWeatherData = { response ->
                    _isLoading.value = false

                    if(response != null) {
                        _weatherData.value = response
                    }
                },
                onError = { errorMessage ->
                    _isLoading.value = false
                    _error.value = errorMessage
                }
                )
        }
    }
}