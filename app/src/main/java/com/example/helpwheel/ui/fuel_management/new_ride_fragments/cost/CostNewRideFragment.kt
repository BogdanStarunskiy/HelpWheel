package com.example.helpwheel.ui.fuel_management.new_ride_fragments.cost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.databinding.FragmentCostOfNewRideBinding


class CostNewRideFragment: Fragment() {
    lateinit var binding: FragmentCostOfNewRideBinding
    private lateinit var costNewRideViewModel: CostNewRideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCostOfNewRideBinding.inflate(inflater, container, false)
        costNewRideViewModel = ViewModelProvider(requireActivity())[CostNewRideViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        costNewRideViewModel.getCostNewRide().observe(viewLifecycleOwner) { binding.price.text = it }
    }
}