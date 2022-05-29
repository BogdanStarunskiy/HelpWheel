package com.example.helpwheel.ui.trip.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.helpwheel.ui.trip.TripFragment
import com.example.helpwheel.ui.trip.bottom_sheet_fragments.LastRideBottomSheetFragment
import com.example.helpwheel.ui.trip.bottom_sheet_fragments.NewRideBottomSheetFragment
import com.example.helpwheel.ui.trip.inerface.BottomSheetCallBack

class BottomSheetViewPagerAdapter(tripFragment: TripFragment): FragmentStateAdapter(tripFragment) {
    private val callback: BottomSheetCallBack = tripFragment
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> LastRideBottomSheetFragment(callback)
            1 -> NewRideBottomSheetFragment(callback)
            else -> TripFragment()
        }
    }

}