package com.example.helpwheel.ui.fuel_management.new_ride_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.databinding.FragmentCostOfNewRideBinding;
import com.example.helpwheel.utils.Constants;


public class CostOfNewRideFragment extends Fragment {
    FragmentCostOfNewRideBinding binding;
    Constants constants;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCostOfNewRideBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences fuelStats = requireContext().getSharedPreferences(constants.APP_PREFERENCES, requireContext().MODE_PRIVATE);
        binding.price.setText(String.valueOf(fuelStats.getFloat(constants.APP_NEW_RIDE_PRICE, 0.0f)));
    }
}