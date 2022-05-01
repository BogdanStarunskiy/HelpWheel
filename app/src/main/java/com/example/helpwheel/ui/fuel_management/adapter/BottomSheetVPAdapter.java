package com.example.helpwheel.ui.fuel_management.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.helpwheel.ui.fuel_management.FuelManagementFragment;
import com.example.helpwheel.ui.fuel_management.bottom_sheet_fragments.LastRideBottomSheetFragment;
import com.example.helpwheel.ui.fuel_management.bottom_sheet_fragments.NewRideBottomSheetFragment;

public class BottomSheetVPAdapter extends FragmentStateAdapter {
    public BottomSheetVPAdapter(FuelManagementFragment fuelManagementFragment) {
        super(fuelManagementFragment);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LastRideBottomSheetFragment();
            case 1:
                return new NewRideBottomSheetFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
