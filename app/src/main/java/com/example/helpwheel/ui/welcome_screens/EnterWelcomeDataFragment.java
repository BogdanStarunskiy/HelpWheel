package com.example.helpwheel.ui.welcome_screens;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentEnterWelcomeDataBinding;

import java.util.Objects;

public class EnterWelcomeDataFragment extends Fragment {
    FragmentEnterWelcomeDataBinding binding;
    SharedPreferences.Editor editor;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String USERNAME = "usernamePref";
    public static final String CONSUMPTION_PER_100KM = "consumptionPer100km";
    public static final String FUEL_TANK_CAPACITY = "fuel_tank_capacity";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEnterWelcomeDataBinding.inflate(inflater, container, false);
        SharedPreferences fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonOK.setOnClickListener(v -> putToSharedPreferences());
    }

    private void putToSharedPreferences() {
        try {
            String username = Objects.requireNonNull(binding.enterName.getText()).toString().trim();
            float consumptionPer100km = Float.parseFloat(Objects.requireNonNull(binding.consumptionPer100km.getText()).toString());
            float fuelTankCapacity = Float.parseFloat(Objects.requireNonNull(binding.fuelTankCapacity.getText()).toString());
            editor.putString(USERNAME, username);
            editor.putFloat(CONSUMPTION_PER_100KM, consumptionPer100km);
            editor.putFloat(FUEL_TANK_CAPACITY, fuelTankCapacity);
            editor.apply();
            NavHostFragment.findNavController(this).navigate(R.id.action_enterWelcomeDataFragment_to_navigation_dashboard);
        } catch (Exception e) {
            Toast.makeText(requireContext(), R.string.field_must_be_filled, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.GONE);
    }
}