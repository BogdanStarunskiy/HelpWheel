package com.example.helpwheel.ui.dashboard.service

import com.example.helpwheel.ui.dashboard.model.WeatherModel
import com.example.helpwheel.utils.BASE_URL
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather?")
    fun getCurrentWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") key: String,
        @Query("units") units: String,
        @Query("lang") language: String
    ): Call<WeatherModel>

    @GET("weather?")
    fun getCurrentWeatherByCity(
        @Query("q") city: String,
        @Query("appid") key: String,
        @Query("units") units: String,
        @Query("lang") language: String
    ): Call<WeatherModel>

    companion object {
        fun create(): WeatherService {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(WeatherService::class.java)
        }
    }
}