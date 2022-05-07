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
import com.example.helpwheel.utils.SharedPreferencesHolder;

import java.util.Objects;

public class ChangeDataFragment extends Fragment {
    FragmentChangeDataBinding binding;
    SharedPreferences fuelStats;
    SharedPreferences.Editor editor;
    SharedPreferencesHolder sharedPreferencesHolder;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String USERNAME_PREF = "usernamePref";
    public static final String CONSUMPTION_PER_100KM = "consumptionPer100km";
    public static final String FUEL_TANK_CAPACITY = "fuel_tank_capacity";
    public static final String FUEL_TANK_CAPACITY_OLD = "fuel_tank_capacity_old";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangeDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, requireContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        sharedPreferencesHolder = new SharedPreferencesHolder(requireContext());
        sharedPreferencesHolder.setEditor(editor);
        sharedPreferencesHolder.setFuelStats(fuelStats);
        binding.buttonOK.setOnClickListener(v -> {
            if (!Objects.requireNonNull(binding.enterName.getText()).toString().trim().isEmpty())
                editor.putString(USERNAME_PREF, binding.enterName.getText().toString().trim());
            if (!Objects.requireNonNull(binding.consumptionPer100km.getText()).toString().trim().isEmpty())
                editor.putFloat(CONSUMPTION_PER_100KM, Float.parseFloat(binding.consumptionPer100km.getText().toString()));
            if (!Objects.requireNonNull(binding.fuelTankCapacity.getText()).toString().isEmpty()) {
                float temp2 = fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f);
                editor.putFloat(FUEL_TANK_CAPACITY_OLD, temp2);
                float temp3 = Float.parseFloat(binding.fuelTankCapacity.getText().toString());
                editor.putFloat(FUEL_TANK_CAPACITY, temp3);
                editor.apply();
                sharedPreferencesHolder.updateFuelTankCapacity();
            }
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