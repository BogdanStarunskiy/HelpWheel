package com.example.helpwheel.ui.dashboard;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentDescriptionWeatherBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DescriptionWeather extends Fragment {

    public static final String WEATHER_TEMPERATURE = "weatherTemp";
    public static final String WEATHER_DESCRIPTION = "weatherDesc";
    public static final String PREF = "user";
    public static final String WEATHER_TEMP_AUTO = "weatherTempAuto";
    public final static String WEATHER_DESC_AUTO = "weatherDescAuto";
    public double longitude;
    public double latitude;
    SharedPreferences pref;
    public String autoTemp;
    public String autoDesc;
    public boolean isChecked = true;
    FragmentDescriptionWeatherBinding binding;
    String temperature = null;
    SharedPreferences.Editor editor;
    String description = null;
    String wind = null;

    String key = "a98ca7720a8fd711bb8548bf2373e263";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDescriptionWeatherBinding.inflate(inflater, container, false);
        pref = requireContext().getSharedPreferences(PREF, getContext().MODE_PRIVATE);
        getWeather();
        editor = pref.edit();
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {



                if (key.equals(WEATHER_TEMPERATURE)) {
                    setTemp();
                } else if (key.equals(WEATHER_DESCRIPTION)) {
                    setDesc();

                }
                else if (key.equals(WEATHER_DESC_AUTO)) {
                    autoDesc = pref.getString(WEATHER_DESC_AUTO, " no data");
                    binding.weatherDescription.setText(autoDesc);
                } else if (key.equals(WEATHER_TEMP_AUTO)) {
                    autoTemp = pref.getString(WEATHER_TEMP_AUTO, "no data");
                    binding.weatherTemperature.setText(autoTemp);
                }


        };
        pref.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        if(!isChecked){
        if (pref.getString(WEATHER_DESCRIPTION, "error") != null) {
            binding.weatherDescription.setText(pref.getString(WEATHER_DESCRIPTION, "error"));
        }
        if (pref.getString(WEATHER_DESCRIPTION, "error") != null) {
            binding.weatherTemperature.setText(pref.getString(WEATHER_TEMPERATURE, "error"));
        }}


        return binding.getRoot();
//sdf

    }


    private void setTemp() {
        String temperature = pref.getString(WEATHER_TEMPERATURE, "error");
        binding.weatherTemperature.setText(temperature);
    }

    private void setDesc() {
        String description = pref.getString(WEATHER_DESCRIPTION, "error");
        binding.weatherDescription.setText(description);
    }

    private void getWeather() {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + key + "&units=metric&lang=en";
        DashboardViewModel dashboardViewModel = new DashboardViewModel();
        dashboardViewModel.getTemperature(url);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {
            if (s != null && !s.equals("")) {


                try {
                    JSONObject jsonObject = new JSONObject(s);
                    temperature = jsonObject.getJSONObject("main").getDouble("temp") + " ";
                    wind = jsonObject.getJSONObject("wind").getDouble("speed") + " ";
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject Json_description = jsonArray.getJSONObject(0);
                    description = Json_description.getString("description");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                editor.putString(WEATHER_TEMP_AUTO, temperature);
                editor.putString(WEATHER_DESC_AUTO, description);
                editor.apply();

            } else {

                binding.weatherTemperature.setText(getActivity().getResources().getString(R.string.error_weather));
            }

        });


    }
}