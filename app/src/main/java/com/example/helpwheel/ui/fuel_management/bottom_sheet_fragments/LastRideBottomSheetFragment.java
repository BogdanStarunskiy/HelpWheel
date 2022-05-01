package com.example.helpwheel.ui.fuel_management.bottom_sheet_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentLastRideBottomSheetBinding;
import com.example.helpwheel.ui.fuel_management.BottomSheetCallBack;


public class LastRideBottomSheetFragment extends Fragment {
    FragmentLastRideBottomSheetBinding binding;
    BottomSheetCallBack callBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLastRideBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.submitBtnFuel.setOnClickListener(v -> {
            String odometerValue = binding.odometerText.getText().toString();
            String priceValue = binding.priceText.getText().toString();
            if (!odometerValue.isEmpty() && !priceValue.isEmpty()) {

            } else if (!odometerValue.isEmpty()) {

            } else if (!priceValue.isEmpty()) {
                binding.odometerEditText.setError(getString(R.string.edit_text_odometer_error));
            } else {
                onViewCreated(view, savedInstanceState);
            }
            callBack.dismissBottomSheet();

        });
    }
    public void regCallBack(BottomSheetCallBack callBack){
        this.callBack = callBack;
    }
}
