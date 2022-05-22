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
import com.example.helpwheel.utils.Constants;
import com.example.helpwheel.utils.SharedPreferencesHolder;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import java.util.Objects;

public class ChangeDataFragment extends Fragment {
    FragmentChangeDataBinding binding;
    SharedPreferences fuelStats;
    SharedPreferences.Editor editor;
    SharedPreferencesHolder sharedPreferencesHolder;
    Constants constants;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangeDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fuelStats = requireContext().getSharedPreferences(constants.APP_PREFERENCES, requireContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        sharedPreferencesHolder = new SharedPreferencesHolder(requireContext());
        sharedPreferencesHolder.setEditor(editor);
        sharedPreferencesHolder.setFuelStats(fuelStats);
        showBalloon();
        editor.putBoolean(Constants.IS_FIRST_LAUNCHED_CHANGE_DATA, false).apply();
        initListeners();
    }

    private void initListeners() {
        binding.buttonOK.setOnClickListener(v -> {
            if (!Objects.requireNonNull(binding.enterName.getText()).toString().trim().isEmpty()) {
                editor.putString(constants.USERNAME_PREF, binding.enterName.getText().toString().trim());
                editor.apply();
            }
            if (!Objects.requireNonNull(binding.consumptionPer100km.getText()).toString().trim().isEmpty()) {
                editor.putFloat(constants.CONSUMPTION_PER_100KM, Float.parseFloat(binding.consumptionPer100km.getText().toString()));
                editor.apply();
                sharedPreferencesHolder.countSpendFuel("new");
                sharedPreferencesHolder.countSpendFuel("last");
                sharedPreferencesHolder.countPrice("new");
                sharedPreferencesHolder.countPrice("last");
                sharedPreferencesHolder.countFuelInTank();
                sharedPreferencesHolder.calculateRemainsFuel();
            }
            if (!Objects.requireNonNull(binding.fuelTankCapacity.getText()).toString().isEmpty()) {
                float temp2 = fuelStats.getFloat(constants.FUEL_TANK_CAPACITY, 0.0f);
                editor.putFloat(constants.FUEL_TANK_CAPACITY_OLD, temp2);
                float temp3 = Float.parseFloat(binding.fuelTankCapacity.getText().toString());
                editor.putFloat(constants.FUEL_TANK_CAPACITY, temp3);
                editor.apply();
                sharedPreferencesHolder.updateFuelTankCapacity();
                sharedPreferencesHolder.calculateRemainsFuel();
            }
            NavHostFragment.findNavController(this).navigate(R.id.action_changeDataFragment_to_navigation_dashboard);
        });
        binding.removeOdometerReadings.setOnClickListener(v -> showDialog());
    }

    private void showBalloon() {
        if (fuelStats.getBoolean(Constants.IS_FIRST_LAUNCHED_CHANGE_DATA, true)) {
            Balloon balloon = new Balloon.Builder(requireContext())
                    .setWidth(BalloonSizeSpec.WRAP)
                    .setHeight(BalloonSizeSpec.WRAP)
                    .setText(getString(R.string.balloon_odometer_readings))
                    .setTextColorResource(R.color.white)
                    .setTextSize(15f)
                    .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                    .setArrowSize(10)
                    .setArrowPosition(0.5f)
                    .setPadding(12)
                    .setCornerRadius(8f)
                    .setBackgroundColorResource(R.color.gray)
                    .setBalloonAnimation(BalloonAnimation.ELASTIC)
                    .setLifecycleOwner(getViewLifecycleOwner())
                    .setAutoDismissDuration(5000L)
                    .setMarginRight(15)
                    .build();
            balloon.showAlignBottom(binding.removeOdometerReadings);
        }
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