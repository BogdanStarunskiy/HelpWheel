package com.example.helpwheel.ui.fuel_management.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.helpwheel.ui.fuel_management.FuelManagementFragment

class LastRideAdapter(fuelManagementFragment: FuelManagementFragment,
                      private val fragmentArray: ArrayList<Fragment>
): FragmentStateAdapter(fuelManagementFragment) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentArray[position]
    }
}