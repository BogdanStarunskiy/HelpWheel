package com.example.helpwheel.ui.trip.new_ride_fragments.cost

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpwheel.App
import com.example.helpwheel.utils.APP_PREFERENCES
import com.example.helpwheel.utils.COST_NEW_RIDE
import com.example.helpwheel.utils.SharedPreferencesHolder

class CostNewRideViewModel: ViewModel() {
    private val costNewRide = MutableLiveData<String>()
    private val fuelStats = App.instance.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

    fun setDefaultCost(){
        costNewRide.postValue(fuelStats.getFloat(COST_NEW_RIDE, 0.0f).toString())
    }

    fun setCostNewRide(){
        costNewRide.postValue(SharedPreferencesHolder.priceNewRide().toString())
    }

    fun getCostNewRide(): LiveData<String> {
        return costNewRide
    }
}