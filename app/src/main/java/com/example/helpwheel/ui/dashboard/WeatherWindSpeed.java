package com.example.helpwheel.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.helpwheel.databinding.FragmentWeatherWindSpeedBinding;


public class WeatherWindSpeed extends Fragment {
    public static final String WEATHER_WIND = "weatherWind";
    public static final String PREF = "user";
    FragmentWeatherWindSpeedBinding binding;
    SharedPreferences pref;
    public static final String WEATHER_WIND_AUTO = "weatherWindAuto";
    private boolean isChecked;

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherWindSpeedBinding.inflate(inflater, container, false);
        pref = requireContext().getSharedPreferences(PREF, getContext().MODE_PRIVATE);
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(WEATHER_WIND)) {
                setWind();
            } else if (key.equals(WEATHER_WIND_AUTO)) {

                binding.weatherWind.setText(pref.getString(WEATHER_WIND_AUTO, "no data"));
            }
        };

        pref.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        if (isChecked) {
            if (pref.getString(WEATHER_WIND, "error") != null) {
                binding.weatherWind.setText(pref.getString(WEATHER_WIND, "error"));
            }
        }
        return binding.getRoot();
    }

    private void setWind() {
        String wind = pref.getString(WEATHER_WIND, "error");
        binding.weatherWind.setText(wind);
    }
}