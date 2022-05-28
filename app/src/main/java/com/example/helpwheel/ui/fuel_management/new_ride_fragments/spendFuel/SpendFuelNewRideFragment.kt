package com.example.helpwheel.ui.fuel_management.new_ride_fragments.spendFuel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.databinding.FragmentSpendFuelNewRideBinding

class SpendFuelNewRideFragment : Fragment() {
    lateinit var binding: FragmentSpendFuelNewRideBinding
    private lateinit var spendFuelNewRideViewModel: SpendFuelNewRideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpendFuelNewRideBinding.inflate(inflater, container, false)
        spendFuelNewRideViewModel = ViewModelProvider(requireActivity())[SpendFuelNewRideViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spendFuelNewRideViewModel.getSpendFuelNewRide()
            .observe(viewLifecycleOwner) { binding.spendFuelNewRide.text = it }
    }

}