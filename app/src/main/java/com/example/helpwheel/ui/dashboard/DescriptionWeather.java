package com.example.helpwheel.ui.dashboard;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentCostOfLastRideBinding;
import com.example.helpwheel.databinding.FragmentDescriptionWeatherBinding;

public class DescriptionWeather extends Fragment {

    private String temperature;
    private String description;
    public static final String WEATHER_TEMPERATURE = "weatherTemp";
    public static final String WEATHER_DESCRIPTION = "weatherDesc";
    public static final String PREF = "user";
    SharedPreferences pref;

    FragmentDescriptionWeatherBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getParentFragmentManager().setFragmentResultListener("weather from dashboard", this, (requestKey, result) -> {
//            String weather = result.getString("weather from dashboard");
//            binding.weatherTemperature.setText(weather);
//
//        });

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDescriptionWeatherBinding.inflate(inflater, container, false);
        pref = requireContext().getSharedPreferences(PREF, getContext().MODE_PRIVATE);

        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(WEATHER_TEMPERATURE)) {
                    setTemp();
                }
            }
        };

        pref.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);


        return binding.getRoot();


    }

    private void setTemp() {
        temperature = pref.getString(WEATHER_TEMPERATURE, "error");
        binding.weatherTemperature.setText(temperature);
    }
}