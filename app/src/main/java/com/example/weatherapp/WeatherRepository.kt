package com.example.weatherapp

import android.app.Activity
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//@TODO Analyze this entire file later

class WeatherRepository(
    private val weatherApiService: WeatherApiService,
    private val locationRepository: LocationRepository,
) {
    //wtf is onWeatherData
    suspend fun getWeatherForCurrentLocation(activity: Activity, onWeatherData: (WeatherResponse?) -> Unit, onError: (String) -> Unit) {
        locationRepository.handleLocationAccess(
            activity = activity,
            onLocationRetrieved = { coordinates ->
                Log.d("WeatherRepository", "Using coordinates lat: ${coordinates.latitude}, lon: ${coordinates.longitude}")

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val requestUrl = "https://api.openweathermap.org/data/3.0/onecall?lat=${coordinates.latitude}&lon=${coordinates.longitude}&units=metric&exclude=hourly,daily&appid=${BuildConfig.API_KEY}"
                        Log.d("WeatherRepository", "Requestion weather from: $requestUrl")

                        val weather = weatherApiService.getWeatherData(
                            latitude = coordinates.latitude,
                            longitude = coordinates.longitude,
                        )

                        Log.d("WeatherRepository", "Weather API response successful")

                        withContext(Dispatchers.Main) {
                            onWeatherData(weather)
                        }
                    } catch (e: Exception) {
                        Log.e("WeatherRepository", "Error fetching weather data: ${e.javaClass.simpleName} - ${e.message}")
                        e.printStackTrace()

                        withContext(Dispatchers.Main) {
                            val errorMessage = when (e) {
                                is java.net.UnknownHostException -> "Cannot connect to weather service. Check your internet conncetion"
                                is java.net.SocketTimeoutException -> "Connection to weather service timed out. Please try again later."
                                is retrofit2.HttpException -> "Weather service error: HTTP ${e.code()}"
                                else -> "Error fetching weather data: ${e.message}"
                            }

                            onError(errorMessage)
                            onWeatherData(null)
                        }
                    }
                }
            },
            onSettingsError = { exception ->
                Log.e("WeatherRepository", "Location settings error", exception)
                onError("Location settings error: ${exception.message}")
                onWeatherData(null)
            }
        )
    }
}