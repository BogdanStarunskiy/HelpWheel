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

public class ChangeDataFragment extends Fragment {
    FragmentChangeDataBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String PREF = "user";
    public static final String USERNAME_PREF = "usernamePref";
    public static final String CONSUMPTION_PER_100KM = "consumptionPer100km";
    public static final String FUEL_TANK_CAPACITY = "fuelTankCapacity";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangeDataBinding.inflate(inflater, container, false);
        preferences = requireContext().getSharedPreferences(PREF, requireContext().MODE_PRIVATE);
        editor = preferences.edit();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonOK.setOnClickListener(v -> {
            if (!binding.enterName.getText().toString().trim().isEmpty())
                editor.putString(USERNAME_PREF, binding.enterName.getText().toString().trim());
            if (!binding.consumptionPer100km.getText().toString().trim().isEmpty())
                editor.putString(CONSUMPTION_PER_100KM, binding.consumptionPer100km.getText().toString().trim());
            if (!binding.fuelTankCapacity.getText().toString().trim().isEmpty())
                editor.putString(FUEL_TANK_CAPACITY, binding.fuelTankCapacity.getText().toString().trim());
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