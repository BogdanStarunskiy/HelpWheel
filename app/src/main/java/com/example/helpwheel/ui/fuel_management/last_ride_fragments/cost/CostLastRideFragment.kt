package com.example.helpwheel.ui.fuel_management.last_ride_fragments.cost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.databinding.FragmentCostOfLastRideBinding

class CostLastRideFragment: Fragment() {
    lateinit var binding: FragmentCostOfLastRideBinding
    private lateinit var costLastRideViewModel: CostLastRideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCostOfLastRideBinding.inflate(inflater, container, false)
        costLastRideViewModel = ViewModelProvider(requireActivity())[CostLastRideViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        costLastRideViewModel.getCostLastRide().observe(viewLifecycleOwner) {
            binding.lastRidePrice.text = it
        }
    }

}