package com.example.helpwheel.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.ui.trip.TripViewModel
import com.example.helpwheel.ui.trip.last_ride_fragments.cost.CostLastRideViewModel
import com.example.helpwheel.ui.trip.last_ride_fragments.distance.DistanceLastRideViewModel
import com.example.helpwheel.ui.trip.last_ride_fragments.ecology.EcologyLastRideViewModel
import com.example.helpwheel.ui.trip.last_ride_fragments.spent_fuel.SpentFuelLastRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.cost.CostNewRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.distance.DistanceNewRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.ecology.EcologyNewRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.remains_fuel.RemainsFuelNewRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.spendFuel.SpendFuelNewRideViewModel

object ViewModels {
    lateinit var remainsFuelNewRideViewModel: RemainsFuelNewRideViewModel
    lateinit var tripViewModel: TripViewModel
    lateinit var costNewRideViewModel: CostNewRideViewModel
    lateinit var distanceNewRideViewModel: DistanceNewRideViewModel
    lateinit var ecologyNewRideViewModel: EcologyNewRideViewModel
    lateinit var spendFuelNewRideViewModel: SpendFuelNewRideViewModel
    lateinit var costLastRideViewModel: CostLastRideViewModel
    lateinit var distanceLastRideViewModel: DistanceLastRideViewModel
    lateinit var ecologyLastRideViewModel: EcologyLastRideViewModel
    lateinit var spentFuelLastRideViewModel: SpentFuelLastRideViewModel

    fun initViewModels(fragmentActivity: FragmentActivity) {
            remainsFuelNewRideViewModel =
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
            costNewRideViewModel =
                ViewModelProvider(fragmentActivity)[CostNewRideViewModel::class.java]
            distanceNewRideViewModel =
                ViewModelProvider(fragmentActivity)[DistanceNewRideViewModel::class.java]
            ecologyNewRideViewModel =
                ViewModelProvider(fragmentActivity)[EcologyNewRideViewModel::class.java]
            remainsFuelNewRideViewModel =
                ViewModelProvider(fragmentActivity)[RemainsFuelNewRideViewModel::class.java]
            spendFuelNewRideViewModel =
                ViewModelProvider(fragmentActivity)[SpendFuelNewRideViewModel::class.java]
    }
}