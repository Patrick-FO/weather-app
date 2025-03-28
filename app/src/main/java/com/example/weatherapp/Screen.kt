package com.example.weatherapp

//TODO Try to understand what this class is actually doing.
sealed class Screen(val route: String) {
    object CurrentWeatherScreen: Screen("current_weather_screen")
    object HourlyWeatherScreen: Screen("hourly_weather_screen")
    object DailyWeatherScreen: Screen("daily_weather_screen")
    object DailyWeatherDetailsScreen: Screen("daily_weather_details_screen")
}