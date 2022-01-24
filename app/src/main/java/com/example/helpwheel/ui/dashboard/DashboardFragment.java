package com.example.helpwheel.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.helpwheel.databinding.FragmentDashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.weatherBtn.setOnClickListener(v -> {
            if (binding.enterCity.getText().toString().trim().equals("")) {
                Toast toast = Toast.makeText(binding.getRoot().getContext(), "Enter city!", Toast.LENGTH_LONG);
                toast.show();
            } else {
                String user_city = binding.enterCity.getText().toString().trim();
                String key = "a98ca7720a8fd711bb8548bf2373e263";
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + user_city + "&appid=" + key + "&units=metric&lang=ru";
                dashboardViewModel.getTemperature(url);
            }
        });


        final TextView textView = binding.currentWeather;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {
            String temp = "";
            try {
                JSONObject jsonObject = new JSONObject(s);
                temp = "Temp " + jsonObject.getJSONObject("main").getDouble("temp");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textView.setText(temp);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}