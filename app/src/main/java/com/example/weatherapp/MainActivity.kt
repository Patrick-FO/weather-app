package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.weatherapp.ui.theme.WeatherAppTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    //private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/3.0/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val weatherApiService = retrofit.create(WeatherApiService::class.java)

        val locationRepository = LocationRepository(
            //fusedLocationClient = fusedLocationClient,
            context = this
        )

        val weatherRepository = WeatherRepository(
            weatherApiService = weatherApiService,
            locationRepository = locationRepository,
            apiKey = "40f4e3432085baaf45c4c9611eedf995"
        )

        weatherViewModel = WeatherViewModel(weatherRepository)

        setContent {
            WeatherAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Navigation(activity = this, viewModel = weatherViewModel)
                }
            }
        }
    }
}
