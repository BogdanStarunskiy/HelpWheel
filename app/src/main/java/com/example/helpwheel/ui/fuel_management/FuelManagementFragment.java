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
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentFuelManagementBinding;
import com.example.helpwheel.ui.fuel_management.adapter.BottomSheetVPAdapter;
import com.example.helpwheel.ui.fuel_management.adapter.LastRideAdapter;
import com.example.helpwheel.ui.fuel_management.adapter.NewRideAdapter;
import com.example.helpwheel.ui.fuel_management.inerface.BottomSheetCallBack;
import com.example.helpwheel.utils.Constants;
import com.example.helpwheel.utils.SharedPreferencesHolder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class FuelManagementFragment extends Fragment implements BottomSheetCallBack {

    private FragmentFuelManagementBinding binding;
    private BottomSheetDialog bottomSheetDialogFuelStats, bottomSheetDialogFuelInTank;
    private View currentView;
    private Bundle currentBundle;
    Constants constants;
    SharedPreferences fuelStats;
    SharedPreferences.Editor editor;
    SharedPreferencesHolder sharedPreferencesHolder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFuelManagementBinding.inflate(inflater, container, false);
        sharedPreferencesHolder = new SharedPreferencesHolder(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fuelStats = requireContext().getSharedPreferences(constants.APP_PREFERENCES, getContext().MODE_PRIVATE);
        editor = fuelStats.edit();
        sharedPreferencesHolder.setEditor(editor);
        sharedPreferencesHolder.setFuelStats(fuelStats);

        initializeViewPagersAndTabs();
        initializeBottomSheetDialogs();
        getViewAndBundle(view, savedInstanceState);

        if (fuelStats.getFloat(constants.APP_PREFERENCES_ODOMETER, 0.0f) == 0.0f) {
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_notifications_to_firstOdometerReadingFragment);
        }

        displayFuelInTank();

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
            ConstraintLayout container = bottomSheetDialogFuelInTank.findViewById(R.id.full_tank);
            assert submit != null;
            submit.setOnClickListener(v1 -> {
                assert tankFuelLevel != null;
                if (!tankFuelLevel.getText().toString().isEmpty()) {
                    if (Float.parseFloat(tankFuelLevel.getText().toString()) > fuelStats.getFloat(constants.FUEL_TANK_CAPACITY, 0.0f))
                        showDialog();
                    else {
                        editor.putFloat(constants.FUEL_LEVEL_OLD, sharedPreferencesHolder.formattedNumber(Float.parseFloat(tankFuelLevel.getText().toString())));
                        editor.putFloat(constants.FUEL_LEVEL, sharedPreferencesHolder.formattedNumber(Float.parseFloat(tankFuelLevel.getText().toString())));
                        editor.apply();
                        bottomSheetDialogFuelInTank.dismiss();
                        sharedPreferencesHolder.calculateRemainsFuel();
                        updateUI();
                    }
                } else {
                    bottomSheetDialogFuelInTank.dismiss();
                }
            });
            assert container != null;
            container.setOnClickListener(v2 -> {
                assert tankFuelLevel != null;
                tankFuelLevel.setText(String.valueOf(fuelStats.getFloat(constants.FUEL_TANK_CAPACITY, 0.0f)));
            });
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editor.apply();
        binding = null;
    }


    private void initializeBottomSheetDialogs(){
        bottomSheetDialogFuelStats = new BottomSheetDialog(requireContext());
        bottomSheetDialogFuelStats.setContentView(R.layout.bottom_sheet_dialog_fuel_stats);
        bottomSheetDialogFuelInTank = new BottomSheetDialog(requireContext());
        bottomSheetDialogFuelInTank.setContentView(R.layout.bottom_sheet_fuel_in_tank);
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

    private void displayFuelInTank(){
        float fuelLevel = fuelStats.getFloat(constants.FUEL_LEVEL, fuelStats.getFloat(constants.FUEL_TANK_CAPACITY, 0.0f));
        if (fuelLevel <= 0.0f){
            editor.putFloat(constants.FUEL_LEVEL, 0.0f);
            editor.apply();
            binding.fuelLevel.setText(String.format("%s %s", 0.0f , getString(R.string.litres_have_left)));
            binding.fuelLevel.setTextColor(requireContext().getColor(R.color.red));
        } else {
            binding.fuelLevel.setTextColor(requireContext().getColor(R.color.white));
            binding.fuelLevel.setText(String.format("%s %s", fuelLevel, getString(R.string.litres_have_left)));
            editor.putFloat(constants.FUEL_LEVEL, fuelLevel);
            editor.apply();
        }
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.edit_fuel_level_error_diallog, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        Button okBtn = dialog.findViewById(R.id.ok_button);
        assert okBtn != null;
        okBtn.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void dismissBottomSheet() {
        bottomSheetDialogFuelStats.dismiss();
        updateUI();
    }

}