package com.example.helpwheel.ui.dashboard.viewPagerFragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentWeatherWindSpeedBinding;
import com.example.helpwheel.ui.dashboard.DashboardViewModel;
import com.example.helpwheel.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;


public class WeatherWindSpeed extends Fragment {
    FragmentWeatherWindSpeedBinding binding;
    SharedPreferences pref;
    Constants constants;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherWindSpeedBinding.inflate(inflater, container, false);
        pref = requireContext().getSharedPreferences(constants.APP_PREFERENCES, getContext().MODE_PRIVATE);
        editor = pref.edit();
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(constants.WEATHER_WIND)) {
                setWind();
            } else if (key.equals(constants.WEATHER_WIND_AUTO)) {
                binding.weatherWind.setText(pref.getString(constants.WEATHER_WIND_AUTO, "no data") + requireContext().getResources().getString(R.string.speed));
            }
        };

        pref.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        if (!isChecked) {
            if (pref.getString(constants.WEATHER_WIND, "error") != null) {
                binding.weatherWind.setText(pref.getString(constants.WEATHER_WIND, "error") + requireContext().getResources().getString(R.string.speed));
            }
        }
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void setWind() {
        String wind = pref.getString(constants.WEATHER_WIND, "error");
        binding.weatherWind.setText(wind + requireContext().getResources().getString(R.string.speed));
    }

}