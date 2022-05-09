package com.example.helpwheel.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentDescriptionWeatherBinding;
import com.example.helpwheel.databinding.FragmentWeatherWindSpeedBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherWindSpeed extends Fragment {
    public static final String WEATHER_WIND = "weatherWind";
    public static final String PREF = "user";
    FragmentWeatherWindSpeedBinding binding;
    SharedPreferences pref;
    public static final String WEATHER_WIND_AUTO = "weatherWindAuto";
    private boolean isChecked;
    public double longitude;
    public double latitude;
    SharedPreferences.Editor editor;

    String wind = null;

    String key = "a98ca7720a8fd711bb8548bf2373e263";

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherWindSpeedBinding.inflate(inflater, container, false);
        pref = requireContext().getSharedPreferences(PREF, getContext().MODE_PRIVATE);
        getWeather();
        editor =pref.edit();
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(WEATHER_WIND)) {
                setWind();
            }else if(key.equals(WEATHER_WIND_AUTO)){
                binding.weatherWind.setText(pref.getString(WEATHER_WIND_AUTO, "no data")+requireContext().getResources().getString(R.string.speed));
            }
        };

        pref.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        if (!isChecked) {
            if (pref.getString(WEATHER_WIND, "error") != null) {
                binding.weatherWind.setText(pref.getString(WEATHER_WIND, "error")+ requireContext().getResources().getString(R.string.speed));
            }
        }
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void setWind() {
        String wind = pref.getString(WEATHER_WIND, "error");
        binding.weatherWind.setText(wind+ requireContext().getResources().getString(R.string.speed));
    }
    private void getWeather() {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + key + "&units=metric&lang=en";
        DashboardViewModel dashboardViewModel = new DashboardViewModel();
        dashboardViewModel.getTemperature(url);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {
            if (s != null && !s.equals("")) {


                try {
                    JSONObject jsonObject = new JSONObject(s);
                    wind = jsonObject.getJSONObject("wind").getDouble("speed") + " ";



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                editor.putString(WEATHER_WIND_AUTO,wind);
                editor.apply();


            } else {

                binding.weatherWind.setText(getActivity().getResources().getString(R.string.error_weather));
            }

        });


    }
}