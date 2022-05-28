package com.example.helpwheel.ui.fuel_management.new_ride_fragments.spendFuel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpendFuelNewRideViewModel: ViewModel() {
    private val spendFuelNewRide = MutableLiveData<String>()

    fun setSpendFuelNewRide(fuel: Float){
        spendFuelNewRide.postValue(fuel.toString())
    }

    fun getSpendFuelNewRide(): LiveData<String> {
        return spendFuelNewRide
    }

}