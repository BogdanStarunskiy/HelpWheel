package com.example.helpwheel.ui.dashboard;



import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.helpwheel.databinding.FragmentDescriptionWeatherBinding;

public class DescriptionWeather extends Fragment {

    public static final String WEATHER_TEMPERATURE = "weatherTemp";
    public static final String WEATHER_DESCRIPTION = "weatherDesc";
    public static final String PREF = "user";
    SharedPreferences pref;

    FragmentDescriptionWeatherBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDescriptionWeatherBinding.inflate(inflater, container, false);
        pref = requireContext().getSharedPreferences(PREF, getContext().MODE_PRIVATE);

        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(WEATHER_TEMPERATURE)) {
                setTemp();
            } else if (key.equals(WEATHER_DESCRIPTION)){
                setDesc();
            }
        };


        pref.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        if(pref.getString(WEATHER_DESCRIPTION, "error")!= null){
            binding.weatherDescription.setText(pref.getString(WEATHER_DESCRIPTION, "error"));
        }
        if(pref.getString(WEATHER_DESCRIPTION, "error")!=null){
            binding.weatherTemperature.setText(pref.getString(WEATHER_TEMPERATURE, "error"));
        }

        return binding.getRoot();


    }

    private void setTemp() {
        String temperature = pref.getString(WEATHER_TEMPERATURE, "error");
        binding.weatherTemperature.setText(temperature);
    }
    private void setDesc(){
        String description = pref.getString(WEATHER_DESCRIPTION, "error");
        binding.weatherDescription.setText(description);
    }
}