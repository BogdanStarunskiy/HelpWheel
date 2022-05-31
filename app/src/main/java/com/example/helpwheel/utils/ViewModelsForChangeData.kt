package com.example.helpwheel.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.ui.trip.TripViewModel
import com.example.helpwheel.ui.trip.last_ride_fragments.cost.CostLastRideViewModel
import com.example.helpwheel.ui.trip.last_ride_fragments.spent_fuel.SpentFuelLastRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.cost.CostNewRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.remains_fuel.RemainsFuelNewRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.spendFuel.SpendFuelNewRideViewModel

object ViewModelsForChangeData {
    lateinit var remainsFuelNewRideViewModel: RemainsFuelNewRideViewModel
    lateinit var tripViewModel: TripViewModel
    lateinit var costNewRideViewModel: CostNewRideViewModel
    lateinit var spendFuelNewRideViewModel: SpendFuelNewRideViewModel
    lateinit var costLastRideViewModel: CostLastRideViewModel
    lateinit var spentFuelLastRideViewModel: SpentFuelLastRideViewModel

    fun initViewModels(fragmentActivity: FragmentActivity) {
        remainsFuelNewRideViewModel =
            ViewModelProvider(fragmentActivity)[RemainsFuelNewRideViewModel::class.java]
        tripViewModel = ViewModelProvider(fragmentActivity)[TripViewModel::class.java]
        costLastRideViewModel =
            ViewModelProvider(fragmentActivity)[CostLastRideViewModel::class.java]
        costNewRideViewModel =
            ViewModelProvider(fragmentActivity)[CostNewRideViewModel::class.java]
        remainsFuelNewRideViewModel =
            ViewModelProvider(fragmentActivity)[RemainsFuelNewRideViewModel::class.java]
        spendFuelNewRideViewModel =
            ViewModelProvider(fragmentActivity)[SpendFuelNewRideViewModel::class.java]
        spentFuelLastRideViewModel =
            ViewModelProvider(fragmentActivity)[SpentFuelLastRideViewModel::class.java]
    }
}