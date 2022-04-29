package com.example.helpwheel.ui.fuel_management.last_ride_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.databinding.FragmentCostOfLastRideBinding;

public class CostOfLastRideFragment extends Fragment {
    FragmentCostOfLastRideBinding binding;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String APP_PREFERENCES_PRE_PRICE = "pre_price";
    public static final String APP_PREFERENCES_PRICE = "price";
    SharedPreferences fuelStats;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCostOfLastRideBinding.inflate(inflater, container, false);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fuelStats.getFloat(APP_PREFERENCES_PRE_PRICE, 0.0f) == 0.0f)
            binding.price.setText("0.0");
        else
            binding.price.setText(Float.toString(fuelStats.getFloat(APP_PREFERENCES_PRICE, 0.0f)));
    }
}