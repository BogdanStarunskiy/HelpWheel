package com.example.helpwheel.utils

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.ui.dashboard.DashboardViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import java.io.IOException
import java.util.*

object Location {
    @SuppressLint("MissingPermission")
    fun getMyLocation(mFusedLocationClient: FusedLocationProviderClient, fragmentActivity: FragmentActivity) {
        val dashboardViewModel = ViewModelProvider(fragmentActivity)[DashboardViewModel::class.java]
        mFusedLocationClient.lastLocation
            .addOnSuccessListener(fragmentActivity) { location ->
                if (location != null) {
                    if (location.latitude != 0.0 && location.longitude != 0.0) {
                        dashboardViewModel.setIsPermissionGranted(true)
                        dashboardViewModel.setIsGpsTurnedOn(true)
                        dashboardViewModel.getWeatherDataAutomatically(
                            location.latitude,
                            location.longitude,
                            KEY
                        )
                        val gcd = Geocoder(fragmentActivity, Locale.getDefault())
                        var addresses: List<Address>? = null
                        try {
                            addresses =
                                gcd.getFromLocation(
                                    location.latitude,
                                    location.longitude,
                                    1
                                )
                        } catch (e: IOException) {
                            e.stackTrace
                        }
                        if (addresses != null && addresses.isNotEmpty()) {
                            val city = addresses[0].locality
                            dashboardViewModel.setCity(city)
                        }
                    }

                } else
                    dashboardViewModel.setIsGpsTurnedOn(false)
            }
    }
}