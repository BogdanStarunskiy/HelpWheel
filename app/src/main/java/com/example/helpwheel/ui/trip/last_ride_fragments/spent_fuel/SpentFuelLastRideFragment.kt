package com.example.helpwheel.ui.trip.last_ride_fragments.spent_fuel

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentSpentFuelBinding

class SpentFuelLastRideFragment: Fragment() {
    lateinit var binding: FragmentSpentFuelBinding
    private lateinit var spentFuelLastRideViewModel: SpentFuelLastRideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpentFuelBinding.inflate(inflater, container, false)
        spentFuelLastRideViewModel = ViewModelProvider(requireActivity())[SpentFuelLastRideViewModel::class.java]
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spentFuelLastRideViewModel.getSpentFuelLastRide().observe(viewLifecycleOwner) {
            binding.spentFuel.text = "$it ${getString(R.string.litres_symbol)}"
        }
    }
}