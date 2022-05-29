package com.example.helpwheel.ui.trip.bottom_sheet_fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentNewRideBottomSheetBinding
import com.example.helpwheel.ui.trip.inerface.BottomSheetCallBack
import com.example.helpwheel.ui.trip.new_ride_fragments.cost.CostNewRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.distance.DistanceNewRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.ecology.EcologyNewRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.remains_fuel.RemainsFuelNewRideViewModel
import com.example.helpwheel.ui.trip.new_ride_fragments.spendFuel.SpendFuelNewRideViewModel
import com.example.helpwheel.utils.APP_PREFERENCES
import com.example.helpwheel.utils.COST_NEW_RIDE
import com.example.helpwheel.utils.DISTANCE_NEW_RIDE
import com.example.helpwheel.utils.PRE_COST_NEW_RIDE

class NewRideBottomSheetFragment(private val callBack: BottomSheetCallBack) : Fragment() {
    private lateinit var binding: FragmentNewRideBottomSheetBinding
    private lateinit var fuelStats: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var costNewRideViewModel: CostNewRideViewModel
    private lateinit var distanceNewRideViewModel: DistanceNewRideViewModel
    private lateinit var ecologyNewRideViewModel: EcologyNewRideViewModel
    private lateinit var remainsFuelNewRideViewModel: RemainsFuelNewRideViewModel
    private lateinit var spendFuelNewRideViewModel: SpendFuelNewRideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewRideBottomSheetBinding.inflate(inflater, container, false)
        initViewModels()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        editor = fuelStats.edit()
        binding.submitBtnFuel.setOnClickListener {
            val distance = binding.distanceNewRideText.text.toString()
            val price = binding.priceNewRideText.text.toString()
            if (distance.isNotEmpty() && price.isNotEmpty()) {
                editor.putFloat(DISTANCE_NEW_RIDE, distance.toFloat())
                editor.putFloat(PRE_COST_NEW_RIDE, price.toFloat())
                editor.apply()
                distanceNewRideViewModel.setDistanceNewRide(distance.toFloat())
                costNewRideViewModel.setCostNewRide()
                callMethods()
                callBack.dismissBottomSheet()
            } else if (distance.isNotEmpty()) {
                editor.putFloat(DISTANCE_NEW_RIDE, distance.toFloat())
                editor.putFloat(COST_NEW_RIDE, 0.0f)
                editor.apply()
                distanceNewRideViewModel.setDistanceNewRide(distance.toFloat())
                callMethods()
                callBack.dismissBottomSheet()
            } else if (price.isNotEmpty()) {
                binding.distanceNewRideEditText.error = getString(R.string.edit_text_odometer_error)
            } else
                callBack.dismissBottomSheet()
        }
    }

    private fun initViewModels() {
        costNewRideViewModel =
            ViewModelProvider(requireActivity())[CostNewRideViewModel::class.java]
        distanceNewRideViewModel =
            ViewModelProvider(requireActivity())[DistanceNewRideViewModel::class.java]
        ecologyNewRideViewModel =
            ViewModelProvider(requireActivity())[EcologyNewRideViewModel::class.java]
        remainsFuelNewRideViewModel =
            ViewModelProvider(requireActivity())[RemainsFuelNewRideViewModel::class.java]
        spendFuelNewRideViewModel =
            ViewModelProvider(requireActivity())[SpendFuelNewRideViewModel::class.java]
    }

    private fun callMethods() {
        spendFuelNewRideViewModel.setSpendFuelNewRide()
        ecologyNewRideViewModel.setCarEmissions()
        remainsFuelNewRideViewModel.setRemainsFuelNewRide()
    }


}