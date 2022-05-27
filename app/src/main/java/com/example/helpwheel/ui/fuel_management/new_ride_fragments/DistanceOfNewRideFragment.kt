package com.example.helpwheel.ui.fuel_management.new_ride_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.helpwheel.databinding.FragmentDistanceOfNewRideBinding
import com.example.helpwheel.ui.fuel_management.TripViewModel


class DistanceOfNewRideFragment: Fragment() {
    lateinit var binding: FragmentDistanceOfNewRideBinding
    private lateinit var tripViewModel: TripViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDistanceOfNewRideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tripViewModel.getDistanceNewRide().observe(viewLifecycleOwner) {
            binding.distanceCounter.text = it
        }
    }
}