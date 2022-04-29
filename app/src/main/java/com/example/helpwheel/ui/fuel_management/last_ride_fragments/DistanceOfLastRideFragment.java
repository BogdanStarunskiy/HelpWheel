package com.example.helpwheel.ui.fuel_management.last_ride_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.databinding.FragmentDistanceOfLastRideBinding;

public class DistanceOfLastRideFragment extends Fragment {
    FragmentDistanceOfLastRideBinding binding;
    SharedPreferences fuelStats;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String APP_PREFERENCES_ODOMETER_OLD = "odometer_old";
    public static final String APP_PREFERENCES_DISTANCE = "distance";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDistanceOfLastRideBinding.inflate(inflater, container, false);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fuelStats.getFloat(APP_PREFERENCES_ODOMETER_OLD, 0.0f) == 0.0f)
            binding.distanceCounter.setText("0.0");
        else
            binding.distanceCounter.setText(Float.toString(fuelStats.getFloat(APP_PREFERENCES_DISTANCE, 0.0f)));
    }

}