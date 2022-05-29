package com.example.helpwheel.ui.trip.new_ride_fragments.distance

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpwheel.App
import com.example.helpwheel.utils.APP_PREFERENCES
import com.example.helpwheel.utils.COST_NEW_RIDE
import com.example.helpwheel.utils.DISTANCE_NEW_RIDE

class DistanceNewRideViewModel: ViewModel() {
    private val distanceNewRide = MutableLiveData<String>()
    private val fuelStats = App.instance.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    fun setDefaultDistance(){
        distanceNewRide.postValue(fuelStats.getFloat(DISTANCE_NEW_RIDE, 0.0f).toString())
    }
    fun setDistanceNewRide(distance: Float){
        distanceNewRide.postValue(distance.toString())
    }

    fun getDistanceNewRide(): LiveData<String> {
        return distanceNewRide
    }
}