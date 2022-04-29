package com.example.helpwheel.ui.fuel_management.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.helpwheel.ui.fuel_management.FuelManagementFragment;
import com.example.helpwheel.ui.fuel_management.last_ride_fragments.CostOfLastRideFragment;
import com.example.helpwheel.ui.fuel_management.last_ride_fragments.DistanceOfLastRideFragment;
import com.example.helpwheel.ui.fuel_management.last_ride_fragments.EcologyImpactFragment;
import com.example.helpwheel.ui.fuel_management.last_ride_fragments.SpentFuelFragment;

public class NewRideAdapter extends FragmentStateAdapter {
    private static final int pagesCount = 4;
    public NewRideAdapter (FuelManagementFragment fuelManagementFragment){
        super(fuelManagementFragment);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new DistanceOfLastRideFragment();
            case 1: return new CostOfLastRideFragment();
            case 2: return new SpentFuelFragment();
            case 3: return new EcologyImpactFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return pagesCount;
    }
}
