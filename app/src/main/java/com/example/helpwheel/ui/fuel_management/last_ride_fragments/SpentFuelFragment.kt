package com.example.helpwheel.ui.fuel_management.last_ride_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.databinding.FragmentSpentFuelBinding
import com.example.helpwheel.ui.fuel_management.TripViewModel

class SpentFuelFragment: Fragment() {
    lateinit var binding: FragmentSpentFuelBinding
    private lateinit var tripViewModel: TripViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpentFuelBinding.inflate(inflater, container, false)
        tripViewModel = ViewModelProvider(requireActivity())[TripViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tripViewModel.getSpentFuelLastRide().observe(viewLifecycleOwner) {
            binding.spentFuel.text = it
        }
    }
}