package com.example.helpwheel.ui.dashboard.viewPagerFragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentDescriptionWeatherBinding;
import com.example.helpwheel.ui.dashboard.DashboardViewModel;
import com.example.helpwheel.utils.Constants;

public class DescriptionWeather extends Fragment {
    public boolean isChecked = true;
    FragmentDescriptionWeatherBinding binding;
    DashboardViewModel dashboardViewModel;

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDescriptionWeatherBinding.inflate(inflater, container, false);
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initObservers();
    }

    private void initObservers() {
        dashboardViewModel.getTemperature().observe(getViewLifecycleOwner(), this::setTemp);
        dashboardViewModel.getDescription().observe(getViewLifecycleOwner(), this::setDesc);
        dashboardViewModel.getCity().observe(getViewLifecycleOwner(), this::setCity);
    }

    private void setCity(String userCity) {
        binding.userCity.setText(userCity);
    }

    private void setTemp(String temperature) {
        binding.weatherTemperature.setText(String.format("%s%s", temperature, getString(R.string.degree_cels)));
    }

    private void setDesc(String description) {
        String currentWeatherState;
        LottieAnimationView animationView = binding.ImageViewDescription;
        if(description.contains("thunderstorm")) {
            currentWeatherState = getString(R.string.thunderstorm);
            animationView.setAnimation(R.raw.ic_thunderstorm);
            animationView.playAnimation();
        } else if (description.contains("drizzle")){
            currentWeatherState = getString(R.string.drizzle);
            animationView.setAnimation(R.raw.ic_drizzle);
            animationView.playAnimation();
        } else if (description.contains("rain")){
            currentWeatherState = getString(R.string.rain);
            animationView.setAnimation(R.raw.ic_rain);
            animationView.playAnimation();
        } else if (description.contains("snow")){
            currentWeatherState = getString(R.string.snow);
            animationView.setAnimation(R.raw.ic_snow);
            animationView.playAnimation();
        } else if (description.contains("clouds")){
            currentWeatherState = getString(R.string.cloudy);
            animationView.setAnimation(R.raw.ic_clouds);
            animationView.playAnimation();
        } else if (description.contains("clear")){
            currentWeatherState = getString(R.string.sunny);
            animationView.setAnimation(R.raw.ic_clear_sky);
            animationView.playAnimation();
        } else if (description.equals("squalls")){
            currentWeatherState = getString(R.string.squalls);
            animationView.setAnimation(R.raw.ic_wind);
            animationView.playAnimation();
        } else if (description.equals("tornado")){
            currentWeatherState = getString(R.string.tornado);
            animationView.setAnimation(R.raw.ic_tornado);
            animationView.playAnimation();
        } else {
            currentWeatherState = getString(R.string.fog);
            animationView.setAnimation(R.raw.ic_mist);
            animationView.playAnimation();
        }

        if (description.equals("few clouds")){
            currentWeatherState = getString(R.string.few_clouds);
            animationView.setAnimation(R.raw.ic_few_clouds);
            animationView.playAnimation();
        }

        binding.weatherDescription.setText(currentWeatherState);
    }
}