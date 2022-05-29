package com.example.helpwheel.ui.trip.new_ride_fragments.distance

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentDistanceOfNewRideBinding


class DistanceNewRideFragment: Fragment() {
    lateinit var binding: FragmentDistanceOfNewRideBinding
    private lateinit var distanceNewRideViewModel: DistanceNewRideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDistanceOfNewRideBinding.inflate(inflater, container, false)
        distanceNewRideViewModel = ViewModelProvider(requireActivity())[DistanceNewRideViewModel::class.java]
        distanceNewRideViewModel.setDefaultDistance()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        distanceNewRideViewModel.getDistanceNewRide().observe(viewLifecycleOwner) {
            binding.distanceCounter.text = "$it ${getString(R.string.km)}"
        }
    }
}