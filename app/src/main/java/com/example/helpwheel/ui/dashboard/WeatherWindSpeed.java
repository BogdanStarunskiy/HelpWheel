package com.example.helpwheel.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentWeatherWindSpeedBinding;


public class WeatherWindSpeed extends Fragment {
    public static final String WEATHER_WIND = "weatherWind";
    public static final String PREF = "user";
   FragmentWeatherWindSpeedBinding binding;
    SharedPreferences pref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherWindSpeedBinding.inflate(inflater,container, false);
        pref = requireContext().getSharedPreferences(PREF, getContext().MODE_PRIVATE);
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(WEATHER_WIND)) {
                setWind();
            }
        };

        pref.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        return binding.getRoot();
    }
    private void setWind(){
        String wind = pref.getString(WEATHER_WIND, "error");
        binding.weatherWind.setText(wind);
    }
}