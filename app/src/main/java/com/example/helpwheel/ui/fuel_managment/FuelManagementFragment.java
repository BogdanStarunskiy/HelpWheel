package com.example.helpwheel.ui.fuel_managment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentFuelManagementBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;

public class FuelManagementFragment extends Fragment {

    private FragmentFuelManagementBinding binding;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String PREF = "user";
    public static final String CONSUMPTION_PER_100KM = "consumptionPer100km";
    public static final String FUEL_TANK_CAPACITY = "fuelTankCapacity";
    public static final String APP_PREFERENCES_ODOMETER = "odometer";
    public static final String APP_PREFERENCES_ODOMETER_OLD = "odometer_old";
    public static final String APP_PREFERENCES_PRE_PRICE = "pre_price";
    public static final String APP_PREFERENCES_RESULT = "result";
    public static final String APP_PREFERENCES_PRICE = "price";
    SharedPreferences fuelStats, regData;
    AlertDialog alertDialog;
    private SharedPreferences.Editor editor, regDataEditor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFuelManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        regData = requireContext().getSharedPreferences(PREF, requireContext().MODE_PRIVATE);
        regDataEditor = regData.edit();
        binding.fuelInputButton.setOnClickListener(v -> {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
            bottomSheetDialog.show();
            Button submitBtn = bottomSheetDialog.findViewById(R.id.submit_btn_fuel);
            assert submitBtn != null;
            submitBtn.setOnClickListener(view1 -> {
                EditText odometer = bottomSheetDialog.findViewById(R.id.odometer_edit_text);
                EditText pricePerLiter = bottomSheetDialog.findViewById(R.id.price_edit_text);
                assert odometer != null;
                String odometerValue = odometer.getText().toString();
                assert pricePerLiter != null;
                String priceValue = pricePerLiter.getText().toString();

                if (!odometerValue.isEmpty() && !priceValue.isEmpty()) {
                    editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                    editor.putFloat(APP_PREFERENCES_PRE_PRICE, Float.parseFloat(priceValue));
                    calculatingDifferences(OldOdometerValue(), Float.parseFloat(odometerValue));
                    countPrice();
                    bottomSheetDialog.dismiss();
                } else if (!odometerValue.isEmpty()) {
                    calculatingDifferences(OldOdometerValue(), Float.parseFloat(odometerValue));
                    editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                    bottomSheetDialog.dismiss();
                } else if (!priceValue.isEmpty()) {
                    odometer.setError(getString(R.string.edit_text_odometer_error));
                }
                bottomSheetDialog.dismiss();
                editor.apply();
            });
        });
        updateUi();
    }

    public Float OldOdometerValue() {
        editor.putFloat(APP_PREFERENCES_ODOMETER_OLD, fuelStats.getFloat(APP_PREFERENCES_ODOMETER, 0));
        editor.apply();
        return fuelStats.getFloat(APP_PREFERENCES_ODOMETER_OLD, 0.0f);
    }

    @SuppressLint("SetTextI18n")
    public void calculatingDifferences(Float oldOdometerValue, Float odometerValue) {
        if (fuelStats.getFloat(APP_PREFERENCES_ODOMETER_OLD, 0.0f) == 0.0f) {
            binding.distanceCounter.setText("0.0");
            showCustomDialog();
        } else {
            float number = BigDecimal.valueOf(odometerValue - oldOdometerValue)
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            binding.distanceCounter.setText(Float.toString(number));
            editor.putFloat(APP_PREFERENCES_RESULT, number);
            editor.apply();
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateUi() {
        if (fuelStats.getFloat(APP_PREFERENCES_ODOMETER_OLD, 0.0f) == 0.0f)
            binding.distanceCounter.setText("0.0");
        else
            binding.distanceCounter.setText(Float.toString(fuelStats.getFloat(APP_PREFERENCES_RESULT, 0.0f)));
        if (fuelStats.getFloat(APP_PREFERENCES_PRE_PRICE, 0.0f) == 0.0f)
            binding.price.setText("0.0");
        else
            binding.price.setText(Float.toString(fuelStats.getFloat(APP_PREFERENCES_PRICE, 0.0f)));
    }

    public void countPrice() {
        Float price = consumptionPer1km() * fuelStats.getFloat(APP_PREFERENCES_RESULT, 0.0f) * fuelStats.getFloat(APP_PREFERENCES_PRE_PRICE, 0.0f);
        float number = BigDecimal.valueOf(price)
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();
        editor.putFloat(APP_PREFERENCES_PRICE, number);
        binding.price.setText(Float.toString(number));
        editor.apply();
    }

    public Float consumptionPer1km() {
        return regData.getFloat(CONSUMPTION_PER_100KM, 0.0f) / 100;
    }

    public void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.WelcomeAlertDialog);
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.fuel_alert_dialog_layout, null);
        builder.setView(view);
        alertDialog = builder.create();
        view.findViewById(R.id.alert_dialog_fuel_ok_button).setOnClickListener(view1 -> alertDialog.dismiss());
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}