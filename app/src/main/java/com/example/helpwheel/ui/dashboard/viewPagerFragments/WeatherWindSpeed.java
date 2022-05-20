package com.example.helpwheel.ui.dashboard.viewPagerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentWeatherWindSpeedBinding;
import com.example.helpwheel.ui.dashboard.DashboardViewModel;


public class WeatherWindSpeed extends Fragment {
    FragmentWeatherWindSpeedBinding binding;
    DashboardViewModel dashboardViewModel;
    boolean isChecked;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherWindSpeedBinding.inflate(inflater, container, false);
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dashboardViewModel.getWind().observe(getViewLifecycleOwner(), this::setWind);
    }

    private void setWind(String wind) {
        binding.weatherWind.setText(String.format("%s%s", wind, requireContext().getResources().getString(R.string.speed)));
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}