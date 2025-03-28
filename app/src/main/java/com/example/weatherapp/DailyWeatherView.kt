package com.example.weatherapp

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DailyWeatherView(activity: Activity, viewModel: WeatherViewModel, navController: NavHostController) {
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
                Text(text = "During the week", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
                LazyColumn {
                    items(
                        items = weatherData!!.daily,
                        key = { it.dt }
                    ) { dailyItem ->
                        val index = weatherData!!.daily.indexOf(dailyItem)
                        Spacer(modifier = Modifier.height(32.dp))
                        DailyWeatherCard(
                            dailyWeatherObject = dailyItem,
                            onClick = {
                                navController.navigate("${Screen.DailyWeatherDetailsScreen.route}/$index")
                            },
                            navController = navController
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                Text("Coordinates, lat: ${weatherData?.lat}, lon: ${weatherData?.lon}")
            }
        }
    }
}

@Composable
fun DailyWeatherCard(dailyWeatherObject: DailyWeather, onClick: () -> Unit, navController: NavHostController) {

    fun isToday(date: Date): Boolean {
        val today = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        calendar.time = date

        return (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR))
    }

    fun formatTimestampToWeekday(
        timestamp: Long,
        abbreviated: Boolean = false,
        useToday: Boolean = true
    ): String {
        val date = Date(timestamp * 1000)

        if(useToday && isToday(date)) {
            return "Today"
        }

        val pattern = if (abbreviated) "EEE" else "EEEE"
        val format = SimpleDateFormat(pattern, Locale.getDefault())

        return format.format(date)
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable { onClick() }) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://openweathermap.org/img/wn/${dailyWeatherObject.weather[0].icon}@2x.png")
                .crossfade(true)
                .build(),
            //Whats a non null asserted call? ("!!" on weatherData when fetching weather list and getting its index), Why are we using it instead of "?"?
            contentDescription = dailyWeatherObject.weather[0].description,
            modifier = Modifier.size(64.dp)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = formatTimestampToWeekday(dailyWeatherObject.dt))
            Text(text = "Weather: ${dailyWeatherObject.weather[0].main}")
            Text(text = "Max temperature: ${dailyWeatherObject.temp.max}°C")
            Text(text = "Min temperature: ${dailyWeatherObject.temp.min}°C")
        }
    }
}

//TODO There was a slight bit of prompt engineering here, so take a look at this and see whats going on
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyWeatherDetailsView(viewModel: WeatherViewModel, navController: NavHostController, dayIndex: Int) {
    val weatherData by viewModel.weatherData.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            weatherData?.daily?.getOrNull(dayIndex)?.let { dailyWeather ->
                // Now we're using the passed dayIndex to get the correct weather data
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://openweathermap.org/img/wn/${dailyWeather.weather[0].icon}@2x.png")
                        .crossfade(true)
                        .build(),
                    contentDescription = dailyWeather.weather[0].description,
                    modifier = Modifier.size(156.dp)
                )
                Text(
                    text = formatTimestampToWeekday(dailyWeather.dt),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Weather: ${dailyWeather.weather[0].main}")
                Text(text = "Max temperature: ${dailyWeather.temp.max}°C")
                Text(text = "Min temperature: ${dailyWeather.temp.min}°C")
                Text(text = "Morning temperature: ${dailyWeather.temp.morn}°C")
                Text(text = "Day temperature: ${dailyWeather.temp.day}°C")
                Text(text = "Evening temperature: ${dailyWeather.temp.eve}°C")
                Text(text = "Night temperature: ${dailyWeather.temp.night}°C")
                Text("Pressure: ${dailyWeather.pressure} hPa")
                Text("Humidity: ${dailyWeather.humidity}%")
                Text("UV index: ${dailyWeather.uvi.toInt()}")
            } ?: run {
                Text("Weather data not available")
            }
        }
    }
}

