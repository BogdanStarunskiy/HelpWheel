package com.example.helpwheel.ui.fuel_management;

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentFirstOdometerReadingBinding;
import com.example.helpwheel.utils.APP_PREFERENCES
import com.example.helpwheel.utils.APP_PREFERENCES_ODOMETER
import com.example.helpwheel.utils.Constants;
import com.example.helpwheel.utils.SharedPreferencesHolder;

import java.util.Objects;

class FirstOdometerReadingFragment: Fragment() {
    lateinit var binding:FragmentFirstOdometerReadingBinding
    val sharedPreferencesHolder = SharedPreferencesHolder()
    lateinit var fuelStats: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstOdometerReadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        editor = fuelStats.edit()
        binding.submitBtnFuel.setOnClickListener {
            if (binding.odometerText.text.toString().isNotEmpty()){
                editor.putFloat(APP_PREFERENCES_ODOMETER, sharedPreferencesHolder.formattedNumber(binding.odometerText.text.toString().toFloat())).apply()
                NavHostFragment.findNavController(this).navigate(R.id.action_firstOdometerReadingFragment_to_navigation_notifications)
            }
        }
    }
}