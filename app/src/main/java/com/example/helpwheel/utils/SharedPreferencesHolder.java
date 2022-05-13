package com.example.helpwheel.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;
import java.util.Locale;

public class SharedPreferencesHolder {
    SharedPreferences fuelStats;
    SharedPreferences.Editor editor;
    Context sharedHolderContext;
    Constants constant;


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
            float fuelLevelOld = fuelStats.getFloat(constant.FUEL_LEVEL_OLD, fuelStats.getFloat(constant.FUEL_TANK_CAPACITY, 0.0f));
            float spentFuel = fuelStats.getFloat(constant.APP_PREFERENCES_SPENT_FUEL, 0.0f);
            float fuelLevel = fuelLevelOld - spentFuel;
            editor.putFloat(constant.FUEL_LEVEL_OLD, formattedNumber(fuelLevel));
            editor.putFloat(constant.FUEL_LEVEL, formattedNumber(fuelLevel));
            editor.apply();
    }


    public void calculateRemainsFuel() {
        float spendFuel = fuelStats.getFloat(constant.APP_NEW_RIDE_WILL_BE_USED_FUEL, 0.0f);
        float fuelLevel = fuelStats.getFloat(constant.FUEL_LEVEL, 0.0f);
        float remainsFuel = fuelLevel - spendFuel;
        editor.putFloat(constant.APP_NEW_RIDE_REMAINS_FUEL, formattedNumber(remainsFuel));
        editor.apply();
    }

    public void countImpactOnEcology(String currentFragment) {
        if (currentFragment.toLowerCase(Locale.ROOT).equals("new")) {
            Float spentFuel = fuelStats.getFloat(constant.APP_NEW_RIDE_WILL_BE_USED_FUEL, 0.0f);
            Float gasolineEmissions = constant.co2EmissionPer1LiterOfGasoline * spentFuel;
            Float dieselEmissions = constant.co2EmissionPer1LiterOfDiesel * spentFuel;
            editor.putFloat(constant.APP_NEW_RIDE_GASOLINE_EMISSIONS, formattedNumber(gasolineEmissions));
            editor.putFloat(constant.APP_NEW_RIDE_DIESEL_EMISSIONS, formattedNumber(dieselEmissions));
        } else if (currentFragment.toLowerCase(Locale.ROOT).equals("last")) {
            Float spentFuel = fuelStats.getFloat(constant.APP_PREFERENCES_SPENT_FUEL, 0.0f);
            Float gasolineEmissions = constant.co2EmissionPer1LiterOfGasoline * spentFuel;
            Float dieselEmissions = constant.co2EmissionPer1LiterOfDiesel * spentFuel;
            editor.putFloat(constant.APP_PREFERENCES_GASOLINE_EMISSIONS, formattedNumber(gasolineEmissions));
            editor.putFloat(constant.APP_PREFERENCES_DIESEL_EMISSIONS, formattedNumber(dieselEmissions));
        }
        editor.apply();
    }

    public void countSpendFuel(String currentFragment) {
        if (currentFragment.toLowerCase(Locale.ROOT).equals("new")){
            Float willBeUsed = consumptionPer1km() * fuelStats.getFloat(constant.APP_NEW_RIDE_DISTANCE, 0.0f);
            editor.putFloat(constant.APP_NEW_RIDE_WILL_BE_USED_FUEL, formattedNumber(willBeUsed));
        } else if (currentFragment.toLowerCase(Locale.ROOT).equals("last")){
            Float spentFuel = consumptionPer1km() * fuelStats.getFloat(constant.APP_PREFERENCES_DISTANCE, 0.0f);
            editor.putFloat(constant.APP_PREFERENCES_SPENT_FUEL, formattedNumber(spentFuel));
        }
        editor.apply();
    }

    public void countPrice(String currentFragment) {
        if (currentFragment.toLowerCase(Locale.ROOT).equals("new")) {
            float consumptionPer1km = consumptionPer1km();
            float distance = fuelStats.getFloat(constant.APP_NEW_RIDE_DISTANCE, 0.0f);
            float priceFromEditText = fuelStats.getFloat(constant.APP_NEW_RIDE_PRE_PRICE, 0.0f);
            Float price = consumptionPer1km * distance * priceFromEditText;
            editor.putFloat(constant.APP_NEW_RIDE_PRICE, formattedNumber(price));
        } else if (currentFragment.toLowerCase(Locale.ROOT).equals("last")){
            float consumptionPer1km = consumptionPer1km();
            float distance = fuelStats.getFloat(constant.APP_PREFERENCES_DISTANCE, 0.0f);
            float priceFromEditText = fuelStats.getFloat(constant.APP_PREFERENCES_PRE_PRICE, 0.0f);
            Float price = consumptionPer1km * distance * priceFromEditText;
            editor.putFloat(constant.APP_PREFERENCES_PRICE, formattedNumber(price));
        }
        editor.apply();
    }


    public Float oldOdometerValue() {
        editor.putFloat(constant.APP_PREFERENCES_ODOMETER_OLD, formattedNumber(fuelStats.getFloat(constant.APP_PREFERENCES_ODOMETER, 0)));
        editor.apply();
        return fuelStats.getFloat(constant.APP_PREFERENCES_ODOMETER_OLD, 0.0f);
    }

    public void calculatingDistance(Float oldOdometerValue, Float odometerValue) {
            editor.putFloat(constant.APP_PREFERENCES_DISTANCE, formattedNumber(odometerValue - oldOdometerValue));
            editor.apply();
    }


    public void removeLastRideData (){
        editor.remove(constant.APP_PREFERENCES_ODOMETER);
        editor.remove(constant.APP_PREFERENCES_DISTANCE);
        editor.remove(constant.APP_PREFERENCES_PRICE);
        editor.remove(constant.APP_PREFERENCES_DIESEL_EMISSIONS);
        editor.remove(constant.APP_PREFERENCES_GASOLINE_EMISSIONS);
        editor.remove(constant.APP_PREFERENCES_SPENT_FUEL);
        editor.apply();
    }

    public void updateFuelTankCapacity () {
        float fuelTankCapacity = fuelStats.getFloat(constant.FUEL_TANK_CAPACITY, 0.0f);
        float fuelTankCapacityOld = fuelStats.getFloat(constant.FUEL_TANK_CAPACITY_OLD, 0.0f);
        float differenceBetweenFuelTanks = fuelTankCapacity - fuelTankCapacityOld;
        float currentFuelLevel = fuelStats.getFloat(constant.FUEL_LEVEL, 0.0f) + differenceBetweenFuelTanks;
        editor.putFloat(constant.FUEL_LEVEL, formattedNumber(currentFuelLevel));
        editor.apply();
    }

    public Float formattedNumber(Float number) {
        return BigDecimal.valueOf(number)
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();
    }

    private Float consumptionPer1km() {
        return formattedNumber(fuelStats.getFloat(constant.CONSUMPTION_PER_100KM, 0.0f) / 100);
    }
}
