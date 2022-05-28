package com.example.helpwheel.ui.fuel_management.new_ride_fragments.cost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.databinding.FragmentCostOfNewRideBinding
import com.example.helpwheel.ui.fuel_management.TripViewModel


class CostNewRideFragment: Fragment() {
    lateinit var binding: FragmentCostOfNewRideBinding
    private lateinit var tripViewModel: TripViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCostOfNewRideBinding.inflate(inflater, container, false)
        tripViewModel = ViewModelProvider(requireActivity())[TripViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tripViewModel.getCostNewRide().observe(viewLifecycleOwner) { binding.price.text = it }
    }
}