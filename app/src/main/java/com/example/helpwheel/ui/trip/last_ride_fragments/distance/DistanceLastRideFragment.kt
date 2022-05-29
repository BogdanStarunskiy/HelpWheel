package com.example.helpwheel.ui.trip.last_ride_fragments.distance

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.R
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
        distanceLastRideViewModel.setDefaultDistance()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        distanceLastRideViewModel.getDistanceLastRide().observe(viewLifecycleOwner) {
            binding.distanceCounter.text = "$it ${getString(R.string.km)}"
        }
    }

}