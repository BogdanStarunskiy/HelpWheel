package com.example.helpwheel.ui.dashboard;


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

        @SuppressLint("SetTextI18n")
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(constants.WEATHER_DESC)) {
                setDesc();
            }
            if (key.equals(constants.WEATHER_TEMP)) {
                setTemp();
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

    private void setTemp() {
        String temperature = pref.getString(constants.WEATHER_TEMP, "no data");
        binding.weatherTemperature.setText(temperature+requireContext().getResources().getString(R.string.degree_cels));
    }

    private void setDesc() {
        String description = pref.getString(constants.WEATHER_DESC, " no data");
        binding.weatherDescription.setText(description);
        LottieAnimationView animationView = binding.ImageViewDescription;
        switch (description) {
            case "clear sky":
                animationView.setAnimation(R.raw.ic_clear_sky);
                animationView.playAnimation();
                break;
            case "few clouds":
                animationView.setAnimation(R.raw.ic_few_clouds);
                animationView.playAnimation();
                break;
            case "scattered clouds":
            case "broken clouds":
            case "overcast clouds":
                animationView.setAnimation(R.raw.ic_clouds);
                animationView.playAnimation();
                break;
            case "shower rain":
            case "rain":
                animationView.setAnimation(R.raw.ic_rain);
                animationView.playAnimation();
                break;
            case "thunderstorm":
                animationView.setAnimation(R.raw.ic_thunderstorm);
                animationView.playAnimation();
                break;
            case "snow":
                animationView.setAnimation(R.raw.ic_snow);
                animationView.playAnimation();
                break;
            case "mist":
                animationView.setAnimation(R.raw.ic_mist);
                animationView.playAnimation();
                break;
        }
    }

}