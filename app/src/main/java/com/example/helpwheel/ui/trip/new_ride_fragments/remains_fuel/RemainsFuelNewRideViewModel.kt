package com.example.helpwheel.ui.trip.new_ride_fragments.remains_fuel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpwheel.App
import com.example.helpwheel.utils.*

class RemainsFuelNewRideViewModel: ViewModel() {
    private val remainsFuel = MutableLiveData<String>()
    private val fuelStats = App.instance.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    fun setDefaultRemainsFuel(){
        remainsFuel.postValue(fuelStats.getFloat(REMAINS_FUEL, fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f)).toString())
    }
    fun setRemainsFuelNewRide(){
        remainsFuel.postValue(SharedPreferencesHolder.remainsFuel().toString())
    }

    fun getRemainsFuelNewRide(): LiveData<String> {
        return remainsFuel
    }
}