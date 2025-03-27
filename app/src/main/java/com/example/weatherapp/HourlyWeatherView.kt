package com.example.weatherapp

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HourlyWeatherView(activity: Activity, viewModel: WeatherViewModel) {
    val weatherData by viewModel.weatherData.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val error by viewModel.error.observeAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isLoading -> Text("Loading...")
            error != null -> Text("Error $error")
            weatherData != null -> {
                Text(text = "During the day", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
                LazyColumn {
                    items(
                        items = weatherData!!.hourly,
                        key = { it.dt }
                    ) { hourlyItem ->
                        Spacer(modifier = Modifier.height(32.dp))
                        HourlyWeatherCard(hourlyItem)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HourlyWeatherCard(hourlyWeatherObject: HourlyWeather) {
    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val format = SimpleDateFormat("h a", Locale.getDefault())

        return format.format(date)
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://openweathermap.org/img/wn/${hourlyWeatherObject.weather[0].icon}@2x.png")
                .crossfade(true)
                .build(),
            //Whats a non null asserted call? ("!!" on weatherData when fetching weather list and getting its index), Why are we using it instead of "?"?
            contentDescription = hourlyWeatherObject.weather[0].description,
            modifier = Modifier.size(64.dp)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = formatTimestamp(hourlyWeatherObject.dt))
            Text(text = "Weather: ${hourlyWeatherObject.weather[0].main}")
            Text(text = "Temperature: ${hourlyWeatherObject.temp.toInt()}°C")
        }
    }
}