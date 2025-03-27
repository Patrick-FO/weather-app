package com.example.weatherapp

import WeatherPager
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//TODO Look into how this works in relation to the pager.
@Composable
fun Navigation(activity: Activity, viewModel: WeatherViewModel, navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "weather_pager"
    ) {
        // Main pager screen that handles the swipe navigation between weather views
        composable("weather_pager") {
            WeatherPager(activity, viewModel, navController)
        }

        // Keep these routes for potential deep linking or navigation from other parts of the app
        // These won't be used directly by the pager navigation
        composable(Screen.CurrentWeatherScreen.route) {
            CurrentWeatherView(viewModel)
        }
        composable(Screen.HourlyWeatherScreen.route) {
            HourlyWeatherView(activity, viewModel)
        }
        composable(Screen.DailyWeatherScreen.route) {
            DailyWeatherView(activity, viewModel)
        }
    }
}