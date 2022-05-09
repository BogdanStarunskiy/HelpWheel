package com.example.helpwheel.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
            if (!Objects.requireNonNull(binding.enterName.getText()).toString().trim().isEmpty()) {
                editor.putString(USERNAME_PREF, binding.enterName.getText().toString().trim());
                editor.apply();
            }
            if (!Objects.requireNonNull(binding.consumptionPer100km.getText()).toString().trim().isEmpty()) {
                editor.putFloat(CONSUMPTION_PER_100KM, Float.parseFloat(binding.consumptionPer100km.getText().toString()));
                editor.apply();
                sharedPreferencesHolder.countSpendFuel("new");
                sharedPreferencesHolder.countSpendFuel("last");
                sharedPreferencesHolder.countPrice("new");
                sharedPreferencesHolder.countPrice("last");
                sharedPreferencesHolder.countFuelInTank();
                sharedPreferencesHolder.calculateRemainsFuel();
            }
            if (!Objects.requireNonNull(binding.fuelTankCapacity.getText()).toString().isEmpty()) {
                float temp2 = fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f);
                editor.putFloat(FUEL_TANK_CAPACITY_OLD, temp2);
                float temp3 = Float.parseFloat(binding.fuelTankCapacity.getText().toString());
                editor.putFloat(FUEL_TANK_CAPACITY, temp3);
                editor.apply();
                sharedPreferencesHolder.updateFuelTankCapacity();
                sharedPreferencesHolder.calculateRemainsFuel();
            }
            NavHostFragment.findNavController(this).navigate(R.id.action_changeDataFragment_to_navigation_dashboard);
        });
        binding.removeOdometerReadings.setOnClickListener(v -> showDialog());
    }
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.remove_odometer_readings_dialog, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        Button yesBtn =  dialog.findViewById(R.id.yes_btn);
        Button noBtn = dialog.findViewById(R.id.no_btn);
        assert yesBtn != null;
        yesBtn.setOnClickListener(v1 -> {
            sharedPreferencesHolder.removeLastRideData();
            dialog.dismiss();
        });
        assert noBtn != null;
        noBtn.setOnClickListener(v2 -> dialog.dismiss());
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.GONE);
    }
}