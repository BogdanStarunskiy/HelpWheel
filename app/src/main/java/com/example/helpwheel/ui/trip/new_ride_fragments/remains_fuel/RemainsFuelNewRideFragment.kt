package com.example.helpwheel.ui.trip.new_ride_fragments.remains_fuel

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentFuelInTankNewRideBinding

class RemainsFuelNewRideFragment: Fragment() {
    lateinit var binding: FragmentFuelInTankNewRideBinding
    private lateinit var remainsFuelNewRideViewModel: RemainsFuelNewRideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFuelInTankNewRideBinding.inflate(inflater, container, false)
        remainsFuelNewRideViewModel = ViewModelProvider(requireActivity())[RemainsFuelNewRideViewModel::class.java]
        remainsFuelNewRideViewModel.setDefaultRemainsFuel()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        remainsFuelNewRideViewModel.getRemainsFuelNewRide().observe(viewLifecycleOwner) {
            binding.remainsInTheTankCount.text = "$it ${getString(R.string.litres_symbol)}"
        }
    }
}