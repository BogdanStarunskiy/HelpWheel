package com.example.helpwheel.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpwheel.ui.dashboard.model.WeatherModel
import com.example.helpwheel.ui.dashboard.service.WeatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {

    private val temperature = MutableLiveData<String>()
    private val wind = MutableLiveData<String>()
    private val description = MutableLiveData<String>()
    private val city = MutableLiveData<String>()
    private val isGpsTurnedOn = MutableLiveData<Boolean>()
    private val isPermissionGranted = MutableLiveData<Boolean>()

    fun setIsPermissionGranted(isGranted: Boolean) {
        isPermissionGranted.value = isGranted
    }

    fun setIsGpsTurnedOn(isTurnedOn: Boolean) {
        isGpsTurnedOn.value = isTurnedOn
    }

    fun setCity(userCity: String) {
        city.value = userCity
    }

    fun getWeatherDataAutomatically(latitude: Double, longitude: Double, key: String) {
        val weatherService = WeatherService.create()
            .getCurrentWeatherByCoordinates(latitude, longitude, key, "metric", "en")
        getWeatherFromServer(weatherService)
    }

    fun getWeatherDataManualInput(userCity: String, key: String) {
        val weatherService =
            WeatherService.create().getCurrentWeatherByCity(userCity, key, "metric", "en")
        getWeatherFromServer(weatherService)
        city.value = userCity
    }

    private fun getWeatherFromServer(weatherService: Call<WeatherModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherService.enqueue(object : Callback<WeatherModel> {
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    val currentWeather = response.body()
                    if (response.code() == 200) {
                        temperature.value = currentWeather!!.main.temp.toInt().toString()
                        wind.value = currentWeather.wind.speed.toInt().toString()
                        description.value = currentWeather.weather[0].description
                    } else
                        postErrorValues()
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                        postErrorValues()
                }
            })
        }
    }

    private fun postErrorValues(){
        temperature.value = "error"
        wind.value = "error"
        description.value = "error"
    }

    fun getIsPermissionGranted(): LiveData<Boolean> {
        return isPermissionGranted
    }

    fun getTemperature(): LiveData<String> {
        return temperature
    }

    fun getIsGPSTurnedOn(): LiveData<Boolean> {
        return isGpsTurnedOn
    }

    fun getWind(): LiveData<String> {
        return wind
    }

    fun getCity(): LiveData<String> {
        return city
    }

    fun getDescription(): LiveData<String> {
        return description
    }
}