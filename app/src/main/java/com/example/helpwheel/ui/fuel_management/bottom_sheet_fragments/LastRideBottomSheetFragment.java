package com.example.helpwheel.ui.fuel_management.bottom_sheet_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentLastRideBottomSheetBinding;
import com.example.helpwheel.ui.fuel_management.inerface.BottomSheetCallBack;
import com.example.helpwheel.utils.Constants;
import com.example.helpwheel.utils.SharedPreferencesHolder;

import java.util.Objects;


public class LastRideBottomSheetFragment extends Fragment {
    FragmentLastRideBottomSheetBinding binding;
    BottomSheetCallBack callBack;
    Constants constants;
    SharedPreferences fuelStats;
    SharedPreferences.Editor editor;
    SharedPreferencesHolder sharedPreferencesHolder;
    String odometerValue;
    String priceValue;

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
        fuelStats = requireContext().getSharedPreferences(constants.APP_PREFERENCES, getContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        sharedPreferencesHolder.setFuelStats(fuelStats);
        sharedPreferencesHolder.setEditor(editor);
        binding.lastOdometerText.setText(String.valueOf(fuelStats.getFloat(constants.APP_PREFERENCES_ODOMETER, 0.0f)));
        String temp = String.valueOf(fuelStats.getFloat(constants.APP_PREFERENCES_ODOMETER, 0.0f));
        initListeners();
    }

    private void initListeners() {
        binding.submitBtnFuel.setOnClickListener(v -> {
            odometerValue = Objects.requireNonNull(binding.odometerText.getText()).toString();
            priceValue = Objects.requireNonNull(binding.priceText.getText()).toString();
            if (!odometerValue.isEmpty() && !priceValue.isEmpty()) {
                checkOdometerReadingsTwoFields(Float.parseFloat(odometerValue));
            } else if (!odometerValue.isEmpty()) {
                checkOdometerReadingsOneField(Float.parseFloat(odometerValue));
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

    private void checkOdometerReadingsTwoFields(Float odometer){
        if (fuelStats.getFloat(constants.APP_PREFERENCES_ODOMETER, 0.0f) > odometer){
            showDialogOdometerError();
        } else {
            editor.putFloat(constants.APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
            editor.putFloat(constants.APP_PREFERENCES_PRE_PRICE, Float.parseFloat(priceValue));
            sharedPreferencesHolder.calculatingDistance(sharedPreferencesHolder.oldOdometerValue(), Float.parseFloat(odometerValue));
            sharedPreferencesHolder.countPrice("last");
            editor.apply();
            callMethods();
            callBack.dismissBottomSheet();
        }
    }

    private void checkOdometerReadingsOneField(Float odometer){
        if (fuelStats.getFloat(constants.APP_PREFERENCES_ODOMETER, 0.0f) > odometer){
            showDialogOdometerError();
        } else {
            sharedPreferencesHolder.calculatingDistance(sharedPreferencesHolder.oldOdometerValue(), Float.parseFloat(odometerValue));
            editor.putFloat(constants.APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
            editor.putFloat(constants.APP_PREFERENCES_PRICE, 0.0f);
            editor.apply();
            callMethods();
            callBack.dismissBottomSheet();
        }
    }

    private void showDialogOdometerError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_odometer_error, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        Button okBtn = dialog.findViewById(R.id.ok_button);
        assert okBtn != null;
        okBtn.setOnClickListener(v -> dialog.dismiss());
    }

}
