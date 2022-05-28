package com.example.helpwheel.ui.fuel_management.new_ride_fragments.distance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DistanceNewRideViewModel: ViewModel() {
    private val distanceNewRide = MutableLiveData<String>()

    fun setDistanceNewRide(distance: Float){
        distanceNewRide.postValue(distance.toString())
    }

    fun getDistanceNewRide(): LiveData<String> {
        return distanceNewRide
    }
}