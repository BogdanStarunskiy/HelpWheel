package com.example.helpwheel.ui.fuel_management.bottom_sheet_fragments;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentLastRideBottomSheetBinding;
import com.example.helpwheel.ui.fuel_management.BottomSheetCallBack;

import java.math.BigDecimal;
import java.util.Objects;


public class LastRideBottomSheetFragment extends Fragment {
    FragmentLastRideBottomSheetBinding binding;
    BottomSheetCallBack callBack;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String PREF = "user";
    public static final String APP_PREFERENCES_ODOMETER = "odometer";
    public static final String APP_PREFERENCES_PRE_PRICE = "pre_price";
    public static final String APP_PREFERENCES_ODOMETER_OLD = "odometer_old";
    public static final String APP_PREFERENCES_DISTANCE = "distance";
    public static final String APP_PREFERENCES_PRICE = "price";
    public static final String CONSUMPTION_PER_100KM = "consumptionPer100km";
    public static final String APP_PREFERENCES_SPENT_FUEL = "spent_fuel";
    public static final String APP_PREFERENCES_GASOLINE_EMISSIONS = "gasoline_emissions";
    public static final String APP_PREFERENCES_DIESEL_EMISSIONS = "diesel_emissions";
    public static final String FUEL_TANK_CAPACITY = "fuelTankCapacity";
    public static final String FUEL_LEVEL_OLD = "fuelLevelOld";
    public static final String FUEL_LEVEL = "fuelLevel";
    private static final Float co2EmissionPer1LiterOfGasoline = 2.347f;
    private static final Float co2EmissionPer1LiterOfDiesel = 2.689f;
    SharedPreferences fuelStats, regData;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLastRideBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        regData = requireContext().getSharedPreferences(PREF, requireContext().MODE_PRIVATE);
        binding.submitBtnFuel.setOnClickListener(v -> {
            String odometerValue = Objects.requireNonNull(binding.odometerText.getText()).toString();
            String priceValue = Objects.requireNonNull(binding.priceText.getText()).toString();
            if (!odometerValue.isEmpty() && !priceValue.isEmpty()) {
                editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                editor.putFloat(APP_PREFERENCES_PRE_PRICE, Float.parseFloat(priceValue));
                calculatingDifferences(OldOdometerValue(), Float.parseFloat(odometerValue));
                countPrice();
                callMethods();
            } else if (!odometerValue.isEmpty()) {
                calculatingDifferences(OldOdometerValue(), Float.parseFloat(odometerValue));
                editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                editor.putFloat(APP_PREFERENCES_PRICE, 0.0f);
                callMethods();
            } else if (!priceValue.isEmpty()) {
                binding.odometerEditText.setError(getString(R.string.edit_text_odometer_error));
            }
            // callBack.dismissBottomSheet();

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editor.apply();
    }

    private void calculatingDifferences(Float oldOdometerValue, Float odometerValue) {
        if (fuelStats.getFloat(APP_PREFERENCES_ODOMETER_OLD, 0.0f) == 0.0f) {
            showCustomDialog();
        } else {
            editor.putFloat(APP_PREFERENCES_DISTANCE, formattedNumber(odometerValue - oldOdometerValue));
            editor.apply();
        }
    }

    private Float formattedNumber(Float number) {
        return BigDecimal.valueOf(number)
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();
    }

    private void countPrice() {
        Float price = consumptionPer1km() * fuelStats.getFloat(APP_PREFERENCES_DISTANCE, 0.0f) * fuelStats.getFloat(APP_PREFERENCES_PRE_PRICE, 0.0f);
        editor.putFloat(APP_PREFERENCES_PRICE, formattedNumber(price));
        editor.apply();
    }

    private Float consumptionPer1km() {
        return regData.getFloat(CONSUMPTION_PER_100KM, 0.0f) / 100;
    }

    private void callMethods() {
        countSpentFuel();
        countImpactOnEcology();
        countFuelInTank();
    }

    private void countSpentFuel() {
        Float spentFuel = consumptionPer1km() * fuelStats.getFloat(APP_PREFERENCES_DISTANCE, 0.0f);
        editor.putFloat(APP_PREFERENCES_SPENT_FUEL, formattedNumber(spentFuel));
        editor.apply();
    }

    private void countImpactOnEcology() {
        Float spentFuel = fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f);
        Float gasolineEmissions = co2EmissionPer1LiterOfGasoline * spentFuel;
        Float dieselEmissions = co2EmissionPer1LiterOfDiesel * spentFuel;
        editor.putFloat(APP_PREFERENCES_GASOLINE_EMISSIONS, formattedNumber(gasolineEmissions));
        editor.putFloat(APP_PREFERENCES_DIESEL_EMISSIONS, formattedNumber(dieselEmissions));
        editor.apply();
    }

    private Float OldOdometerValue() {
        editor.putFloat(APP_PREFERENCES_ODOMETER_OLD, fuelStats.getFloat(APP_PREFERENCES_ODOMETER, 0));
        editor.apply();
        return fuelStats.getFloat(APP_PREFERENCES_ODOMETER_OLD, 0.0f);
    }

    private void countFuelInTank() {
        if (fuelStats.getFloat(FUEL_LEVEL_OLD, 0.0f) == 0.0f) {
            float fuelLevel = regData.getFloat(FUEL_TANK_CAPACITY, 0.0f) - fuelStats.getFloat(FUEL_LEVEL_OLD, 0.0f);
            editor.putFloat(FUEL_LEVEL_OLD, fuelLevel);
        } else {
            float newFuelLevel = fuelStats.getFloat(FUEL_LEVEL_OLD, 0.0f) - fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f);
            editor.putFloat(FUEL_LEVEL_OLD, newFuelLevel);
            editor.putFloat(FUEL_LEVEL, newFuelLevel);
        }
        editor.apply();
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.WelcomeAlertDialog);
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.fuel_alert_dialog_layout, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.alert_dialog_fuel_ok_button).setOnClickListener(view1 -> alertDialog.dismiss());
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
    }

    public void regCallBack(BottomSheetCallBack callBack) {
        this.callBack = callBack;
    }
}
