package com.example.helpwheel.ui.trip

import android.content.Context.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpwheel.App
import com.example.helpwheel.ui.trip.last_ride_fragments.cost.CostLastRideFragment
import com.example.helpwheel.ui.trip.last_ride_fragments.distance.DistanceLastRideFragment
import com.example.helpwheel.ui.trip.last_ride_fragments.ecology.EcologyLastRideFragment
import com.example.helpwheel.ui.trip.last_ride_fragments.spent_fuel.SpentFuelLastRideFragment
import com.example.helpwheel.ui.trip.new_ride_fragments.cost.CostNewRideFragment
import com.example.helpwheel.ui.trip.new_ride_fragments.distance.DistanceNewRideFragment
import com.example.helpwheel.ui.trip.new_ride_fragments.ecology.EcologyNewRideFragment
import com.example.helpwheel.ui.trip.new_ride_fragments.remains_fuel.RemainsFuelNewRideFragment
import com.example.helpwheel.ui.trip.new_ride_fragments.spendFuel.SpendFuelNewRideFragment
import com.example.helpwheel.utils.*

class TripViewModel : ViewModel() {
    private val fuelInTank = MutableLiveData<String>()
    private val fuelStats = App.instance.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
    private val editor = fuelStats.edit()
    init {
        setDefaultValue()
    }

    private fun setDefaultValue() {
        fuelInTank.postValue(
            fuelStats.getFloat(
                FUEL_LEVEL,
                fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f)
            ).toString()
        )
    }

    fun inflateNewRideFragments(): ArrayList<Fragment>{
        val newRideFragments = ArrayList<Fragment>()
        newRideFragments.add(DistanceNewRideFragment())
        newRideFragments.add(RemainsFuelNewRideFragment())
        newRideFragments.add(CostNewRideFragment())
        newRideFragments.add(SpendFuelNewRideFragment())
        newRideFragments.add(EcologyNewRideFragment())
        return newRideFragments
    }

    fun inflateLastRideFragments(): ArrayList<Fragment> {
        val lastRideFragments = ArrayList<Fragment>()
        lastRideFragments.add(DistanceLastRideFragment())
        lastRideFragments.add(CostLastRideFragment())
        lastRideFragments.add(SpentFuelLastRideFragment())
        lastRideFragments.add(EcologyLastRideFragment())
        return lastRideFragments
    }

    fun setFuelInTankFromEditText(fuelLevel: Float){
        fuelInTank.postValue(fuelLevel.toString())
    }

    fun setFuelInTank() {
        val fuelLevel = SharedPreferencesHolder.fuelInTank()
        if (fuelLevel > 0.0f)
            fuelInTank.postValue(fuelLevel.toString())
        else {
            fuelInTank.postValue("0.0")
            editor.putFloat(FUEL_LEVEL, 0.0f).apply()
        }
    }

    fun getFuelInTank(): LiveData<String> {
        return fuelInTank
    }

}