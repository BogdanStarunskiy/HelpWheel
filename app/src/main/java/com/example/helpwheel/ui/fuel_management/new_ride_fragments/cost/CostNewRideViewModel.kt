package com.example.helpwheel.ui.fuel_management.new_ride_fragments.cost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CostNewRideViewModel: ViewModel() {
    private val costNewRide = MutableLiveData<String>()

    fun setCostNewRide(cost: Float){
        costNewRide.postValue(cost.toString())
    }

    fun getCostNewRide(): LiveData<String> {
        return costNewRide
    }
}