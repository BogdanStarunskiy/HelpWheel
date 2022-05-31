package com.example.helpwheel.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.ui.trip.TripViewModel
import com.example.helpwheel.ui.trip.last_ride_fragments.cost.CostLastRideViewModel
import com.example.helpwheel.ui.trip.last_ride_fragments.distance.DistanceLastRideViewModel
import com.example.helpwheel.ui.trip.last_ride_fragments.ecology.EcologyLastRideViewModel
import com.example.helpwheel.ui.trip.last_ride_fragments.spent_fuel.SpentFuelLastRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.remains_fuel.RemainsFuelNewRideViewModel

object LastRideViewModels {
    lateinit var remainsFuelViewModel: RemainsFuelNewRideViewModel
    lateinit var tripViewModel: TripViewModel
    lateinit var costLastRideViewModel: CostLastRideViewModel
    lateinit var distanceLastRideViewModel: DistanceLastRideViewModel
    lateinit var ecologyLastRideViewModel: EcologyLastRideViewModel
    lateinit var spentFuelLastRideViewModel: SpentFuelLastRideViewModel

    fun initViewModelsLastRide(fragmentActivity: FragmentActivity) {
        remainsFuelViewModel =
            ViewModelProvider(fragmentActivity)[RemainsFuelNewRideViewModel::class.java]
        tripViewModel = ViewModelProvider(fragmentActivity)[TripViewModel::class.java]
        costLastRideViewModel =
            ViewModelProvider(fragmentActivity)[CostLastRideViewModel::class.java]
        distanceLastRideViewModel =
            ViewModelProvider(fragmentActivity)[DistanceLastRideViewModel::class.java]
        ecologyLastRideViewModel =
            ViewModelProvider(fragmentActivity)[EcologyLastRideViewModel::class.java]
        spentFuelLastRideViewModel =
            ViewModelProvider(fragmentActivity)[SpentFuelLastRideViewModel::class.java]
    }
}