package com.example.helpwheel.ui.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentCostOfLastRideBinding;
import com.example.helpwheel.databinding.FragmentDescriptionWeatherBinding;

public class DescriptionWeather extends Fragment {



    FragmentDescriptionWeatherBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDescriptionWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}