package com.example.helpwheel.ui.dashboard.viewPagerFragments;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentDescriptionWeatherBinding;
import com.example.helpwheel.utils.Constants;

import java.util.Locale;

public class DescriptionWeather extends Fragment {

    Constants constants;
    SharedPreferences pref;
    public boolean isChecked = true;
    FragmentDescriptionWeatherBinding binding;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDescriptionWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = requireContext().getSharedPreferences(constants.APP_PREFERENCES, getContext().MODE_PRIVATE);
        editor = pref.edit();
        setCity();
        @SuppressLint("SetTextI18n")
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(constants.WEATHER_DESC)) {
                setDesc();
            }
            if (key.equals(constants.WEATHER_TEMP)) {
                setTemp();
            }
            if (key.equals(constants.USER_CITY)){
                setCity();
            }
        };
        pref.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        if(!isChecked){
            if (pref.getString(constants.WEATHER_DESC, "error") != null) {
                binding.weatherDescription.setText(pref.getString(constants.WEATHER_DESC, "error"));
            }
            if (pref.getString(constants.WEATHER_DESC, "error") != null) {
                binding.weatherTemperature.setText(pref.getString(constants.WEATHER_TEMP, "error"));
            }}

    }

    private void setCity() {
        String city = pref.getString(constants.USER_CITY, "no data");
        binding.userCity.setText(city);
    }

    private void setTemp() {
        String temperature = pref.getString(constants.WEATHER_TEMP, "no data");
        binding.weatherTemperature.setText(temperature+requireContext().getResources().getString(R.string.degree_cels));
    }

    private void setDesc() {
        String description = pref.getString(constants.WEATHER_DESC, " no data");
        String currentWeatherState = getString(R.string.error_weather);
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