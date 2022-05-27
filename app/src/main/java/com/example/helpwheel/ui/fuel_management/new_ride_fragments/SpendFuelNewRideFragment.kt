package com.example.helpwheel.ui.fuel_management.new_ride_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.databinding.FragmentSpendFuelNewRideBinding
import com.example.helpwheel.ui.fuel_management.TripViewModel

class SpendFuelNewRideFragment : Fragment() {
    lateinit var binding: FragmentSpendFuelNewRideBinding
    private lateinit var tripViewModel: TripViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpendFuelNewRideBinding.inflate(inflater, container, false)
        tripViewModel = ViewModelProvider(requireActivity())[TripViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tripViewModel.getSpendFuelNewRide()
            .observe(viewLifecycleOwner) { binding.spendFuelNewRide.text = it }
    }

}