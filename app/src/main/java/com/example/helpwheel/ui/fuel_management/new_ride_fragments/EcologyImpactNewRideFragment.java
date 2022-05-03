package com.example.helpwheel.ui.fuel_management.new_ride_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentEcologyImpactBinding;


public class EcologyImpactNewRideFragment extends Fragment {
    FragmentEcologyImpactBinding binding;
    public static final String APP_NEW_RIDE_PREFERENCES = "new_ride_prefs";
    public static final String APP_NEW_RIDE_GASOLINE_EMISSIONS = "new_ride_gasoline_emissions";
    public static final String APP_NEW_RIDE_DIESEL_EMISSIONS = "new_ride_diesel_emissions";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEcologyImpactBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences newRideData = requireContext().getSharedPreferences(APP_NEW_RIDE_PREFERENCES, requireContext().MODE_PRIVATE);
        binding.gasolineCount.setText(String.format("%s %s", newRideData.getFloat(APP_NEW_RIDE_GASOLINE_EMISSIONS, 0.0f), getString(R.string.grams_sign)));
        binding.dieselCount.setText(String.format("%s %s", newRideData.getFloat(APP_NEW_RIDE_DIESEL_EMISSIONS, 0.0f), getString(R.string.grams_sign)));
    }
}