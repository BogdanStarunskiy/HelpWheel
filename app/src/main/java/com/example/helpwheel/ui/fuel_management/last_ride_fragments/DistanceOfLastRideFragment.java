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
import com.example.helpwheel.databinding.FragmentDistanceOfLastRideBinding;
import com.example.helpwheel.utils.Constants;

public class DistanceOfLastRideFragment extends Fragment {
    FragmentDistanceOfLastRideBinding binding;
    SharedPreferences fuelStats;
    Constants constants;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDistanceOfLastRideBinding.inflate(inflater, container, false);

        fuelStats = requireContext().getSharedPreferences(constants.APP_PREFERENCES, getContext().MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (fuelStats.getFloat(constants.APP_PREFERENCES_ODOMETER_OLD, 0.0f) == 0.0f)
            binding.distanceCounter.setText(String.format("0.0 %s", getResources().getString(R.string.km)));
        else
            binding.distanceCounter.setText(String.format("%s %s", fuelStats.getFloat(constants.APP_PREFERENCES_DISTANCE, 0.0f), getResources().getString(R.string.km)));
    }

}