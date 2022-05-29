package com.example.helpwheel.ui.trip.new_ride_fragments.spendFuel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpwheel.App
import com.example.helpwheel.utils.APP_PREFERENCES
import com.example.helpwheel.utils.SharedPreferencesHolder
import com.example.helpwheel.utils.WILL_BE_USED_FUEL

class SpendFuelNewRideViewModel: ViewModel() {
    private val spendFuelNewRide = MutableLiveData<String>()
    private val fuelStats = App.instance.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    fun setDefaultSpendFuel(){
        spendFuelNewRide.postValue(fuelStats.getFloat(WILL_BE_USED_FUEL, 0.0f).toString())
    }
    fun setSpendFuelNewRide(){
        spendFuelNewRide.postValue(SharedPreferencesHolder.countSpendFuelNewRide().toString())
    }

    fun getSpendFuelNewRide(): LiveData<String> {
        return spendFuelNewRide
    }

}