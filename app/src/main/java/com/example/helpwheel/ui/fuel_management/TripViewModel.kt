package com.example.helpwheel.ui.fuel_management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TripViewModel: ViewModel (){
    private val distanceLastRide = MutableLiveData<String>()
    private val distanceNewRide = MutableLiveData<String>()
    private val costLastRide = MutableLiveData<String>()
    private val costNewRide = MutableLiveData<String>()
    private val remainsFuelNewRide = MutableLiveData<String>()
    private val spentFuelLastRide = MutableLiveData<String>()
    private val spendFuelNewRide = MutableLiveData<String>()

    fun setDistanceLastRide(distance: Float){
        distanceLastRide.value = distance.toString()
    }

    fun setDistanceNewRide(distance: Float){
        distanceNewRide.value = distance.toString()
    }

    fun setCostLastRide(cost: Float){
        costLastRide.value = cost.toString()
    }

    fun setCostNewRide(cost: Float){
        costNewRide.value = cost.toString()
    }

    fun setRemainsFuelNewRide(fuel: Float){
        remainsFuelNewRide.value = fuel.toString()
    }

    fun setSpentFuelLastRide(fuel: Float){
        spentFuelLastRide.value = fuel.toString()
    }

    fun setSpendFuelNewRide(fuel: Float){
        spendFuelNewRide.value = fuel.toString()
    }

    fun getDistanceLastRide(): LiveData<String>{
        return distanceLastRide
    }

    fun getDistanceNewRide(): LiveData<String>{
        return distanceNewRide
    }

    fun getCostLastRide(): LiveData<String>{
        return costLastRide
    }

    fun getCostNewRide(): LiveData<String>{
        return costNewRide
    }

    fun getRemainsFuelNewRide(): LiveData<String>{
        return remainsFuelNewRide
    }

    fun getSpentFuelLastRide(): LiveData<String>{
        return spentFuelLastRide
    }

    fun getSpendFuelNewRide(): LiveData<String>{
        return spendFuelNewRide
    }


}