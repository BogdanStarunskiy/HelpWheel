package com.example.helpwheel.ui.fuel_management.bottom_sheet_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentNewRideBottomSheetBinding;

public class NewRideBottomSheetFragment extends Fragment {
    FragmentNewRideBottomSheetBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewRideBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String distance = binding.distanceNewRideText.getText().toString();
        String price = binding.priceNewRideText.getText().toString();
        if (!distance.isEmpty() && !price.isEmpty()) {

        } else if (!distance.isEmpty()) {


        } else if (!price.isEmpty()) {
           binding.distanceNewRideEditText.setError(getString(R.string.edit_text_odometer_error));
        }
    }
}