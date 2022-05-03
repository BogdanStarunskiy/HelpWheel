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


public class CostOfNewRideFragment extends Fragment {
    FragmentCostOfNewRideBinding binding;
    public static final String APP_NEW_RIDE_PREFERENCES = "new_ride_prefs";
    public static final String APP_NEW_RIDE_PRICE = "new_ride_price";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCostOfNewRideBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences newRideData = requireContext().getSharedPreferences(APP_NEW_RIDE_PREFERENCES, getContext().MODE_PRIVATE);
        binding.price.setText(String.valueOf(newRideData.getFloat(APP_NEW_RIDE_PRICE, 0.0f)));
    }
}