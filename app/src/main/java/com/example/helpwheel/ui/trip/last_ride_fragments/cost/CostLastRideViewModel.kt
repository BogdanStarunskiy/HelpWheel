package com.example.helpwheel.ui.trip.last_ride_fragments.cost

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpwheel.App
import com.example.helpwheel.utils.APP_PREFERENCES
import com.example.helpwheel.utils.COST_LAST_RIDE
import com.example.helpwheel.utils.SharedPreferencesHolder

class CostLastRideViewModel: ViewModel() {
    private val costLastRide = MutableLiveData<String>()
    private val fuelStats = App.instance.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

    init{
        setDefaultCost()
    }

    private fun setDefaultCost(){
        costLastRide.postValue(fuelStats.getFloat(COST_LAST_RIDE, 0.0f).toString())
    }

    fun setCostLastRide(cost: Float){
        costLastRide.postValue(SharedPreferencesHolder.priceLastRide(cost).toString())
    }

    fun getCostLastRide(): LiveData<String> {
        return costLastRide
    }
}