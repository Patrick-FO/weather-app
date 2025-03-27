package com.example.weatherapp

import com.google.gson.annotations.SerializedName

//@TODO Reformat data classes later to follow DRY code principal? (Refactor)

//Root data class
data class WeatherResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val current: CurrentWeather,
    val hourly: List<HourlyWeather>,
    val daily: List<DailyWeather>,
    val minutely: List<MinutelyForecast>?
)

data class CurrentWeather(
    val dt: Long,                   // Time of data calculation (unix timestamp)
    val sunrise: Long,              // Sunrise time (unix timestamp)
    val sunset: Long,               // Sunset time (unix timestamp)
    val temp: Double,               // Temperature in Kelvin (or in Celsius if units=metric)
    val feels_like: Double,         // "Feels like" temperature
    val pressure: Int,              // Atmospheric pressure in hPa
    val humidity: Int,              // Humidity percentage
    val dew_point: Double,          // Dew point temperature
    val uvi: Double,                // UV index
    val clouds: Int,                // Cloudiness percentage
    val visibility: Int,            // Visibility in meters
    val wind_speed: Double,         // Wind speed
    val wind_deg: Int,              // Wind direction in degrees
    val weather: List<WeatherDescription>, // Weather condition details
)

data class HourlyWeather(
    val dt: Long,                   // Time of data calculation (unix timestamp)
    val temp: Double,               // Temperature in Kelvin (or in Celsius if units=metric)
    val feels_like: Double,         // "Feels like" temperature
    val pressure: Int,              // Atmospheric pressure in hPa
    val humidity: Int,              // Humidity percentage
    val dew_point: Double,          // Dew point temperature
    val uvi: Double,                // UV index
    val clouds: Int,                // Cloudiness percentage
    val visibility: Int,            // Visibility in meters
    val wind_speed: Double,         // Wind speed
    val wind_deg: Int,              // Wind direction in degrees
    val wind_gust: Double,
    val weather: List<WeatherDescription>, // Weather condition details
    val pop: Double
)

data class DailyWeather(
    val dt: Long,                   // Time of data calculation (unix timestamp)
    val sunrise: Long,              // Sunrise time (unix timestamp)
    val sunset: Long,               // Sunset time (unix timestamp)
    val moonrise: Long,
    val moonset: Long,
    val moon_phase: Double,
    val summary: String,
    val temp: Temp,
    val feels_like: FeelsLike,
    val pressure: Int,              // Atmospheric pressure in hPa
    val humidity: Int,              // Humidity percentage
    val dew_point: Double,          // Dew point temperature
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double,
    val weather: List<WeatherDescription>,
    val clouds: Int,
    val pop: Double,
    val uvi: Double
)

//Comes as a list, and is accessed by CurrentWeather
data class WeatherDescription(
    val id: Int,                    // Weather condition ID
    val main: String,               // Group of weather parameters (Rain, Snow, etc.)
    val description: String,        // Weather condition within the group
    val icon: String                // Weather icon ID
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double,
)

data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

//Minutely forecast
data class MinutelyForecast(
    val dt: Long,                  // Time of forecast (unix timestamp)
    val precipitation: Double      // Precipitation volume in mm
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)
