package com.example.helpwheel.ui.trip

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpwheel.App
import com.example.helpwheel.utils.*

class TripViewModel : ViewModel() {
    private val fuelInTank = MutableLiveData<String>()
    private val fuelStats = App.instance.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
    private val editor = fuelStats.edit()
    init {
        setDefaultValue()
    }
    private fun setDefaultValue() {
        fuelInTank.postValue(
            fuelStats.getFloat(
                FUEL_LEVEL,
                fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f)
            ).toString()
        )
    }

    fun setFuelInTankFromEditText(fuelLevel: Float){
        fuelInTank.postValue(fuelLevel.toString())
    }

    fun setFuelInTank() {
        if (SharedPreferencesHolder.fuelInTank() > 0.0f)
            fuelInTank.postValue(SharedPreferencesHolder.fuelInTank().toString())
        else {
            fuelInTank.postValue("0.0")
            editor.putFloat(FUEL_LEVEL, 0.0f).apply()
        }
    }

    fun getFuelInTank(): LiveData<String> {
        return fuelInTank
    }

}