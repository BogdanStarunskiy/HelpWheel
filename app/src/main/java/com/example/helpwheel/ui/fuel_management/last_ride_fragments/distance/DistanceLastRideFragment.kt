package com.example.helpwheel.ui.fuel_management.last_ride_fragments.distance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.databinding.FragmentDistanceOfLastRideBinding

class DistanceLastRideFragment: Fragment() {
    lateinit var binding: FragmentDistanceOfLastRideBinding
    private lateinit var distanceLastRideViewModel: DistanceLastRideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDistanceOfLastRideBinding.inflate(inflater, container, false)
        distanceLastRideViewModel = ViewModelProvider(requireActivity())[DistanceLastRideViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        distanceLastRideViewModel.getDistanceLastRide().observe(viewLifecycleOwner) {
            binding.distanceCounter.text = it
        }
    }

}