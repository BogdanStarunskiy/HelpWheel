package com.example.helpwheel.ui.trip.last_ride_fragments.ecology

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentEcologyImpactBinding

class EcologyLastRideFragment: Fragment() {
    private lateinit var binding: FragmentEcologyImpactBinding
    private lateinit var ecologyLastRideViewModel: EcologyLastRideViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEcologyImpactBinding.inflate(inflater, container, false)
        ecologyLastRideViewModel = ViewModelProvider(requireActivity())[EcologyLastRideViewModel::class.java]
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ecologyLastRideViewModel.getDieselEmissions().observe(viewLifecycleOwner) {
            binding.dieselCount.text = "$it ${getString(R.string.grams_sign)}"
        }
        ecologyLastRideViewModel.getGasolineEmissions().observe(viewLifecycleOwner) {
            binding.gasolineCount.text = "$it ${getString(R.string.grams_sign)}"
        }
    }
}