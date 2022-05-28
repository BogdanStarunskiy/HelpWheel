package com.example.helpwheel.ui.fuel_management.last_ride_fragments.ecology;

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
import com.example.helpwheel.utils.Constants;

public class EcologyLastRideFragment extends Fragment {
    FragmentEcologyImpactBinding binding;
    Constants constants;
    SharedPreferences fuelStats;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEcologyImpactBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fuelStats = requireContext().getSharedPreferences(constants.APP_PREFERENCES, getContext().MODE_PRIVATE);
        binding.dieselCount.setText(String.format("%s %s", fuelStats.getFloat(constants.APP_PREFERENCES_DIESEL_EMISSIONS, 0.0f), getString(R.string.grams_sign)));
        binding.gasolineCount.setText(String.format("%s %s", fuelStats.getFloat(constants.APP_PREFERENCES_GASOLINE_EMISSIONS, 0.0f), getString(R.string.grams_sign)));
    }
}