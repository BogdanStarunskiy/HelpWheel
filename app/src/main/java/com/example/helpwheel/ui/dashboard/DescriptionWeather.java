package com.example.helpwheel.ui.dashboard;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentDescriptionWeatherBinding;
import com.example.helpwheel.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DescriptionWeather extends Fragment {

    Constants constants;
    public double longitude;
    public double latitude;
    SharedPreferences pref;
    public String autoTemp;
    public String autoDesc;
    public boolean isChecked = true;
    FragmentDescriptionWeatherBinding binding;
    String temperature = null;
    SharedPreferences.Editor editor;
    public static final String WEATHER_WIND_AUTO = "weatherWindAuto";
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
        pref = requireContext().getSharedPreferences(constants.APP_PREFERENCES, getContext().MODE_PRIVATE);
        getWeather();
        editor = pref.edit();
        @SuppressLint("SetTextI18n") SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {



                if (key.equals(constants.WEATHER_TEMPERATURE)) {
                    setTemp();
                } else if (key.equals(constants.WEATHER_DESCRIPTION)) {
                    setDesc();

                }
                else if (key.equals(constants.WEATHER_DESC_AUTO)) {
                    autoDesc = pref.getString(constants.WEATHER_DESC_AUTO, " no data");
                    binding.weatherDescription.setText(autoDesc);
                } else if (key.equals(constants.WEATHER_TEMP_AUTO)) {
                    autoTemp = pref.getString(constants.WEATHER_TEMP_AUTO, "no data");
                    binding.weatherTemperature.setText(autoTemp+requireContext().getResources().getString(R.string.degree_cels));
                }


        };
        pref.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        if(!isChecked){
        if (pref.getString(constants.WEATHER_DESCRIPTION, "error") != null) {
            binding.weatherDescription.setText(pref.getString(constants.WEATHER_DESCRIPTION, "error"));
        }
        if (pref.getString(constants.WEATHER_DESCRIPTION, "error") != null) {
            binding.weatherTemperature.setText(pref.getString(constants.WEATHER_TEMPERATURE, "error"));
        }}


        return binding.getRoot();


    }


    @SuppressLint("SetTextI18n")
    private void setTemp() {
        String temperature = pref.getString(constants.WEATHER_TEMPERATURE, "error");
        binding.weatherTemperature.setText(temperature+requireContext().getResources().getString(R.string.degree_cels));
    }

    private void setDesc() {
        String description = pref.getString(constants.WEATHER_DESCRIPTION, "error");
        binding.weatherDescription.setText(description);
        LottieAnimationView animationView = binding.ImageViewDescription;
        if (description.equals("clear sky")){
            animationView.setAnimation(R.raw.ic_clear_sky);
            animationView.playAnimation();
        } else if (description.equals("few clouds")){
            animationView.setAnimation(R.raw.ic_few_clouds);
            animationView.playAnimation();
        } else if (description.equals("scattered clouds") || description.equals("broken clouds") || description.equals("overcast clouds")){
            animationView.setAnimation(R.raw.ic_clouds);
            animationView.playAnimation();
        } else if (description.equals("shower rain")){
            animationView.setAnimation(R.raw.ic_few_clouds);
        } else if (description.equals("rain")) {
            animationView.setAnimation(R.raw.ic_few_clouds);
        } else if (description.equals("thunderstorm")){
            animationView.setAnimation(R.raw.ic_few_clouds);
        } else if (description.equals("snow")){
            animationView.setAnimation(R.raw.ic_few_clouds);
        } else if (description.equals("mist")){
            animationView.setAnimation(R.raw.ic_few_clouds);
        }
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

                editor.putString(WEATHER_WIND_AUTO,wind);
                editor.putString(constants.WEATHER_TEMP_AUTO, temperature);
                editor.putString(constants.WEATHER_DESC_AUTO, description);
                editor.apply();


            } else {

                binding.weatherTemperature.setText(requireActivity().getResources().getString(R.string.error_weather));
            }

        });


    }
}