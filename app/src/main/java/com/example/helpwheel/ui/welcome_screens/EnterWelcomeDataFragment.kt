package com.example.helpwheel.ui.welcome_screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentEnterWelcomeDataBinding
import com.example.helpwheel.utils.APP_PREFERENCES
import com.example.helpwheel.utils.CONSUMPTION_PER_100KM
import com.example.helpwheel.utils.FUEL_TANK_CAPACITY
import com.example.helpwheel.utils.USERNAME
import java.util.*

class EnterWelcomeDataFragment : Fragment() {
    lateinit var binding: FragmentEnterWelcomeDataBinding
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterWelcomeDataBinding.inflate(inflater, container, false)
        val fuelStats: SharedPreferences =
            requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        editor = fuelStats.edit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonOK.setOnClickListener { putToSharedPreferences() }
    }

    private fun putToSharedPreferences() {
        try {
            val username = Objects.requireNonNull(binding.enterName.text).toString().trim()
            val consumptionPer100km =
                Objects.requireNonNull(binding.consumptionPer100km.text).toString().toFloat()
            val fuelTankCapacity =
                Objects.requireNonNull(binding.fuelTankCapacity.text).toString().toFloat()
            editor.putString(USERNAME, username)
            editor.putFloat(CONSUMPTION_PER_100KM, consumptionPer100km)
            editor.putFloat(FUEL_TANK_CAPACITY, fuelTankCapacity)
            editor.apply()
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_enterWelcomeDataFragment_to_navigation_dashboard)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), R.string.field_must_be_filled, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<CardView>(R.id.customBnb).visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<CardView>(R.id.customBnb).visibility = View.GONE
    }
}