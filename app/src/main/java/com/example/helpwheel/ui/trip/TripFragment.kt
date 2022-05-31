package com.example.helpwheel.ui.trip

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentFuelManagementBinding
import com.example.helpwheel.ui.trip.adapter.BottomSheetViewPagerAdapter
import com.example.helpwheel.ui.trip.adapter.LastRideAdapter
import com.example.helpwheel.ui.trip.adapter.NewRideAdapter
import com.example.helpwheel.ui.trip.inerface.BottomSheetCallBack
import com.example.helpwheel.ui.trip.new_ride_fragments.remains_fuel.RemainsFuelNewRideViewModel
import com.example.helpwheel.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class TripFragment : Fragment(), BottomSheetCallBack {

    lateinit var binding: FragmentFuelManagementBinding
    private lateinit var bottomSheetDialogFuelStats: BottomSheetDialog
    private lateinit var bottomSheetDialogFuelInTank: BottomSheetDialog
    private lateinit var fuelStats: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var tripViewModel: TripViewModel
    private lateinit var remainsFuelViewModel: RemainsFuelNewRideViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFuelManagementBinding.inflate(inflater, container, false)
        tripViewModel = ViewModelProvider(requireActivity())[TripViewModel::class.java]
        remainsFuelViewModel = ViewModelProvider(requireActivity())[RemainsFuelNewRideViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        editor = fuelStats.edit()
        if (fuelStats.getFloat(ODOMETER, 0.0f) == 0.0f)
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_navigation_notifications_to_firstOdometerReadingFragment)
        initViewPagersAndTabs()
        initBottomSheetDialogs()
        initListeners()
        initObservers()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<CardView>(R.id.customBnb).visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        showBalloon()
        editor.putBoolean(IS_FIRST_LAUNCHED_FUEL_MANAGEMENT, false).apply()
    }

    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        tripViewModel.getFuelInTank().observe(viewLifecycleOwner) {
            if (it.toFloat() > 0)
                binding.fuelLevel.setTextColor(requireActivity().getColor(R.color.white))
            else
                binding.fuelLevel.setTextColor(requireActivity().getColor(R.color.red))

            binding.fuelLevel.text = "$it ${getString(R.string.litres_have_left)}"
        }
    }

    private fun showBalloon() {
        if (fuelStats.getBoolean(IS_FIRST_LAUNCHED_FUEL_MANAGEMENT, true)) {
            BuildBalloon(
                requireContext(),
                getString(R.string.balloon_refueling),
                viewLifecycleOwner
            ).balloon.showAlignBottom(binding.editButton)
            BuildBalloon(
                requireContext(),
                getString(R.string.balloon_fuelStats),
                viewLifecycleOwner
            ).balloon.showAlignTop(binding.fuelInputButton)
        }
    }

    private fun initListeners() {
        binding.fuelInputButton.setOnClickListener {
            bottomSheetDialogFuelStats.show()
            val viewPager2BottomSheet =
                bottomSheetDialogFuelStats.findViewById<ViewPager2>(R.id.view_pager_bottom_sheet)
            viewPager2BottomSheet!!.adapter = BottomSheetViewPagerAdapter(this)
            TabLayoutMediator(
                Objects.requireNonNull(bottomSheetDialogFuelStats.findViewById(R.id.tab)),
                viewPager2BottomSheet
            ) { _, _ -> }.attach()
        }

        binding.editButton.setOnClickListener {
            bottomSheetDialogFuelInTank.show()
            val tankFuelLevel =
                bottomSheetDialogFuelInTank.findViewById<EditText>(R.id.fuel_in_tank_text)
            val submit =
                bottomSheetDialogFuelInTank.findViewById<Button>(R.id.submit_btn_fuel_in_tank)
            val container =
                bottomSheetDialogFuelInTank.findViewById<ConstraintLayout>(R.id.full_tank)
            submit!!.setOnClickListener {
                if (tankFuelLevel!!.text.toString().isNotEmpty()) {
                    if (tankFuelLevel.text.toString().toFloat() > fuelStats.getFloat(
                            FUEL_TANK_CAPACITY,
                            0.0f
                        )
                    )
                        showDialogOverFuel()
                    else {
                        val fuelLevel = SharedPreferencesHolder.formattedNumber(tankFuelLevel.text.toString().toFloat())
                        editor.putFloat(FUEL_LEVEL, fuelLevel).apply()
                        bottomSheetDialogFuelInTank.dismiss()
                        tripViewModel.setFuelInTankFromEditText(tankFuelLevel.text.toString().toFloat())
                        remainsFuelViewModel.setRemainsFuelNewRide()
                    }
                } else {
                    bottomSheetDialogFuelInTank.dismiss()
                }
            }
            container!!.setOnClickListener {
                tankFuelLevel!!.setText(fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f).toString())
            }
        }
    }

    private fun initBottomSheetDialogs() {
        bottomSheetDialogFuelStats = BottomSheetDialog(requireContext())
        bottomSheetDialogFuelStats.setContentView(R.layout.bottom_sheet_dialog_fuel_stats)
        bottomSheetDialogFuelInTank = BottomSheetDialog(requireContext())
        bottomSheetDialogFuelInTank.setContentView(R.layout.bottom_sheet_fuel_in_tank)
    }

    private fun initViewPagersAndTabs() {
        binding.viewPagerLastRide.adapter = LastRideAdapter(this, tripViewModel.inflateLastRideFragments())
        binding.viewPagerNewRide.adapter = NewRideAdapter(this, tripViewModel.inflateNewRideFragments())
        TabLayoutMediator(binding.tabLastRide, binding.viewPagerLastRide) { _, _ -> }.attach()
        TabLayoutMediator(binding.tabNewRide, binding.viewPagerNewRide) { _, _ -> }.attach()
    }

    private fun showDialogOverFuel() {
        val dialog =
            AlertDialog.Builder(requireContext()).setView(R.layout.edit_fuel_level_error_diallog)
                .create()
        dialog.show()
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        val okBtn = dialog.findViewById<Button>(R.id.ok_button)
        okBtn!!.setOnClickListener { dialog.dismiss() }
    }

    override fun dismissBottomSheet() {
        bottomSheetDialogFuelStats.dismiss()
    }

}