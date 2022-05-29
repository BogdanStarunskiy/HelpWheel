package com.example.helpwheel.ui.dashboard

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentChangeDataBinding
import com.example.helpwheel.utils.*
import java.util.*

class ChangeDataFragment : Fragment() {
    lateinit var binding: FragmentChangeDataBinding
    lateinit var fuelStats: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        editor = fuelStats.edit()
        showBalloon()
        editor.putBoolean(IS_FIRST_LAUNCHED_CHANGE_DATA, false).apply()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<CardView>(R.id.customBnb).visibility = View.GONE
    }

    private fun initListeners() {
        binding.buttonOK.setOnClickListener {
            if (Objects.requireNonNull(binding.enterName.text).toString().trim().isNotEmpty()) {
                editor.putString(USERNAME, binding.enterName.text.toString().trim())
                editor.apply()
            }
            if (Objects.requireNonNull(binding.consumptionPer100km.text).toString().trim()
                    .isNotEmpty()
            ) {
                editor.putFloat(
                    CONSUMPTION_PER_100KM,
                    binding.consumptionPer100km.text.toString().toFloat()
                ).apply()
//                SharedPreferencesHolder.countSpendFuel("new")
//                SharedPreferencesHolder.countSpendFuel("last")
//                SharedPreferencesHolder.countPrice("new")
//                SharedPreferencesHolder.countPrice("last")
                SharedPreferencesHolder.countFuelInTank()
//                SharedPreferencesHolder.calculateRemainsFuel()
            }
            if (Objects.requireNonNull(binding.fuelTankCapacity.text).toString().isNotEmpty()) {
                val capacity = fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f)
                editor.putFloat(FUEL_TANK_CAPACITY_OLD, capacity)
                val fuelInTank = binding.fuelTankCapacity.text.toString().toFloat()
                editor.putFloat(FUEL_TANK_CAPACITY, fuelInTank)
                editor.apply()
                SharedPreferencesHolder.updateFuelTankCapacity()
//                SharedPreferencesHolder.calculateRemainsFuel()
            }
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_changeDataFragment_to_navigation_dashboard)
        }
        binding.removeOdometerReadings.setOnClickListener { showDialog() }
    }

    private fun showBalloon() {
        if (fuelStats.getBoolean(IS_FIRST_LAUNCHED_CHANGE_DATA, true))
            BuildBalloon(requireContext(), getString(R.string.balloon_odometer_readings), viewLifecycleOwner).balloon.showAlignBottom(binding.removeOdometerReadings)
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val customLayout = layoutInflater.inflate(R.layout.remove_odometer_readings_dialog, null)
        builder.setView(customLayout)
        val dialog = builder.create()
        dialog.show()
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        val yesBtn = dialog.findViewById<Button>(R.id.yes_btn)
        val noBtn = dialog.findViewById<Button>(R.id.no_btn)
        yesBtn!!.setOnClickListener {
            SharedPreferencesHolder.removeLastRideData()
            dialog.dismiss()
        }
        noBtn!!.setOnClickListener { dialog.dismiss() }
    }
}