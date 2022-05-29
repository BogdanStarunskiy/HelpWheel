package com.example.helpwheel.ui.trip

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpwheel.App
import com.example.helpwheel.utils.*

class TripViewModel: ViewModel (){
    private val fuelInTank = MutableLiveData<String>()
    private val fuelStats = App.instance.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)


    fun setDefaultValue(){
        fuelInTank.postValue(fuelStats.getFloat(FUEL_LEVEL, fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f)).toString())
    }

    fun getFuelInTank():LiveData<String>{
        return fuelInTank
    }

    fun setFuelInTank() {
        fuelInTank.postValue(SharedPreferencesHolder.fuelInTank().toString())
    }
}