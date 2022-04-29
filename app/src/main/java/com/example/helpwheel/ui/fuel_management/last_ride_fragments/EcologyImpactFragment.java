package com.example.helpwheel.ui.fuel_management.last_ride_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpwheel.databinding.FragmentEcologyImpactBinding;

public class EcologyImpactFragment extends Fragment {
    FragmentEcologyImpactBinding binding;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String APP_PREFERENCES_GASOLINE_EMISSIONS = "gasoline_emissions";
    public static final String APP_PREFERENCES_DIESEL_EMISSIONS = "diesel_emissions";
    SharedPreferences fuelStats;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEcologyImpactBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        binding.dieselCount.setText(String.valueOf(fuelStats.getFloat(APP_PREFERENCES_DIESEL_EMISSIONS, 0.0f)));
        binding.gasolineCount.setText(String.valueOf(fuelStats.getFloat(APP_PREFERENCES_GASOLINE_EMISSIONS, 0.0f)));
    }
}