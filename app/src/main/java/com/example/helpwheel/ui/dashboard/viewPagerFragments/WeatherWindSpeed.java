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
        initObservers();
    }

    private void initObservers(){
        dashboardViewModel.getWind().observe(getViewLifecycleOwner(), this::setWind);
        dashboardViewModel.getIsGPSTurnedOn().observe(getViewLifecycleOwner(), aBoolean -> {
            if (!aBoolean)
                setDataIfGPSIsTurnedOff();
            else
                setDataIfGPSIsTurnedOn();
        });
    }

    private void setWind(String wind) {
        binding.weatherWind.setText(String.format("%s%s", wind , requireContext().getResources().getString(R.string.speed)));
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private void setDataIfGPSIsTurnedOff() {
        binding.imageViewWind.setAnimation(R.raw.ic_no_gps);
        binding.imageViewWind.playAnimation();
        binding.imageViewWind.setScaleX(1);
        binding.imageViewWind.setScaleY(1);
        binding.weatherWind.setText(getString(R.string.no_gps_text));
    }

    private void setDataIfGPSIsTurnedOn() {
        binding.imageViewWind.setAnimation(R.raw.ic_wind);
        binding.imageViewWind.playAnimation();
    }
}