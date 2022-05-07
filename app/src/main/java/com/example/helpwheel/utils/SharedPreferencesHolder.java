package com.example.helpwheel.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.helpwheel.R;

import java.math.BigDecimal;
import java.util.Locale;

public class SharedPreferencesHolder {
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String APP_NEW_RIDE_DISTANCE = "new_ride_distance";
    public static final String APP_NEW_RIDE_PRICE = "new_ride_price";
    public static final String APP_NEW_RIDE_PRE_PRICE = "new_ride_pre_price";
    public static final String CONSUMPTION_PER_100KM = "consumptionPer100km";
    public static final String APP_NEW_RIDE_WILL_BE_USED_FUEL = "will_be_used_fuel";
    public static final String APP_NEW_RIDE_GASOLINE_EMISSIONS = "new_ride_gasoline_emissions";
    public static final String APP_NEW_RIDE_DIESEL_EMISSIONS = "new_ride_diesel_emissions";
    public static final String FUEL_LEVEL = "fuel_level";
    public static final String APP_NEW_RIDE_REMAINS_FUEL = "remains_fuel";
    public static final String APP_PREFERENCES_ODOMETER = "odometer";
    public static final String APP_PREFERENCES_PRE_PRICE = "pre_price";
    public static final String APP_PREFERENCES_ODOMETER_OLD = "odometer_old";
    public static final String APP_PREFERENCES_DISTANCE = "distance";
    public static final String APP_PREFERENCES_PRICE = "price";
    public static final String APP_PREFERENCES_SPENT_FUEL = "spent_fuel";
    public static final String APP_PREFERENCES_GASOLINE_EMISSIONS = "gasoline_emissions";
    public static final String APP_PREFERENCES_DIESEL_EMISSIONS = "diesel_emissions";
    public static final String FUEL_TANK_CAPACITY = "fuel_tank_capacity";
    public static final String FUEL_LEVEL_OLD = "fuelLevelOld";
    private static final Float co2EmissionPer1LiterOfGasoline = 2.347f;
    private static final Float co2EmissionPer1LiterOfDiesel = 2.689f;

    SharedPreferences fuelStats;
    SharedPreferences.Editor editor;
    Context sharedHolderContext;

    public SharedPreferencesHolder(Context context){
        this.sharedHolderContext = context;
    }

    public void setFuelStats(SharedPreferences fuelStats) {
        this.fuelStats = fuelStats;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public void countFuelInTank() {
        if (fuelStats.getFloat(FUEL_LEVEL_OLD, 0.0f) == 0.0f) {
            float fuelLevel = fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f) - fuelStats.getFloat(FUEL_LEVEL_OLD, 0.0f);
            editor.putFloat(FUEL_LEVEL_OLD, fuelLevel);
        } else {
            float newFuelLevel = fuelStats.getFloat(FUEL_LEVEL_OLD, 0.0f) - fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f);
            editor.putFloat(FUEL_LEVEL_OLD, formattedNumber(newFuelLevel));
            editor.putFloat(FUEL_LEVEL, formattedNumber(newFuelLevel));
        }
        editor.apply();
    }


    public void calculateRemainsFuel() {
        float spendFuel = fuelStats.getFloat(APP_NEW_RIDE_WILL_BE_USED_FUEL, 0.0f);
        float fuelLevel = fuelStats.getFloat(FUEL_LEVEL, 0.0f);
        float remainsFuel = fuelLevel - spendFuel;
        editor.putFloat(APP_NEW_RIDE_REMAINS_FUEL, formattedNumber(remainsFuel));
        editor.apply();
    }

    public void countImpactOnEcology(String currentFragment) {
        if (currentFragment.toLowerCase(Locale.ROOT).equals("new")) {
            Float spentFuel = fuelStats.getFloat(APP_NEW_RIDE_WILL_BE_USED_FUEL, 0.0f);
            Float gasolineEmissions = co2EmissionPer1LiterOfGasoline * spentFuel;
            Float dieselEmissions = co2EmissionPer1LiterOfDiesel * spentFuel;
            editor.putFloat(APP_NEW_RIDE_GASOLINE_EMISSIONS, formattedNumber(gasolineEmissions));
            editor.putFloat(APP_NEW_RIDE_DIESEL_EMISSIONS, formattedNumber(dieselEmissions));
        } else if (currentFragment.toLowerCase(Locale.ROOT).equals("last")) {
            Float spentFuel = fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f);
            Float gasolineEmissions = co2EmissionPer1LiterOfGasoline * spentFuel;
            Float dieselEmissions = co2EmissionPer1LiterOfDiesel * spentFuel;
            editor.putFloat(APP_PREFERENCES_GASOLINE_EMISSIONS, formattedNumber(gasolineEmissions));
            editor.putFloat(APP_PREFERENCES_DIESEL_EMISSIONS, formattedNumber(dieselEmissions));
        }
        editor.apply();
    }

    public void countSpendFuel(String currentFragment) {
        if (currentFragment.toLowerCase(Locale.ROOT).equals("new")){
            Float willBeUsed = consumptionPer1km() * fuelStats.getFloat(APP_NEW_RIDE_DISTANCE, 0.0f);
            editor.putFloat(APP_NEW_RIDE_WILL_BE_USED_FUEL, formattedNumber(willBeUsed));
        } else if (currentFragment.toLowerCase(Locale.ROOT).equals("last")){
            Float spentFuel = consumptionPer1km() * fuelStats.getFloat(APP_PREFERENCES_DISTANCE, 0.0f);
            editor.putFloat(APP_PREFERENCES_SPENT_FUEL, formattedNumber(spentFuel));
        }
        editor.apply();
    }

    public void countPrice(String currentFragment) {
        if (currentFragment.toLowerCase(Locale.ROOT).equals("new")) {
            float consumptionPer1km = consumptionPer1km();
            float distance = fuelStats.getFloat(APP_NEW_RIDE_DISTANCE, 0.0f);
            float priceFromEditText = fuelStats.getFloat(APP_NEW_RIDE_PRE_PRICE, 0.0f);
            Float price = consumptionPer1km * distance * priceFromEditText;
            editor.putFloat(APP_NEW_RIDE_PRICE, formattedNumber(price));
        } else if (currentFragment.toLowerCase(Locale.ROOT).equals("last")){
            float consumptionPer1km = consumptionPer1km();
            float distance = fuelStats.getFloat(APP_PREFERENCES_DISTANCE, 0.0f);
            float priceFromEditText = fuelStats.getFloat(APP_PREFERENCES_PRE_PRICE, 0.0f);
            Float price = consumptionPer1km * distance * priceFromEditText;
            editor.putFloat(APP_PREFERENCES_PRICE, formattedNumber(price));
        }
        editor.apply();
    }

    public void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(sharedHolderContext, R.style.WelcomeAlertDialog);
        View view = LayoutInflater.from(sharedHolderContext).inflate(R.layout.fuel_alert_dialog_layout, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.alert_dialog_fuel_ok_button).setOnClickListener(view1 -> alertDialog.dismiss());
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
    }

    public Float oldOdometerValue() {
        editor.putFloat(APP_PREFERENCES_ODOMETER_OLD, formattedNumber(fuelStats.getFloat(APP_PREFERENCES_ODOMETER, 0)));
        editor.apply();
        return fuelStats.getFloat(APP_PREFERENCES_ODOMETER_OLD, 0.0f);
    }

    public void calculatingDistance(Float oldOdometerValue, Float odometerValue) {
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

    private Float consumptionPer1km() {
        return formattedNumber(fuelStats.getFloat(CONSUMPTION_PER_100KM, 0.0f) / 100);
    }
}
