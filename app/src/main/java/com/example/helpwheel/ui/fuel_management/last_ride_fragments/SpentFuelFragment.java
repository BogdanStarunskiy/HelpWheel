package com.example.helpwheel.ui.fuel_management.last_ride_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentSpentFuelBinding;
import com.example.helpwheel.utils.Constants;

public class SpentFuelFragment extends Fragment {
    FragmentSpentFuelBinding binding;
    Constants constants;
    SharedPreferences fuelStats;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSpentFuelBinding.inflate(inflater, container, false);
        fuelStats = requireContext().getSharedPreferences(constants.APP_PREFERENCES, getContext().MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.spentFuel.setText(String.format("%s %s", fuelStats.getFloat(constants.APP_PREFERENCES_SPENT_FUEL, 0.0f), getString(R.string.litres_symbol)));
    }
}