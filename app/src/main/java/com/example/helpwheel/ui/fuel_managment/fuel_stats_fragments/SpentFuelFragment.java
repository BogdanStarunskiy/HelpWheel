package com.example.helpwheel.ui.fuel_managment.fuel_stats_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentSpentFuelBinding;

public class SpentFuelFragment extends Fragment {
    FragmentSpentFuelBinding binding;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String APP_PREFERENCES_SPENT_FUEL = "spent_fuel";
    SharedPreferences fuelStats;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSpentFuelBinding.inflate(inflater, container, false);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.spentFuel.setText(String.valueOf(fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f)));
    }
}