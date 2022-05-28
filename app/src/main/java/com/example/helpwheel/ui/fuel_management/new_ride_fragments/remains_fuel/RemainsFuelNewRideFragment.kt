package com.example.helpwheel.ui.fuel_management.new_ride_fragments.remains_fuel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.databinding.FragmentFuelInTankNewRideBinding
import com.example.helpwheel.ui.fuel_management.TripViewModel

class RemainsFuelNewRideFragment: Fragment() {
    lateinit var binding: FragmentFuelInTankNewRideBinding
    private lateinit var tripViewModel: TripViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFuelInTankNewRideBinding.inflate(inflater, container, false)
        tripViewModel = ViewModelProvider(requireActivity())[TripViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tripViewModel.getRemainsFuelNewRide().observe(viewLifecycleOwner) {
            binding.remainsInTheTank.text = it
        }
    }
}