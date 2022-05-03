package com.example.helpwheel.ui.fuel_management.bottom_sheet_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentNewRideBottomSheetBinding;

import java.math.BigDecimal;
import java.util.Objects;

public class NewRideBottomSheetFragment extends Fragment {
    public static final String PREF = "user";
    public static final String APP_NEW_RIDE_PREFERENCES = "new_ride_prefs";
    public static final String APP_NEW_RIDE_DISTANCE = "new_ride_distance";
    public static final String APP_NEW_RIDE_PRICE = "new_ride_price";
    public static final String APP_NEW_RIDE_PRE_PRICE = "new_ride_pre_price";
    public static final String CONSUMPTION_PER_100KM = "consumptionPer100km";
    public static final String APP_NEW_RIDE_WILL_BE_USED_FUEL = "will_be_used_fuel";
    public static final String APP_NEW_RIDE_GASOLINE_EMISSIONS = "new_ride_gasoline_emissions";
    public static final String APP_NEW_RIDE_DIESEL_EMISSIONS = "new_ride_diesel_emissions";
    private static final Float co2EmissionPer1LiterOfGasoline = 2.347f;
    private static final Float co2EmissionPer1LiterOfDiesel = 2.689f;
    private FragmentNewRideBottomSheetBinding binding;
    private SharedPreferences newRideData, regData;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewRideBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newRideData = requireContext().getSharedPreferences(APP_NEW_RIDE_PREFERENCES, requireContext().MODE_PRIVATE);
        regData = requireContext().getSharedPreferences(PREF, requireContext().MODE_PRIVATE);
        editor = newRideData.edit();
        binding.submitBtnFuel.setOnClickListener(v -> {
            String distance = Objects.requireNonNull(binding.distanceNewRideText.getText()).toString();
            String price = Objects.requireNonNull(binding.priceNewRideText.getText()).toString();
            if (!distance.isEmpty() && !price.isEmpty()) {
                editor.putFloat(APP_NEW_RIDE_DISTANCE, Float.parseFloat(distance));
                editor.putFloat(APP_NEW_RIDE_PRE_PRICE, Float.parseFloat(price));
                countPrice();
                callMethods();
            } else if (!distance.isEmpty()) {
                editor.putFloat(APP_NEW_RIDE_DISTANCE, Float.parseFloat(distance));
                editor.putFloat(APP_NEW_RIDE_PRE_PRICE, 0.0f);
                callMethods();
            } else if (!price.isEmpty()) {
                binding.distanceNewRideEditText.setError(getString(R.string.edit_text_odometer_error));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editor.apply();
    }

    private void countPrice() {
        Float price = consumptionPer1km() * newRideData.getFloat(APP_NEW_RIDE_DISTANCE, 0.0f) * newRideData.getFloat(APP_NEW_RIDE_PRE_PRICE, 0.0f);
        editor.putFloat(APP_NEW_RIDE_PRICE, formattedNumber(price));
        editor.apply();
    }

    private Float consumptionPer1km() {
        return regData.getFloat(CONSUMPTION_PER_100KM, 0.0f) / 100;
    }

    private Float formattedNumber(Float number) {
        return BigDecimal.valueOf(number)
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();
    }

    private void callMethods() {
        countWillBeUsedFuel();
        countImpactOnEcology();
    }

    private void countWillBeUsedFuel() {
        Float willBeUsed = consumptionPer1km() * newRideData.getFloat(APP_NEW_RIDE_DISTANCE, 0.0f);
        editor.putFloat(APP_NEW_RIDE_WILL_BE_USED_FUEL, formattedNumber(willBeUsed));
        editor.apply();
    }

    private void countImpactOnEcology() {
        Float spentFuel = newRideData.getFloat(APP_NEW_RIDE_WILL_BE_USED_FUEL, 0.0f);
        Float gasolineEmissions = co2EmissionPer1LiterOfGasoline * spentFuel;
        Float dieselEmissions = co2EmissionPer1LiterOfDiesel * spentFuel;
        editor.putFloat(APP_NEW_RIDE_GASOLINE_EMISSIONS, formattedNumber(gasolineEmissions));
        editor.putFloat(APP_NEW_RIDE_DIESEL_EMISSIONS, formattedNumber(dieselEmissions));
        editor.apply();
    }

}