package com.example.helpwheel.ui.fuel_management.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.helpwheel.ui.fuel_management.FuelManagementFragment;
import com.example.helpwheel.ui.fuel_management.new_ride_fragments.CostOfNewRideFragment;
import com.example.helpwheel.ui.fuel_management.new_ride_fragments.DistanceOfNewRideFragment;
import com.example.helpwheel.ui.fuel_management.new_ride_fragments.EcologyImpactNewRideFragment;
import com.example.helpwheel.ui.fuel_management.new_ride_fragments.FuelInTankNewRideFragment;
import com.example.helpwheel.ui.fuel_management.new_ride_fragments.SpendFuelNewRideFragment;

public class NewRideAdapter extends FragmentStateAdapter {
    private static final int pagesCount = 5;
    public NewRideAdapter (FuelManagementFragment fuelManagementFragment){
        super(fuelManagementFragment);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new DistanceOfNewRideFragment();
            case 1: return new FuelInTankNewRideFragment();
            case 2: return new CostOfNewRideFragment();
            case 3: return new SpendFuelNewRideFragment();
            case 4: return new EcologyImpactNewRideFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return pagesCount;
    }
}
