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
import android.widget.CompoundButton;
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
    private String currentTemperature = "";
    private String feels_like = "";
    private String wind_speed = "";
    private String weather_desc = "";
    private String degree_cels = "";
    private String speed = "";
    public static final String PREF = "user";
    public static final String USERNAME_PREF = "usernamePref";
    public static final String WEATHER_TEMPERATURE = "weatherTemp";
    public static final String WEATHER_DESCRIPTION = "weatherDesc";
    public static final String WEATHER_WIND = "weatherWind";
    public static final String WEATHER_TEMP_AUTO = "weatherTempAuto";
    public final static String WEATHER_DESC_AUTO = "weatherDescAuto";
    public static final String WEATHER_WIND_AUTO = "weatherWindAuto";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private FusedLocationProviderClient mFusedLocationClient;
    String temperature = null;
    String main_description = null;
    String description = null;
    String wind = null;
    String feel_temperature = null;
    String key = "a98ca7720a8fd711bb8548bf2373e263";
    private FragmentStateAdapter pagerAdapter;
    private ViewPager2 viewPager2;
    private static final int NUM_PAGES = 2;


    private void showErrorMessage() {
        binding.currentWeather.setVisibility(View.VISIBLE);
        binding.currentWeather.setText(R.string.error_weather);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

//        View root = binding.getRoot();

//        binding.manualInput.setOnCheckedChangeListener((compoundButton, b) -> {
//            changeInputTypeWeather();
//        });



//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), s -> {
//            binding.weatherLoading.setVisibility(View.INVISIBLE);
//            if (s != null && !s.equals("")) {
//
//
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    temperature = currentTemperature + " " + jsonObject.getJSONObject("main").getDouble("temp") + degree_cels;
//                    feel_temperature = feels_like + " " + jsonObject.getJSONObject("main").getDouble("feels_like") + degree_cels;
//                    wind = wind_speed + " " + jsonObject.getJSONObject("wind").getDouble("speed") + " " + speed;
//                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
//                    JSONObject Json_description = jsonArray.getJSONObject(0);
//                    main_description = Json_description.getString("main");
//                    description = weather_desc + " " + Json_description.getString("description");
//                    if(!binding.manualInput.isChecked()){
//                        editor.putString(WEATHER_WIND, wind);
//                        editor.putString(WEATHER_TEMPERATURE, temperature);
//                        editor.putString(WEATHER_DESCRIPTION, description);
//                        editor.apply();}
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                binding.weatherLoading.setVisibility(View.INVISIBLE);
//            } else {
//                binding.weatherLoading.setVisibility(View.INVISIBLE);
//
//            }
//
//        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = requireContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        editor = preferences.edit();
        //        if (preferences.getString(USERNAME_PREF, "user").equals("user") || preferences.getString(USERNAME_PREF, "user").equals(""))
//            showWelcomeScreen();
        initStrings();
        changeUi();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        viewPager2 = binding.weatherViewPager;
        pagerAdapter = new ScreenSlidePageAdapterWeather(requireActivity());
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setPageTransformer(new ZoomPageTransformer());
        new TabLayoutMediator(binding.tab, binding.weatherViewPager, (tab, position) -> {
        }).attach();
        initObservers();
        setupSwitcher();
        initListeners();
    }

    private void initListeners() {
        binding.greetingText.setOnClickListener(view -> showEditDataFragment());
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


                editor.putString(WEATHER_TEMP_AUTO, temperature);
                editor.putString(WEATHER_DESC_AUTO, description);
                editor.putString(WEATHER_WIND_AUTO, wind);
                editor.apply();

              binding.weatherLoading.setVisibility(View.INVISIBLE);
            } else {
                binding.weatherLoading.setVisibility(View.INVISIBLE);
            }
        });

    }

    private class ZoomPageTransformer implements ViewPager2.PageTransformer {
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

    private class ScreenSlidePageAdapterWeather extends FragmentStateAdapter {
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
                            Log.wtf("ADDRESSES", (addresses.get(0).getLocality()) + (addresses.get(0).getAdminArea()));

                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                            DescriptionWeather descriptionWeather = new DescriptionWeather();
                            descriptionWeather.setLongitude(location.getLongitude());
                            descriptionWeather.setLatitude(location.getLatitude());


                            Log.d("TAG", "getAddress:  address" + address);
                            Log.d("TAG", "" + city);
                            Log.d("TAG", "getAddress:  state" + state);
                            Log.d("TAG", "getAddress:  postalCode" + postalCode);
                            Log.d("TAG", "getAddress:  knownName" + knownName);

                        }

                    }
                });

    }





}
