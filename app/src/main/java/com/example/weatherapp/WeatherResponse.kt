package com.example.weatherapp

data class CurrentWeather(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Long,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Long,
    val uvi: Int,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Int,
    val wind_deg: Int,
    val weather: Weather
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)