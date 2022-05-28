package com.example.helpwheel.ui.fuel_management.last_ride_fragments.cost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CostLastRideViewModel: ViewModel() {
    private val costLastRide = MutableLiveData<String>()

    fun setCostLastRide(cost: Float){
        costLastRide.postValue(cost.toString())
    }

    fun getCostLastRide(): LiveData<String> {
        return costLastRide
    }
}