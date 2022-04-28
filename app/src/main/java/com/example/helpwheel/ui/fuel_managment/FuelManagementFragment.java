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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentFuelManagementBinding;
import com.example.helpwheel.ui.fuel_managment.adapter.ScreenSlidePageAdapter;
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
    public static final String APP_PREFERENCES_SPENT_FUEL = "spent_fuel";
    public static final String APP_PREFERENCES_GASOLINE_EMISSIONS = "gasoline_emissions";
    public static final String APP_PREFERENCES_DIESEL_EMISSIONS = "diesel_emissions";

    private static final Float co2EmissionPer1LiterOfGasoline = 2.347f;
    private static final Float co2EmissionPer1LiterOfDiesel = 2.689f;
    SharedPreferences fuelStats, regData;
    AlertDialog alertDialog;
    private SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFuelManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager2 = binding.viewPagerLastRide;
        FragmentStateAdapter pageAdapter = new ScreenSlidePageAdapter(this);
        viewPager2.setAdapter(pageAdapter);

        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        regData = requireContext().getSharedPreferences(PREF, requireContext().MODE_PRIVATE);

        binding.fuelInputButton.setOnClickListener(v -> {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
            bottomSheetDialog.show();

            Button submitBtn = bottomSheetDialog.findViewById(R.id.submit_btn_fuel);
            assert submitBtn != null;

            submitBtn.setOnClickListener(view1 -> {

                EditText odometer = bottomSheetDialog.findViewById(R.id.odometer_text);
                EditText pricePerLiter = bottomSheetDialog.findViewById(R.id.price_text);
                assert odometer != null;
                String odometerValue = odometer.getText().toString();
                assert pricePerLiter != null;
                String priceValue = pricePerLiter.getText().toString();

                if (!odometerValue.isEmpty() && !priceValue.isEmpty()) {
                    editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                    editor.putFloat(APP_PREFERENCES_PRE_PRICE, Float.parseFloat(priceValue));
                    calculatingDifferences(OldOdometerValue(), Float.parseFloat(odometerValue));
                    countPrice();
                    callMethods();
                    bottomSheetDialog.dismiss();
                    onViewCreated(view, savedInstanceState);
                } else if (!odometerValue.isEmpty()) {
                    calculatingDifferences(OldOdometerValue(), Float.parseFloat(odometerValue));
                    editor.putFloat(APP_PREFERENCES_ODOMETER, Float.parseFloat(odometerValue));
                    callMethods();
                    bottomSheetDialog.dismiss();
                    onViewCreated(view, savedInstanceState);
                } else if (!priceValue.isEmpty()) {
                    odometer.setError(getString(R.string.edit_text_odometer_error));
                } else {
                    bottomSheetDialog.dismiss();
                    onViewCreated(view, savedInstanceState);
                }
                editor.apply();
            });
        });

    }

    private Float OldOdometerValue() {
        editor.putFloat(APP_PREFERENCES_ODOMETER_OLD, fuelStats.getFloat(APP_PREFERENCES_ODOMETER, 0));
        editor.apply();
        return fuelStats.getFloat(APP_PREFERENCES_ODOMETER_OLD, 0.0f);
    }

    @SuppressLint("SetTextI18n")
    private void calculatingDifferences(Float oldOdometerValue, Float odometerValue) {
        if (fuelStats.getFloat(APP_PREFERENCES_ODOMETER_OLD, 0.0f) == 0.0f) {
            showCustomDialog();
        } else {
            editor.putFloat(APP_PREFERENCES_RESULT, formattedNumber(odometerValue - oldOdometerValue));
            editor.apply();
        }
    }

    private void countPrice() {
        Float price = consumptionPer1km() * fuelStats.getFloat(APP_PREFERENCES_RESULT, 0.0f) * fuelStats.getFloat(APP_PREFERENCES_PRE_PRICE, 0.0f);
        editor.putFloat(APP_PREFERENCES_PRICE, formattedNumber(price));
        editor.apply();
    }

    private Float consumptionPer1km() {
        return regData.getFloat(CONSUMPTION_PER_100KM, 0.0f) / 100;
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.WelcomeAlertDialog);
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.fuel_alert_dialog_layout, null);
        builder.setView(view);
        alertDialog = builder.create();
        view.findViewById(R.id.alert_dialog_fuel_ok_button).setOnClickListener(view1 -> alertDialog.dismiss());
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
    }

    private void countSpentFuel(){
        Float spentFuel = consumptionPer1km() * fuelStats.getFloat(APP_PREFERENCES_RESULT, 0.0f);
        editor.putFloat(APP_PREFERENCES_SPENT_FUEL, spentFuel);
        editor.apply();
    }

    private void countImpactOnEcology(){
        Float spentFuel = fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f);
        Float gasolineEmissions = co2EmissionPer1LiterOfGasoline * spentFuel;
        Float dieselEmissions = co2EmissionPer1LiterOfDiesel * spentFuel;
        editor.putFloat(APP_PREFERENCES_GASOLINE_EMISSIONS, formattedNumber(gasolineEmissions));
        editor.putFloat(APP_PREFERENCES_DIESEL_EMISSIONS, formattedNumber(dieselEmissions));
        editor.apply();
    }

    private void callMethods(){
        countSpentFuel();
        countImpactOnEcology();
    }

    private Float formattedNumber(Float number){
        float result = BigDecimal.valueOf(number)
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editor.apply();
        binding = null;
    }
}