package com.example.helpwheel.ui.fuel_management.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.helpwheel.ui.fuel_management.FuelManagementFragment
import com.example.helpwheel.ui.fuel_management.bottom_sheet_fragments.LastRideBottomSheetFragment
import com.example.helpwheel.ui.fuel_management.bottom_sheet_fragments.NewRideBottomSheetFragment
import com.example.helpwheel.ui.fuel_management.inerface.BottomSheetCallBack

class BottomSheetViewPagerAdapter(fuelManagementFragment: FuelManagementFragment): FragmentStateAdapter(fuelManagementFragment) {
    private val callback: BottomSheetCallBack = fuelManagementFragment
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> LastRideBottomSheetFragment(callback)
            1 -> NewRideBottomSheetFragment(callback)
            else -> FuelManagementFragment()
        }
    }

}