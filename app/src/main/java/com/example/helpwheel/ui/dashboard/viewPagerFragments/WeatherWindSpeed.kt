package com.example.helpwheel.ui.dashboard.viewPagerFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentWeatherWindSpeedBinding
import com.example.helpwheel.ui.dashboard.DashboardViewModel


class WeatherWindSpeed: Fragment() {
    lateinit var binding: FragmentWeatherWindSpeedBinding
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherWindSpeedBinding.inflate(inflater, container, false)
        dashboardViewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }


    private fun initObservers(){
        dashboardViewModel.getWind().observe(viewLifecycleOwner, this::setWind)
        dashboardViewModel.getIsGPSTurnedOn().observe(viewLifecycleOwner) {
            if (!it)
                setDataIfGPSIsTurnedOff()
            else
                setDataIfGPSIsTurnedOn()
        }
    }

    private fun setWind(wind: String) {
        binding.weatherWind.text = String.format("%s %s", wind, requireContext().resources.getString(R.string.speed))
    }

//    fun setChecked(checked: Boolean) {
//        isChecked = checked;
//    }

    private fun setDataIfGPSIsTurnedOff() {
        binding.imageViewWind.setAnimation(R.raw.ic_no_gps)
        binding.imageViewWind.playAnimation()
        binding.imageViewWind.scaleX = 1.0f
        binding.imageViewWind.scaleY = 1.0f
        binding.weatherWind.text = getString(R.string.no_gps_text)
    }

    private fun setDataIfGPSIsTurnedOn() {
        binding.imageViewWind.setAnimation(R.raw.ic_wind)
        binding.imageViewWind.playAnimation()
    }
}