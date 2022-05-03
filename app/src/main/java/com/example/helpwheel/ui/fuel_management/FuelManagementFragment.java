package com.example.helpwheel.ui.fuel_management;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentFuelManagementBinding;
import com.example.helpwheel.ui.fuel_management.adapter.BottomSheetVPAdapter;
import com.example.helpwheel.ui.fuel_management.adapter.LastRideAdapter;
import com.example.helpwheel.ui.fuel_management.adapter.NewRideAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayoutMediator;

import java.math.BigDecimal;
import java.util.Objects;

public class FuelManagementFragment extends Fragment implements BottomSheetCallBack {

    private FragmentFuelManagementBinding binding;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String PREF = "user";
    public static final String FUEL_TANK_CAPACITY = "fuelTankCapacity";
    public static final String FUEL_LEVEL_OLD = "fuelLevelOld";
    public static final String FUEL_LEVEL = "fuelLevel";
    public static final String APP_PREFERENCES_SPENT_FUEL = "spent_uel";
    private BottomSheetDialog bottomSheetDialog;
    SharedPreferences fuelStats, regData;


    SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFuelManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, getContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        regData = requireContext().getSharedPreferences(PREF, requireContext().MODE_PRIVATE);

        countFuelInTank();
        initializeViewPagersAndTabs();

        binding.fuelLevel.setText(String.format("%s %s", formattedNumber(fuelStats.getFloat(FUEL_LEVEL, regData.getFloat(FUEL_TANK_CAPACITY, 0.0f))), getString(R.string.litres_have_left)));
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);


        binding.fuelInputButton.setOnClickListener(v -> {
            bottomSheetDialog.show();
            ViewPager2 viewPager2BottomSheet = bottomSheetDialog.findViewById(R.id.view_pager_bottom_sheet);
            assert viewPager2BottomSheet != null;
            viewPager2BottomSheet.setAdapter(new BottomSheetVPAdapter(this));
            new TabLayoutMediator(Objects.requireNonNull(bottomSheetDialog.findViewById(R.id.tab)), viewPager2BottomSheet, (tab, position) -> {
            }).attach();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editor.apply();
        binding = null;
    }

    private void countFuelInTank() {
        if (fuelStats.getFloat(FUEL_LEVEL_OLD, 0.0f) == 0.0f) {
            float fuelLevel = regData.getFloat(FUEL_TANK_CAPACITY, 0.0f) - fuelStats.getFloat(FUEL_LEVEL_OLD, 0.0f);
            editor.putFloat(FUEL_LEVEL_OLD, fuelLevel);
        } else {
            float newFuelLevel = fuelStats.getFloat(FUEL_LEVEL_OLD, 0.0f) - fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f);
            editor.putFloat(FUEL_LEVEL_OLD, newFuelLevel);
            editor.putFloat(FUEL_LEVEL, newFuelLevel);
            binding.fuelLevel.setText(String.valueOf(formattedNumber(newFuelLevel)));
        }
        editor.apply();
    }


    private Float formattedNumber(Float number) {
        return BigDecimal.valueOf(number)
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();
    }

    private void initializeViewPagersAndTabs(){
        ViewPager2 newRideVP = binding.viewPagerNewRide;
        ViewPager2 lastRideVP = binding.viewPagerLastRide;
        lastRideVP.setAdapter(new LastRideAdapter(this));
        newRideVP.setAdapter(new NewRideAdapter(this));
        new TabLayoutMediator(binding.tabLastRide, binding.viewPagerLastRide, (tab, position) -> {
        }).attach();
        new TabLayoutMediator(binding.tabNewRide, binding.viewPagerNewRide, (tab, position) -> {
        }).attach();
    }


    @Override
    public void dismissBottomSheet() {
        bottomSheetDialog.dismiss();
    }
}