package com.example.helpwheel.ui.fuel_management.last_ride_fragments.distance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DistanceLastRideViewModel: ViewModel() {
    private val distanceLastRide = MutableLiveData<String>()

    fun setDistanceLastRide(distance: Float){
        distanceLastRide.postValue(distance.toString())
    }

    fun getDistanceLastRide(): LiveData<String> {
        return distanceLastRide
    }
}