package com.example.helpwheel.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentDashboardBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class DashboardFragment extends Fragment {
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private String currentTemperature = "";
    private String feels_like = "";
    private String wind_speed = "";
    private String weather_desc = "";
    private String degree_cels = "";
    private String speed = "";
    public static final String PREF = "user";
    public static final String USERNAME_PREF = "usernamePref";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private void showSuccessMessage() {
        binding.currentWeather.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        binding.currentWeather.setVisibility(View.VISIBLE);
        binding.currentWeather.setText(R.string.error_weather);
    }

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        preferences = requireContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        editor = preferences.edit();
        if (preferences.getString(USERNAME_PREF, "user").equals("user") || preferences.getString(USERNAME_PREF, "user").equals(""))
            showWelcomeScreen();
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initStrings();
        changeUi();
        binding.greetingText.setOnClickListener(view -> showEditDataFragment());
        binding.weatherBtn.setOnClickListener(v -> {
            if (binding.enterCity.getText().toString().trim().equals("")) {
                Toast toast = Toast.makeText(binding.getRoot().getContext(), R.string.enter_city_message, Toast.LENGTH_LONG);
                toast.show();
            } else {
                String user_city = binding.enterCity.getText().toString().trim();
                String key = "a98ca7720a8fd711bb8548bf2373e263";
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + user_city + "&appid=" + key + "&units=metric&lang=en";
                dashboardViewModel.getTemperature(url);
                binding.weatherLoading.setVisibility(View.VISIBLE);
            }
        });


        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {
            binding.weatherLoading.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")) {
                String temperature = null;
                String main_description = null;
                String description = null;
                String wind = null;
                String feel_temperature = null;

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    temperature = currentTemperature + " " + jsonObject.getJSONObject("main").getDouble("temp") + degree_cels;
                    feel_temperature = feels_like + " " + jsonObject.getJSONObject("main").getDouble("feels_like") + degree_cels;
                    wind = wind_speed + " " + jsonObject.getJSONObject("wind").getDouble("speed") + " " + speed;
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject Json_description = jsonArray.getJSONObject(0);
                    main_description = Json_description.getString("main");
                    description = weather_desc + " " + Json_description.getString("description");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                assert main_description != null;
                assert description != null;
                if (main_description.trim().toLowerCase(Locale.ROOT).
                        equals(description.trim().
                                toLowerCase(Locale.ROOT))) {
                    binding.currentWeather.setText(temperature + "\n");
                    binding.currentWeather.append(feel_temperature + "\n");
                    binding.currentWeather.append(main_description + "\n");
                    binding.currentWeather.append(wind + "\n");
                } else {
                    binding.currentWeather.setText(temperature + "\n");
                    binding.currentWeather.append(feel_temperature + "\n");
                    binding.currentWeather.append(main_description + "\n");
                    binding.currentWeather.append(description + "\n");
                    binding.currentWeather.append(wind + "\n");
                }
                binding.weatherLoading.setVisibility(View.INVISIBLE);
                showSuccessMessage();
            } else {
                showErrorMessage();
                binding.weatherLoading.setVisibility(View.INVISIBLE);

            }


        });
        return root;
    }

    private void initStrings() {
        currentTemperature = requireContext().getResources().getString(R.string.temperature_is);
        feels_like = requireContext().getResources().getString(R.string.feels_like);
        wind_speed = requireContext().getResources().getString(R.string.wind_speed);
        weather_desc = requireContext().getResources().getString(R.string.weather_desc);
        degree_cels = requireContext().getResources().getString(R.string.degree_cels);
        speed = requireContext().getResources().getString(R.string.speed);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showWelcomeScreen() {
        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_dashboard_to_welcomeFragment);
    }

    private void showEditDataFragment() {
        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_dashboard_to_changeDataFragment);
    }

    @SuppressLint("SetTextI18n")
    public void changeUi() {
        binding.greetingText.setText(getString(R.string.hello_user_text) + " " + preferences.getString(USERNAME_PREF, "user") + "!");
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.VISIBLE);
    }
}
//fdsfsd