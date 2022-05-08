package com.example.helpwheel.ui.fuel_management;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentFirstOdometerReadingBinding;
import com.example.helpwheel.utils.SharedPreferencesHolder;

import java.util.Objects;

public class FirstOdometerReadingFragment extends Fragment {
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String APP_PREFERENCES_ODOMETER = "odometer";
    SharedPreferences fuelStats;
    SharedPreferences.Editor editor;
    FragmentFirstOdometerReadingBinding binding;
    SharedPreferencesHolder sharedPreferencesHolder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFirstOdometerReadingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, requireContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        sharedPreferencesHolder = new SharedPreferencesHolder(requireContext());
        binding.submitBtnFuel.setOnClickListener(v -> {
            if (!Objects.requireNonNull(binding.odometerText.getText()).toString().isEmpty()){
                editor.putFloat(APP_PREFERENCES_ODOMETER, sharedPreferencesHolder.formattedNumber(Float.parseFloat(binding.odometerText.getText().toString())));
                editor.apply();
                NavHostFragment.findNavController(this).navigate(R.id.action_firstOdometerReadingFragment_to_navigation_notifications);
            }
        });
    }
}