package com.example.helpwheel.ui.fuel_management.last_ride_fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.databinding.FragmentCostOfLastRideBinding;
import com.example.helpwheel.utils.Constants;

public class CostOfLastRideFragment extends Fragment {
    FragmentCostOfLastRideBinding binding;
    Constants constants;
    SharedPreferences fuelStats;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCostOfLastRideBinding.inflate(inflater, container, false);
        fuelStats = requireContext().getSharedPreferences(constants.APP_PREFERENCES, getContext().MODE_PRIVATE);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fuelStats.getFloat(constants.APP_PREFERENCES_PRE_PRICE, 0.0f) == 0.0f)
            binding.price.setText("0.0");
        else
            binding.price.setText(Float.toString(fuelStats.getFloat(constants.APP_PREFERENCES_PRICE, 0.0f)));
    }
}