package com.example.helpwheel.ui.fuel_management.new_ride_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentSpendFuelNewRideBinding;
import com.example.helpwheel.utils.Constants;

public class SpendFuelNewRideFragment extends Fragment {
    FragmentSpendFuelNewRideBinding binding;
    Constants constants;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSpendFuelNewRideBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences fuelStats = requireContext().getSharedPreferences(constants.APP_PREFERENCES, requireContext().MODE_PRIVATE);
        binding.spendFuelNewRide.setText(String.format("%s %s", fuelStats.getFloat(constants.APP_NEW_RIDE_WILL_BE_USED_FUEL, 0.0f), getString(R.string.litres_symbol)));
    }
}