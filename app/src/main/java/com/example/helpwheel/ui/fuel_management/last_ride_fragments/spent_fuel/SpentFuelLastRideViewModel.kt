package com.example.helpwheel.ui.fuel_management.last_ride_fragments.spent_fuel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpentFuelLastRideViewModel: ViewModel() {
    private val spentFuelLastRide = MutableLiveData<String>()

    fun setSpentFuelLastRide(fuel: Float){
        spentFuelLastRide.postValue(fuel.toString())
    }

    fun getSpentFuelLastRide(): LiveData<String> {
        return spentFuelLastRide
    }
}