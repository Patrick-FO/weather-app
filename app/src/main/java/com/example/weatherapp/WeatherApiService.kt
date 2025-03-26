package com.example.weatherapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/3.0/")
    .addConverterFactory(GsonConverterFactory.create()).build()


interface WeatherApiService {
    @GET("onecall?lat=33.44&lon=-94.04&appid=40f4e3432085baaf45c4c9611eedf995")
    suspend fun weatherResponse(): CurrentWeather
}