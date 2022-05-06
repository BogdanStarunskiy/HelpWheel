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
import com.example.helpwheel.ui.fuel_management.inerface.BottomSheetCallBack;
import com.example.helpwheel.utils.SharedPreferencesHolder;

import java.util.Objects;

public class NewRideBottomSheetFragment extends Fragment {
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String APP_NEW_RIDE_DISTANCE = "new_ride_distance";
    public static final String APP_NEW_RIDE_PRE_PRICE = "new_ride_pre_price";
    private FragmentNewRideBottomSheetBinding binding;
    private SharedPreferences.Editor editor;
    BottomSheetCallBack callBack;
    SharedPreferencesHolder sharedPreferencesHolder;

    public NewRideBottomSheetFragment(BottomSheetCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewRideBottomSheetBinding.inflate(inflater, container, false);
        sharedPreferencesHolder = new SharedPreferencesHolder(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, requireContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        sharedPreferencesHolder.setFuelStats(fuelStats);
        sharedPreferencesHolder.setEditor(editor);
        binding.submitBtnFuel.setOnClickListener(v -> {
            String distance = Objects.requireNonNull(binding.distanceNewRideText.getText()).toString();
            String price = Objects.requireNonNull(binding.priceNewRideText.getText()).toString();
            if (!distance.isEmpty() && !price.isEmpty()) {
                editor.putFloat(APP_NEW_RIDE_DISTANCE, Float.parseFloat(distance));
                editor.putFloat(APP_NEW_RIDE_PRE_PRICE, Float.parseFloat(price));
                sharedPreferencesHolder.countPrice("new");
                callMethods();
                callBack.dismissBottomSheet();
            } else if (!distance.isEmpty()) {
                editor.putFloat(APP_NEW_RIDE_DISTANCE, Float.parseFloat(distance));
                editor.putFloat(APP_NEW_RIDE_PRE_PRICE, 0.0f);
                callMethods();
                callBack.dismissBottomSheet();
            } else if (!price.isEmpty()) {
                binding.distanceNewRideEditText.setError(getString(R.string.edit_text_odometer_error));
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
        sharedPreferencesHolder.countSpendFuel("new");
        sharedPreferencesHolder.countImpactOnEcology("new");
        sharedPreferencesHolder.calculateRemainsFuel();
    }

}