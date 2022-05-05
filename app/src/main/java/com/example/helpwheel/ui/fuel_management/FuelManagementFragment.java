package com.example.helpwheel.ui.fuel_management;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentFuelManagementBinding;
import com.example.helpwheel.ui.fuel_management.adapter.BottomSheetVPAdapter;
import com.example.helpwheel.ui.fuel_management.adapter.LastRideAdapter;
import com.example.helpwheel.ui.fuel_management.adapter.NewRideAdapter;
import com.example.helpwheel.ui.fuel_management.inerface.BottomSheetCallBack;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayoutMediator;

import java.math.BigDecimal;
import java.util.Objects;

public class FuelManagementFragment extends Fragment implements BottomSheetCallBack {

    private FragmentFuelManagementBinding binding;
    public static final String APP_PREFERENCES = "fuelStats";
    public static final String PREF = "user";
    public static final String FUEL_TANK_CAPACITY = "fuel_tank_capacity";
    public static final String FUEL_LEVEL = "fuel_level";
    public static final String APP_PREFERENCES_SPENT_FUEL = "spent_fuel";
    private BottomSheetDialog bottomSheetDialogFuelStats, bottomSheetDialogFuelInTank;
    private View currentView;
    private Bundle currentBundle;
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

        initializeViewPagersAndTabs();

        bottomSheetDialogFuelStats = new BottomSheetDialog(requireContext());
        bottomSheetDialogFuelStats.setContentView(R.layout.bottom_sheet_dialog_fuel_stats);
        bottomSheetDialogFuelInTank = new BottomSheetDialog(requireContext());
        bottomSheetDialogFuelInTank.setContentView(R.layout.bottom_sheet_fuel_in_tank);

        float fuelLevel = formattedNumber(fuelStats.getFloat(FUEL_LEVEL, 0.0f));
        binding.fuelLevel.setText(String.format("%s %s", fuelLevel, getString(R.string.litres_have_left)));

        getViewAndBundle(view, savedInstanceState);
        binding.fuelInputButton.setOnClickListener(v -> {
            bottomSheetDialogFuelStats.show();
            ViewPager2 viewPager2BottomSheet = bottomSheetDialogFuelStats.findViewById(R.id.view_pager_bottom_sheet);
            assert viewPager2BottomSheet != null;
            viewPager2BottomSheet.setAdapter(new BottomSheetVPAdapter(this));
            new TabLayoutMediator(Objects.requireNonNull(bottomSheetDialogFuelStats.findViewById(R.id.tab)), viewPager2BottomSheet, (tab, position) -> {
            }).attach();
        });

        binding.editButton.setOnClickListener(v -> {
            bottomSheetDialogFuelInTank.show();
            EditText tankFuelLevel = bottomSheetDialogFuelInTank.findViewById(R.id.fuel_in_tank_text);
            Button submit = bottomSheetDialogFuelInTank.findViewById(R.id.submit_btn_fuel_in_tank);

            assert submit != null;
            submit.setOnClickListener(v1 -> {
                assert tankFuelLevel != null;
                if (!tankFuelLevel.getText().toString().isEmpty()) {
                    editor.putFloat(FUEL_LEVEL, Float.parseFloat(tankFuelLevel.getText().toString()));
                    editor.apply();
                    bottomSheetDialogFuelInTank.dismiss();
                    updateUI();
                } else {
                    bottomSheetDialogFuelInTank.dismiss();
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editor.apply();
        binding = null;
    }

    private void countFuelInTank() {
        if (fuelStats.getFloat(FUEL_LEVEL, 0.0f) == 0.0f) {
            float spentFuel = fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f);
            float fuelTankCapacity = regData.getFloat(FUEL_TANK_CAPACITY, 0.0f);
            float fuelLevel = fuelTankCapacity - spentFuel;
            editor.putFloat(FUEL_LEVEL, fuelLevel);
        } else{
            float lastFuelLevel = fuelStats.getFloat(FUEL_LEVEL, 0.0f);
            float spentFuel = fuelStats.getFloat(APP_PREFERENCES_SPENT_FUEL, 0.0f);
            float fuelLevel = lastFuelLevel - spentFuel;
            editor.putFloat(FUEL_LEVEL, fuelLevel);
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

    public void getViewAndBundle(View view, Bundle savedInstanceState){
        currentView = view;
        currentBundle = savedInstanceState;
    }

    private void updateUI(){
        onViewCreated(currentView, currentBundle);
    }

    @Override
    public void dismissBottomSheet() {
        bottomSheetDialogFuelStats.dismiss();
        countFuelInTank();
        updateUI();
    }
}