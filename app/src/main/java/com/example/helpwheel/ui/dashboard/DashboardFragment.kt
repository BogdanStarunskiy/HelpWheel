package com.example.helpwheel.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentDashboardBinding
import com.example.helpwheel.ui.dashboard.viewPagerFragments.DescriptionWeather
import com.example.helpwheel.ui.dashboard.viewPagerFragments.WeatherWindSpeed
import com.example.helpwheel.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*

class DashboardFragment : Fragment() {
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        dashboardViewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        editor = preferences.edit()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (preferences.getString(USERNAME, "user").equals("user") || preferences.getString(USERNAME, "user").equals(""))
            showWelcomeScreen()
        changeUi()
        initViewPager()
        initListeners()
        initObservers()
        setupSwitcher()
    }


    private fun initObservers() {
        dashboardViewModel.getIsPermissionGranted().observe(viewLifecycleOwner) { updateUiWhenPermissionChanged(it) }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<CardView>(R.id.customBnb).visibility = View.VISIBLE
        checkPermissions()
    }


    private fun showBalloon() {
        if (preferences.getBoolean(IS_FIRST_LAUNCHED_DASHBOARD, true))
            BuildBalloon(requireContext(), getString(R.string.balloon_edit_profile_here), viewLifecycleOwner).balloon.showAlignBottom(binding.userGreetingEditButton)
    }

    private fun updateUiWhenPermissionChanged(isPermissionGranted: Boolean) {
        if (isPermissionGranted) {
            binding.enterCityEditText.visibility = View.GONE
            binding.manualInput.visibility = View.VISIBLE
            binding.enterCity.visibility = View.GONE
            binding.weatherBtn.visibility = View.GONE
        } else {
            binding.manualInput.visibility = View.GONE
            binding.enterCityEditText.visibility = View.VISIBLE
            binding.enterCity.visibility = View.VISIBLE
            binding.weatherBtn.visibility = View.VISIBLE
            binding.enterCity.setText((preferences.getString(USER_CITY, getString(R.string.kyiv))))
            preferences.getString(USER_CITY, getString(R.string.kyiv))?.let { dashboardViewModel.getWeatherDataManualInput(it, KEY) }
        }
    }

    private fun initViewPager() {
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(DescriptionWeather())
        fragmentList.add(WeatherWindSpeed())
        val viewPager2 = binding.weatherViewPager
        val pagerAdapter = ViewPagerAdapter(requireActivity(), fragmentList)
        viewPager2.adapter = pagerAdapter
        viewPager2.setPageTransformer(ZoomPageTransformer())
        TabLayoutMediator(binding.tab, binding.weatherViewPager) { _, _ -> }.attach()
    }

    private fun initListeners() {
        binding.userGreetingEditButton.setOnClickListener { showEditDataFragment() }
        binding.weatherBtn.setOnClickListener {
            if (Objects.requireNonNull(binding.enterCity.text).toString().trim() == "")
                Toast.makeText(binding.root.context, R.string.enter_city_message, Toast.LENGTH_LONG).show()
            else {
                getWeatherFromManualInput()
                editor.putString(USER_CITY, binding.enterCity.text.toString())
                dashboardViewModel.setCity(binding.enterCity.text.toString())
            }
        }
    }

    private fun setupSwitcher() {
        binding.manualInput.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkPermissions()
            } else {
                binding.enterCity.setText(
                    (preferences.getString(
                        USER_CITY,
                        getString(R.string.kyiv)
                    ))
                )
                preferences.getString(USER_CITY, requireContext().resources.getString(R.string.kyiv))?.let { dashboardViewModel.getWeatherDataManualInput(it, KEY) }
                dashboardViewModel.setIsGpsTurnedOn(true)
            }
            changeInputTypeWeather()
        }
    }

    private fun getWeatherFromManualInput() {
        val userCity = Objects.requireNonNull(binding.enterCity.text).toString().trim()
        binding.enterCityEditText.visibility = View.VISIBLE
        binding.enterCity.visibility = View.VISIBLE
        binding.weatherBtn.visibility = View.VISIBLE
        dashboardViewModel.getWeatherDataManualInput(userCity, KEY)
        editor.putString(USER_CITY, userCity).apply()
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Dexter.withContext(requireContext())
                .withPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        Location.getMyLocation(mFusedLocationClient, requireActivity())
                        showBalloon()
                        editor.putBoolean(IS_FIRST_LAUNCHED_DASHBOARD, false).apply()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        dashboardViewModel.setIsPermissionGranted(false)
                        showBalloon()
                        editor.putBoolean(IS_FIRST_LAUNCHED_DASHBOARD, false).apply()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        p1!!.continuePermissionRequest()
                    }
                }).check()

        } else
            Location.getMyLocation(mFusedLocationClient, requireActivity())
    }

    private fun showWelcomeScreen() {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_navigation_dashboard_to_welcomeFragment)
    }

    private fun showEditDataFragment() {
        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_dashboard_to_changeDataFragment)
    }

    @SuppressLint("SetTextI18n")
    fun changeUi() {
        binding.greetingText.text = getString(R.string.hello_user_text) + " " + preferences.getString(USERNAME, "user") + "!"
    }

    private fun changeInputTypeWeather() {
        if (binding.manualInput.isChecked) {
            binding.weatherBtn.visibility = View.GONE
            binding.enterCityEditText.visibility = View.GONE
            binding.enterCity.visibility = View.GONE
        } else {
            binding.weatherBtn.visibility = View.VISIBLE
            binding.enterCityEditText.visibility = View.VISIBLE
            binding.enterCity.visibility = View.VISIBLE
        }
    }
}
