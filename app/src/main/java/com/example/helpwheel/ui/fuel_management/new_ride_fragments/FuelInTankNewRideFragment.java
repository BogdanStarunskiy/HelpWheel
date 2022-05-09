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
import com.example.helpwheel.databinding.FragmentFuelInTankNewRideBinding;

public class FuelInTankNewRideFragment extends Fragment {
    FragmentFuelInTankNewRideBinding binding;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String APP_NEW_RIDE_REMAINS_FUEL = "remains_fuel";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFuelInTankNewRideBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, requireContext().MODE_PRIVATE);
        binding.remainsInTheTankCount.setText(String.format("%s %s", fuelStats.getFloat(APP_NEW_RIDE_REMAINS_FUEL, 0.0f), getString(R.string.litres_symbol)));
        if (fuelStats.getFloat(APP_NEW_RIDE_REMAINS_FUEL, 0.0f) <= 0){
            binding.remainsInTheTankCount.setTextColor(requireContext().getColor(R.color.red));
        }
    }
}