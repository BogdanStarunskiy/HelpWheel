package com.example.helpwheel.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.helpwheel.App
import kotlin.math.roundToInt

object SharedPreferencesHolder {
    private val fuelStats: SharedPreferences =
        App.instance.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = fuelStats.edit()

    fun countFuelInTank() {
        val fuelLevelOld =
            fuelStats.getFloat(FUEL_LEVEL_OLD, fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f))
        val spentFuel = fuelStats.getFloat(SPENT_FUEL_LAST_RIDE, 0.0f)
        val fuelLevel = fuelLevelOld - spentFuel
        editor.putFloat(FUEL_LEVEL_OLD, formattedNumber(fuelLevel))
        editor.putFloat(FUEL_LEVEL, formattedNumber(fuelLevel))
        editor.apply()
    }

    fun remainsFuel(): Float {
        val spendFuel = fuelStats.getFloat(WILL_BE_USED_FUEL, 0.0f)
        val fuelLevel = fuelStats.getFloat(FUEL_LEVEL, 0.0f)
        val remainsFuel = fuelLevel - spendFuel
        editor.putFloat(REMAINS_FUEL, formattedNumber(remainsFuel)).apply()
        return formattedNumber(remainsFuel)
    }

    fun co2GasolineEmissionLastRide(): Float {
        val spentFuel = fuelStats.getFloat(SPENT_FUEL_LAST_RIDE, 0.0f)
        val gasolineEmissions = co2EmissionPer1LiterOfGasoline * spentFuel
        editor.putFloat(GASOLINE_EMISSIONS_LAST_RIDE, formattedNumber(gasolineEmissions))
            .apply()
        return formattedNumber(gasolineEmissions)
    }

    fun co2DieselEmissionLastRide(): Float {
        val spentFuel = fuelStats.getFloat(SPENT_FUEL_LAST_RIDE, 0.0f)
        val dieselEmissions = co2EmissionPer1LiterOfDiesel * spentFuel
        editor.putFloat(DIESEL_EMISSIONS_LAST_RIDE, formattedNumber(dieselEmissions)).apply()
        return formattedNumber(dieselEmissions)
    }

    fun co2GasolineEmissionNewRide(): Float {
        val spentFuel = fuelStats.getFloat(WILL_BE_USED_FUEL, 0.0f)
        val gasolineEmissions = co2EmissionPer1LiterOfGasoline * spentFuel
        editor.putFloat(GASOLINE_EMISSIONS_NEW_RIDE, formattedNumber(gasolineEmissions)).apply()
        return formattedNumber(gasolineEmissions)
    }

    fun co2DieselEmissionNewRide(): Float {
        val spentFuel = fuelStats.getFloat(WILL_BE_USED_FUEL, 0.0f)
        val dieselEmissions = co2EmissionPer1LiterOfDiesel * spentFuel
        editor.putFloat(DIESEL_EMISSIONS_NEW_RIDE, formattedNumber(dieselEmissions)).apply()
        return formattedNumber(dieselEmissions)
    }

    fun countSpentFuelLastRide(): Float {
        val spentFuel = consumptionPer1km() * fuelStats.getFloat(DISTANCE_LAST_RIDE, 0.0f)
        editor.putFloat(SPENT_FUEL_LAST_RIDE, formattedNumber(spentFuel)).apply()
        return formattedNumber(spentFuel)
    }

    fun countSpendFuelNewRide(): Float {
        val willBeUsed = consumptionPer1km() * fuelStats.getFloat(DISTANCE_NEW_RIDE, 0.0f)
        editor.putFloat(WILL_BE_USED_FUEL, formattedNumber(willBeUsed)).apply()
        return formattedNumber(willBeUsed)
    }

    fun priceNewRide(): Float {
        val distance = fuelStats.getFloat(DISTANCE_NEW_RIDE, 0.0f)
        val priceFromEditText = fuelStats.getFloat(COST_NEW_RIDE, 0.0f)
        val price = consumptionPer1km() * distance * priceFromEditText
        editor.putFloat(COST_NEW_RIDE, formattedNumber(price)).apply()
        return formattedNumber(price)
    }

    fun priceLastRide(): Float {
        val distance = fuelStats.getFloat(DISTANCE_LAST_RIDE, 0.0f)
        val priceFromEditText = fuelStats.getFloat(COST_LAST_RIDE, 0.0f)
        val price = consumptionPer1km() * distance * priceFromEditText
        editor.putFloat(COST_LAST_RIDE, formattedNumber(price)).apply()
        return formattedNumber(price)
    }


    fun oldOdometerValue(): Float {
        editor.putFloat(
            ODOMETER_OLD,
            formattedNumber(fuelStats.getFloat(ODOMETER, 0.0f))
        ).apply()
        return formattedNumber(fuelStats.getFloat(ODOMETER_OLD, 0.0f))
    }

    fun distance(oldOdometerValue: Float, odometerValue: Float): Float {
        val distance = odometerValue - oldOdometerValue
        editor.putFloat(DISTANCE_LAST_RIDE, formattedNumber(distance)).apply()
        return formattedNumber(distance)
    }


    fun removeLastRideData() {
        editor.remove(ODOMETER)
        editor.remove(DISTANCE_LAST_RIDE)
        editor.remove(COST_LAST_RIDE)
        editor.remove(DIESEL_EMISSIONS_LAST_RIDE)
        editor.remove(GASOLINE_EMISSIONS_LAST_RIDE)
        editor.remove(SPENT_FUEL_LAST_RIDE)
        editor.apply()
    }

    fun updateFuelTankCapacity() {
        val fuelTankCapacity = fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f)
        val fuelTankCapacityOld = fuelStats.getFloat(FUEL_TANK_CAPACITY_OLD, 0.0f)
        val differenceBetweenFuelTanks = fuelTankCapacity - fuelTankCapacityOld
        val currentFuelLevel = fuelStats.getFloat(FUEL_LEVEL, 0.0f) + differenceBetweenFuelTanks
        editor.putFloat(FUEL_LEVEL, formattedNumber(currentFuelLevel)).apply()
    }

    fun formattedNumber(number: Float): Float {
        return (number * 100.0).roundToInt() / 100.0f
    }

    private fun consumptionPer1km(): Float {
        return formattedNumber(fuelStats.getFloat(CONSUMPTION_PER_100KM, 0.0f) / 100)
    }
}