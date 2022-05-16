package com.example.helpwheel.ui.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentDashboardBinding;
import com.example.helpwheel.utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayoutMediator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private FusedLocationProviderClient mFusedLocationClient;
    String temperature = null;
    String description = null;
    String wind = null;
    String key = "a98ca7720a8fd711bb8548bf2373e263";
    Constants constants;
    private static final int NUM_PAGES = 2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = requireContext().getSharedPreferences(constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = preferences.edit();
        if (preferences.getString(constants.USERNAME_PREF, "user").equals("user") || preferences.getString(constants.USERNAME_PREF, "user").equals(""))
            showWelcomeScreen();
        changeUi();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        ViewPager2 viewPager2 = binding.weatherViewPager;
        FragmentStateAdapter pagerAdapter = new ScreenSlidePageAdapterWeather(requireActivity());
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setPageTransformer(new ZoomPageTransformer());
        new TabLayoutMediator(binding.tab, binding.weatherViewPager, (tab, position) -> {
        }).attach();
        initObservers();
        setupSwitcher();
        initListeners();
    }

    private void initListeners() {
        binding.userGreetingEditButton.setOnClickListener(view -> showEditDataFragment());
        binding.weatherBtn.setOnClickListener(v -> {
            if (binding.enterCity.getText().toString().trim().equals("")) {
                Toast toast = Toast.makeText(binding.getRoot().getContext(), R.string.enter_city_message, Toast.LENGTH_LONG);
                toast.show();
            } else {
                String user_city = binding.enterCity.getText().toString().trim();

                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + user_city + "&appid=" + key + "&units=metric&lang=en";
                dashboardViewModel.getTemperature(url);
                binding.weatherLoading.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupSwitcher() {
        binding.manualInput.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkPermissions();

            }else {
                String user_city = binding.enterCity.getText().toString().trim();
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + user_city + "&appid=" + key + "&units=metric&lang=en";
                dashboardViewModel.getTemperature(url);
            }
            changeInputTypeWeather();
        });
    }

    private void initObservers() {
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


                editor.putString(constants.WEATHER_TEMP_AUTO, temperature);
                editor.putString(constants.WEATHER_DESC_AUTO, description);
                editor.putString(constants.WEATHER_WIND_AUTO, wind);
                editor.apply();

            }
            binding.weatherLoading.setVisibility(View.INVISIBLE);
        });

    }

    private static class ZoomPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;

        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();
            int pageHeight = page.getHeight();
            if (position < -1) {
                page.setAlpha(0f);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    page.setTranslationX(horzMargin - vertMargin / 2);

                } else {
                    page.setTranslationX(-horzMargin + vertMargin / 2);
                }
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            }


        }
    }

    private static class ScreenSlidePageAdapterWeather extends FragmentStateAdapter {
        public ScreenSlidePageAdapterWeather(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new DescriptionWeather();
                case 1:
                    return new WeatherWindSpeed();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Dexter.withContext(requireContext())
                    .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    getMyLocation();
                }


                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
            }).check();

        } else {
            getMyLocation();
        }

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
        binding.greetingText.setText(getString(R.string.hello_user_text) + " " + preferences.getString(constants.USERNAME_PREF, "user") + "!");
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.VISIBLE);
        checkPermissions();
    }

    @SuppressLint("MissingPermission")
    private void changeInputTypeWeather() {
        if (binding.manualInput.isChecked()) {
            DescriptionWeather descriptionWeather = new DescriptionWeather();
            descriptionWeather.setIsChecked(true);
            WeatherWindSpeed weatherWindSpeed = new WeatherWindSpeed();
            weatherWindSpeed.setChecked(true);
            binding.weatherBtn.setVisibility(View.GONE);
            binding.enterCity.setVisibility(View.GONE);

        } else {
            DescriptionWeather descriptionWeather = new DescriptionWeather();
            descriptionWeather.setIsChecked(false);
            WeatherWindSpeed weatherWindSpeed = new WeatherWindSpeed();
            weatherWindSpeed.setChecked(false);
            binding.weatherBtn.setVisibility(View.VISIBLE);
            binding.enterCity.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation() {

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {

                    if (location != null) {
                        Log.wtf("LOCATION", location.toString());
                        String url2 = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + key + "&units=metric&lang=en";
                        dashboardViewModel.getTemperature(url2);

                        Geocoder gcd = new Geocoder(requireContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses != null && !addresses.isEmpty()) {
                            String city = addresses.get(0).getLocality();
                            DescriptionWeather descriptionWeather = new DescriptionWeather();
                            descriptionWeather.setLongitude(location.getLongitude());
                            descriptionWeather.setLatitude(location.getLatitude());
                        }

                    }
                });

    }





}
