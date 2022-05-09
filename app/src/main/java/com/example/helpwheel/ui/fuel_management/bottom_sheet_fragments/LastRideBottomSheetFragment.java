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
import com.example.helpwheel.databinding.FragmentLastRideBottomSheetBinding;
import com.example.helpwheel.ui.fuel_management.inerface.BottomSheetCallBack;
import com.example.helpwheel.utils.SharedPreferencesHolder;

import java.util.Objects;


public class LastRideBottomSheetFragment extends Fragment {
    FragmentLastRideBottomSheetBinding binding;
    BottomSheetCallBack callBack;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String APP_PREFERENCES_ODOMETER = "odometer";
    public static final String APP_PREFERENCES_PRE_PRICE = "pre_price";
    public static final String APP_PREFERENCES_PRICE = "price";
    SharedPreferences fuelStats;
    private SharedPreferences.Editor editor;
    SharedPreferencesHolder sharedPreferencesHolder;

    public  LastRideBottomSheetFragment(BottomSheetCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLastRideBottomSheetBinding.inflate(inflater, container, false);
        sharedPreferencesHolder = new SharedPreferencesHolder(requireContext());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        sharedPreferencesHolder.setFuelStats(fuelStats);
        sharedPreferencesHolder.setEditor(editor);
        binding.lastOdometerText.setText(String.valueOf(fuelStats.getFloat(APP_PREFERENCES_ODOMETER, 0.0f)));
        binding.submitBtnFuel.setOnClickListener(v -> {
            String odometerValue = Objects.requireNonNull(binding.odometerText.getText()).toString();
            String priceValue = Objects.requireNonNull(binding.priceText.getText()).toString();
            if (!odometerValue.isEmpty() && !priceValue.isEmpty()) {
                editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                editor.putFloat(APP_PREFERENCES_PRE_PRICE, Float.parseFloat(priceValue));
                sharedPreferencesHolder.calculatingDistance(sharedPreferencesHolder.oldOdometerValue(), Float.parseFloat(odometerValue));
                sharedPreferencesHolder.countPrice("last");
                editor.apply();
                callMethods();
                callBack.dismissBottomSheet();
            } else if (!odometerValue.isEmpty()) {
                sharedPreferencesHolder.calculatingDistance(sharedPreferencesHolder.oldOdometerValue(), Float.parseFloat(odometerValue));
                editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                editor.putFloat(APP_PREFERENCES_PRICE, 0.0f);
                editor.apply();
                callMethods();
                callBack.dismissBottomSheet();
            } else if (!priceValue.isEmpty()) {
                binding.odometerEditText.setError(getString(R.string.edit_text_odometer_error));
            } else
             callBack.dismissBottomSheet();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editor.apply();
    }

    private void callMethods() {
        sharedPreferencesHolder.countSpendFuel("last");
        sharedPreferencesHolder.countImpactOnEcology("last");
        sharedPreferencesHolder.countFuelInTank();
        sharedPreferencesHolder.calculateRemainsFuel();
    }


}
