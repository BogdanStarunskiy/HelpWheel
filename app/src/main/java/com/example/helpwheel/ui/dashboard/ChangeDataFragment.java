package com.example.helpwheel.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentChangeDataBinding;

import java.util.Objects;

public class ChangeDataFragment extends Fragment {
    FragmentChangeDataBinding binding;
    SharedPreferences fuelStats;
    SharedPreferences.Editor editor;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String USERNAME_PREF = "usernamePref";
    public static final String CONSUMPTION_PER_100KM = "consumptionPer100km";
    public static final String FUEL_TANK_CAPACITY = "fuel_tank_capacity";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangeDataBinding.inflate(inflater, container, false);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, requireContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonOK.setOnClickListener(v -> {
            if (!Objects.requireNonNull(binding.enterName.getText()).toString().trim().isEmpty())
                editor.putString(USERNAME_PREF, binding.enterName.getText().toString().trim());
            if (!Objects.requireNonNull(binding.consumptionPer100km.getText()).toString().trim().isEmpty())
                editor.putFloat(CONSUMPTION_PER_100KM, Float.parseFloat(binding.consumptionPer100km.getText().toString()));
            if (!Objects.requireNonNull(binding.fuelTankCapacity.getText()).toString().trim().isEmpty())
                editor.putFloat(FUEL_TANK_CAPACITY, Float.parseFloat(binding.fuelTankCapacity.getText().toString()));
            editor.apply();
            NavHostFragment.findNavController(this).navigate(R.id.action_changeDataFragment_to_navigation_dashboard);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.GONE);
    }
}