package com.example.helpwheel.ui.trip.new_ride_fragments.ecology

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentEcologyImpactNewRideBinding


class EcologyNewRideFragment: Fragment() {
    private lateinit var binding :FragmentEcologyImpactNewRideBinding
    private lateinit var ecologyNewRideViewModel: EcologyNewRideViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEcologyImpactNewRideBinding.inflate(inflater, container, false)
        ecologyNewRideViewModel = ViewModelProvider(requireActivity())[EcologyNewRideViewModel::class.java]
        ecologyNewRideViewModel.setDefaultEmissions()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ecologyNewRideViewModel.getGasolineEmissions().observe(viewLifecycleOwner) {
            binding.gasolineCount.text = "$it ${getString(R.string.grams_sign)}"
        }
        ecologyNewRideViewModel.getDieselEmissions().observe(viewLifecycleOwner) {
            binding.dieselCount.text = "$it ${getString(R.string.grams_sign)}"
        }
    }
}