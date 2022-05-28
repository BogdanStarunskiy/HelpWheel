package com.example.helpwheel.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.helpwheel.App
import java.util.*
import kotlin.math.roundToInt

class SharedPreferencesHolder(val fuelStats: SharedPreferences,
                              private val editor: SharedPreferences.Editor
) {

    fun countFuelInTank() {
        val fuelLevelOld = fuelStats.getFloat(FUEL_LEVEL_OLD, fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f))
        val spentFuel = fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f)
        val fuelLevel = fuelLevelOld - spentFuel
            editor.putFloat(FUEL_LEVEL_OLD, formattedNumber(fuelLevel))
            editor.putFloat(FUEL_LEVEL, formattedNumber(fuelLevel))
            editor.apply()
    }

    fun countFuelInTank2() {
        val preferences = App.instance.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        val editor = preferences.edit()
        val fuelLevelOld = fuelStats.getFloat(FUEL_LEVEL_OLD, fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f))
        val spentFuel = fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f)
        val fuelLevel = fuelLevelOld - spentFuel
        editor.putFloat(FUEL_LEVEL_OLD, formattedNumber(fuelLevel))
        editor.putFloat(FUEL_LEVEL, formattedNumber(fuelLevel))
        editor.apply()
    }


    fun calculateRemainsFuel() {
        val spendFuel = fuelStats.getFloat(APP_NEW_RIDE_WILL_BE_USED_FUEL, 0.0f)
        val fuelLevel = fuelStats.getFloat(FUEL_LEVEL, 0.0f)
        val remainsFuel = fuelLevel - spendFuel
        editor.putFloat(APP_NEW_RIDE_REMAINS_FUEL, formattedNumber(remainsFuel)).apply()
    }

    fun countImpactOnEcology(currentFragment: String) {
        if (currentFragment.lowercase(Locale.ROOT) == "new") {
            val spentFuel = fuelStats.getFloat(APP_NEW_RIDE_WILL_BE_USED_FUEL, 0.0f)
            val gasolineEmissions = co2EmissionPer1LiterOfGasoline * spentFuel
            val dieselEmissions = co2EmissionPer1LiterOfDiesel * spentFuel
            editor.putFloat(APP_NEW_RIDE_GASOLINE_EMISSIONS, formattedNumber(gasolineEmissions))
            editor.putFloat(APP_NEW_RIDE_DIESEL_EMISSIONS, formattedNumber(dieselEmissions))
        } else if (currentFragment.lowercase(Locale.ROOT) == "last") {
            val spentFuel = fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f)
            val gasolineEmissions = co2EmissionPer1LiterOfGasoline * spentFuel
            val dieselEmissions = co2EmissionPer1LiterOfDiesel * spentFuel
            editor.putFloat(APP_PREFERENCES_GASOLINE_EMISSIONS, formattedNumber(gasolineEmissions))
            editor.putFloat(APP_PREFERENCES_DIESEL_EMISSIONS, formattedNumber(dieselEmissions))
        }
        editor.apply()
    }

    fun countSpendFuel(currentFragment: String) {
        if (currentFragment.lowercase(Locale.ROOT) == "new"){
            val willBeUsed = consumptionPer1km() * fuelStats.getFloat(APP_NEW_RIDE_DISTANCE, 0.0f)
            editor.putFloat(APP_NEW_RIDE_WILL_BE_USED_FUEL, formattedNumber(willBeUsed))
        } else if (currentFragment.lowercase(Locale.ROOT) == "last"){
            val spentFuel = consumptionPer1km() * fuelStats.getFloat(APP_PREFERENCES_DISTANCE, 0.0f)
            editor.putFloat(APP_PREFERENCES_SPENT_FUEL, formattedNumber(spentFuel))
        }
        editor.apply()
    }

    fun countPrice(currentFragment: String) {
        if (currentFragment.lowercase(Locale.ROOT) == "new") {
            val consumptionPer1km = consumptionPer1km()
            val distance = fuelStats.getFloat(APP_NEW_RIDE_DISTANCE, 0.0f)
            val priceFromEditText = fuelStats.getFloat(APP_NEW_RIDE_PRE_PRICE, 0.0f)
            val price = consumptionPer1km * distance * priceFromEditText
            editor.putFloat(APP_NEW_RIDE_PRICE, formattedNumber(price))
        } else if (currentFragment.lowercase(Locale.ROOT) == "last"){
            val consumptionPer1km = consumptionPer1km()
            val distance = fuelStats.getFloat(APP_PREFERENCES_DISTANCE, 0.0f)
            val priceFromEditText = fuelStats.getFloat(APP_PREFERENCES_PRE_PRICE, 0.0f)
            val price = consumptionPer1km * distance * priceFromEditText
            editor.putFloat(APP_PREFERENCES_PRICE, formattedNumber(price))
        }
        editor.apply()
    }


    fun oldOdometerValue(): Float{
        editor.putFloat(APP_PREFERENCES_ODOMETER_OLD, formattedNumber(fuelStats.getFloat(APP_PREFERENCES_ODOMETER, 0.0f))).apply()
        return fuelStats.getFloat(APP_PREFERENCES_ODOMETER_OLD, 0.0f)
    }

    fun calculatingDistance(oldOdometerValue: Float, odometerValue: Float) {
            editor.putFloat(APP_PREFERENCES_DISTANCE, formattedNumber(odometerValue - oldOdometerValue)).apply()
    }


    fun removeLastRideData (){
        editor.remove(APP_PREFERENCES_ODOMETER)
        editor.remove(APP_PREFERENCES_DISTANCE)
        editor.remove(APP_PREFERENCES_PRICE)
        editor.remove(APP_PREFERENCES_DIESEL_EMISSIONS)
        editor.remove(APP_PREFERENCES_GASOLINE_EMISSIONS)
        editor.remove(APP_PREFERENCES_SPENT_FUEL)
        editor.apply()
    }

    fun updateFuelTankCapacity () {
        val fuelTankCapacity = fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f)
        val fuelTankCapacityOld = fuelStats.getFloat(FUEL_TANK_CAPACITY_OLD, 0.0f)
        val differenceBetweenFuelTanks = fuelTankCapacity - fuelTankCapacityOld
        val currentFuelLevel = fuelStats.getFloat(FUEL_LEVEL, 0.0f) + differenceBetweenFuelTanks
        editor.putFloat(FUEL_LEVEL, formattedNumber(currentFuelLevel)).apply()
    }

    fun formattedNumber(number: Float): Float {
        return (number * 100.0).roundToInt() / 100.0f
    }

    private fun consumptionPer1km(): Float{
        return formattedNumber(fuelStats.getFloat(CONSUMPTION_PER_100KM, 0.0f) / 100)
    }
}
