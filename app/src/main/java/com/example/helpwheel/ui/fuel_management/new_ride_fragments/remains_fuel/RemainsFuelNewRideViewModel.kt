package com.example.helpwheel.ui.fuel_management.new_ride_fragments.remains_fuel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RemainsFuelNewRideViewModel: ViewModel() {
    private val remainsFuelNewRide = MutableLiveData<String>()

    fun setRemainsFuelNewRide(fuel: Float){
        remainsFuelNewRide.postValue(fuel.toString())
    }

    fun getRemainsFuelNewRide(): LiveData<String> {
        return remainsFuelNewRide
    }
}