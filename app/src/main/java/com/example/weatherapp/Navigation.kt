package com.example.weatherapp

import WeatherPager
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

//TODO Look into how this works in relation to the pager.
//TODO Take a look at how the navigation for the daily details screen works
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
            DailyWeatherView(activity, viewModel, navController)
        }
        composable(
            route = "${Screen.DailyWeatherDetailsScreen.route}/{dayIndex}",
            arguments = listOf(navArgument("dayIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val dayIndex = backStackEntry.arguments?.getInt("dayIndex") ?: 0
            DailyWeatherDetailsView(viewModel, navController, dayIndex)
        }
    }
}