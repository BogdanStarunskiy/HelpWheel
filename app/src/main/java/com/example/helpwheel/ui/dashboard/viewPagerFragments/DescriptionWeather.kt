package com.example.helpwheel.ui.dashboard.viewPagerFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentDescriptionWeatherBinding
import com.example.helpwheel.ui.dashboard.DashboardViewModel

class DescriptionWeather : Fragment() {
    private lateinit var binding: FragmentDescriptionWeatherBinding
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDescriptionWeatherBinding.inflate(inflater, container, false)
        dashboardViewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        dashboardViewModel.getTemperature().observe(viewLifecycleOwner, this::setTemp)
        dashboardViewModel.getDescription().observe(viewLifecycleOwner, this::setDesc)
        dashboardViewModel.getCity().observe(viewLifecycleOwner, this::setCity)
        dashboardViewModel.getIsGPSTurnedOn().observe(viewLifecycleOwner) {
            if (!it)
                setDataIfGPSIsTurnedOff()
            else
                setDataIfGPSIsTurnedOn()
        }
    }

    private fun setCity(userCity: String) {
        binding.userCity.text = userCity
    }

    private fun setTemp(temperature: String) {
        binding.weatherTemperature.text =
            String.format("%s%s", temperature, getString(R.string.degree_cels))
    }

    private fun setDesc(description: String) {
        var currentWeatherState: String
        val animationView = binding.imageViewDescription
        binding.weatherDescription.visibility = View.VISIBLE
        binding.weatherDescription.visibility = View.VISIBLE
        binding.userCityAnimation.visibility = View.VISIBLE
        binding.userCity.visibility = View.VISIBLE
        animationView.scaleX = 1.0f
        animationView.scaleY = 1.0f
        if (description.contains("thunderstorm")) {
            currentWeatherState = getString(R.string.thunderstorm)
            animationView.setAnimation(R.raw.ic_thunderstorm)
            animationView.playAnimation()
        } else if (description.contains("drizzle")) {
            currentWeatherState = getString(R.string.drizzle)
            animationView.setAnimation(R.raw.ic_drizzle)
            animationView.playAnimation()
        } else if (description.contains("rain")) {
            currentWeatherState = getString(R.string.rain)
            animationView.setAnimation(R.raw.ic_rain)
            animationView.playAnimation()
        } else if (description.contains("snow")) {
            currentWeatherState = getString(R.string.snow)
            animationView.setAnimation(R.raw.ic_snow)
            animationView.playAnimation()
        } else if (description.contains("clouds")) {
            currentWeatherState = getString(R.string.cloudy)
            animationView.setAnimation(R.raw.ic_clouds)
            animationView.playAnimation()
        } else if (description.contains("clear")) {
            currentWeatherState = getString(R.string.sunny)
            animationView.setAnimation(R.raw.ic_clear_sky)
            animationView.playAnimation()
        } else if (description == "squalls") {
            currentWeatherState = getString(R.string.squalls)
            animationView.setAnimation(R.raw.ic_wind)
            animationView.playAnimation()
        } else if (description == "tornado") {
            currentWeatherState = getString(R.string.tornado)
            animationView.setAnimation(R.raw.ic_tornado)
            animationView.playAnimation()
        } else if (description == "error") {
            currentWeatherState = getString(R.string.internet_error)
            binding.weatherTemperature.text = currentWeatherState
            binding.weatherDescription.visibility = View.GONE
            binding.userCityAnimation.visibility = View.GONE
            binding.userCity.visibility = View.GONE
            animationView.setAnimation(R.raw.ic_no_internet)
            animationView.scaleX = 1.5f
            animationView.scaleY = 1.5f
            animationView.playAnimation()
        }
        else {
            currentWeatherState = getString(R.string.fog)
            animationView.setAnimation(R.raw.ic_mist)
            animationView.playAnimation()
        }

        if (description == "few clouds") {
            currentWeatherState = getString(R.string.few_clouds)
            animationView.setAnimation(R.raw.ic_few_clouds)
            animationView.playAnimation()
        }

        binding.weatherDescription.text = currentWeatherState
    }

    private fun setDataIfGPSIsTurnedOff() {
        binding.imageViewDescription.setAnimation(R.raw.ic_no_gps)
        binding.imageViewDescription.playAnimation()
        binding.weatherDescription.visibility = View.GONE
        binding.weatherTemperature.text = getString(R.string.no_gps_text)
        binding.userCity.visibility = View.GONE
        binding.userCityAnimation.visibility = View.GONE
    }

    private fun setDataIfGPSIsTurnedOn() {
        binding.weatherDescription.visibility = View.VISIBLE
        binding.userCity.visibility = View.VISIBLE
        binding.userCityAnimation.visibility = View.VISIBLE
    }
}