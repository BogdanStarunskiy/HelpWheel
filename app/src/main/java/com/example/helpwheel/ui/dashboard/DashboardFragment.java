package com.example.helpwheel.ui.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentDashboardBinding;
import com.example.helpwheel.ui.dashboard.viewPagerFragments.DescriptionWeather;
import com.example.helpwheel.ui.dashboard.viewPagerFragments.WeatherWindSpeed;
import com.example.helpwheel.utils.Constants;
import com.example.helpwheel.utils.ZoomPageTransformer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayoutMediator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DashboardFragment extends Fragment {
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private FusedLocationProviderClient mFusedLocationClient;
    String key = "a98ca7720a8fd711bb8548bf2373e263";
    Constants constants;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = requireContext().getSharedPreferences(constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = preferences.edit();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        if (preferences.getString(constants.USERNAME_PREF, "user").equals("user") || preferences.getString(constants.USERNAME_PREF, "user").equals(""))
            showWelcomeScreen();

        changeUi();
        initViewPager();
        initListeners();
        setupSwitcher();
        updateUiWhenPermissionChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.VISIBLE);
        checkPermissions();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateUiWhenPermissionChanged() {
        if (!preferences.getBoolean(constants.IS_PERMISSION_GRANTED, true)) {
            binding.manualInput.setVisibility(View.GONE);
            binding.enterCityEditText.setVisibility(View.VISIBLE);
            binding.enterCity.setVisibility(View.VISIBLE);
            binding.weatherBtn.setVisibility(View.VISIBLE);
        } else {
            binding.enterCityEditText.setVisibility(View.GONE);
            binding.manualInput.setVisibility(View.VISIBLE);
            binding.enterCity.setVisibility(View.GONE);
            binding.weatherBtn.setVisibility(View.GONE);
        }
    }

    private void initViewPager() {
        ViewPager2 viewPager2 = binding.weatherViewPager;
        FragmentStateAdapter pagerAdapter = new ViewPagerAdapter(requireActivity());
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setPageTransformer(new ZoomPageTransformer());
        new TabLayoutMediator(binding.tab, binding.weatherViewPager, (tab, position) -> {
        }).attach();
    }

    private void initListeners() {
        binding.userGreetingEditButton.setOnClickListener(view -> showEditDataFragment());
        binding.weatherBtn.setOnClickListener(v -> {
            if (Objects.requireNonNull(binding.enterCity.getText()).toString().trim().equals("")) {
                Toast toast = Toast.makeText(binding.getRoot().getContext(), R.string.enter_city_message, Toast.LENGTH_LONG);
                toast.show();
            } else {
                getWeatherFromManualInput();
                editor.putString(constants.USER_CITY, binding.enterCity.getText().toString());
                dashboardViewModel.setCity(binding.enterCity.getText().toString());
            }
        });
    }

    private void setupSwitcher() {
        binding.manualInput.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkPermissions();
            } else {
                binding.enterCity.setText((preferences.getString(constants.USER_CITY, null)));
                dashboardViewModel.getWeatherDataManualInput(preferences.getString(constants.USER_CITY, null), key);
            }
            changeInputTypeWeather();
        });
    }

    private void getWeatherFromManualInput() {
        String user_city = Objects.requireNonNull(binding.enterCity.getText()).toString().trim();
        binding.enterCityEditText.setVisibility(View.VISIBLE);
        binding.enterCity.setVisibility(View.VISIBLE);
        binding.weatherBtn.setVisibility(View.VISIBLE);
        dashboardViewModel.getWeatherDataManualInput(user_city, key);
        editor.putString(constants.USER_CITY, user_city);
        editor.apply();
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Dexter.withContext(requireContext())
                    .withPermission(
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ).withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            getMyLocation();
                            editor.putBoolean(constants.IS_PERMISSION_GRANTED, true);
                            editor.apply();
                            updateUiWhenPermissionChanged();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            editor.putBoolean(constants.IS_PERMISSION_GRANTED, false);
                            editor.apply();
                            updateUiWhenPermissionChanged();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();

        } else {
            getMyLocation();
            updateUiWhenPermissionChanged();
        }
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

    @SuppressLint("MissingPermission")
    private void changeInputTypeWeather() {
        if (binding.manualInput.isChecked()) {
            DescriptionWeather descriptionWeather = new DescriptionWeather();
            descriptionWeather.setIsChecked(true);
            WeatherWindSpeed weatherWindSpeed = new WeatherWindSpeed();
            weatherWindSpeed.setChecked(true);
            binding.weatherBtn.setVisibility(View.GONE);
            binding.enterCityEditText.setVisibility(View.GONE);
            binding.enterCity.setVisibility(View.GONE);
        } else {
            DescriptionWeather descriptionWeather = new DescriptionWeather();
            descriptionWeather.setIsChecked(false);
            WeatherWindSpeed weatherWindSpeed = new WeatherWindSpeed();
            weatherWindSpeed.setChecked(false);
            binding.weatherBtn.setVisibility(View.VISIBLE);
            binding.enterCityEditText.setVisibility(View.VISIBLE);
            binding.enterCity.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation() {

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        if (location.getLatitude() == 0 && location.getLongitude() == 0) {
                            binding.weatherLoading.setVisibility(View.VISIBLE);
                        } else {
                            dashboardViewModel.getWeatherDataAutomatically(location.getLatitude(), location.getLongitude(), key);
                            Geocoder gcd = new Geocoder(requireContext(), Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (addresses != null && !addresses.isEmpty()) {
                                String city = addresses.get(0).getLocality();
                                dashboardViewModel.setCity(city);
                            }
                        }

                    }
                });

    }
}
