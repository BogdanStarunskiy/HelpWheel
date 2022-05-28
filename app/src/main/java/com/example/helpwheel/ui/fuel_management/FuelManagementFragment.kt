package com.example.helpwheel.ui.fuel_management

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
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentFuelManagementBinding
import com.example.helpwheel.ui.fuel_management.adapter.BottomSheetViewPagerAdapter
import com.example.helpwheel.ui.fuel_management.adapter.LastRideAdapter
import com.example.helpwheel.ui.fuel_management.adapter.NewRideAdapter
import com.example.helpwheel.ui.fuel_management.inerface.BottomSheetCallBack
import com.example.helpwheel.ui.fuel_management.last_ride_fragments.cost.CostLastRideFragment
import com.example.helpwheel.ui.fuel_management.last_ride_fragments.distance.DistanceLastRideFragment
import com.example.helpwheel.ui.fuel_management.last_ride_fragments.ecology.EcologyLastRideFragment
import com.example.helpwheel.ui.fuel_management.last_ride_fragments.spent_fuel.SpentFuelLastRideFragment
import com.example.helpwheel.ui.fuel_management.new_ride_fragments.cost.CostNewRideFragment
import com.example.helpwheel.ui.fuel_management.new_ride_fragments.distance.DistanceNewRideFragment
import com.example.helpwheel.ui.fuel_management.new_ride_fragments.ecology.EcologyNewRideFragment
import com.example.helpwheel.ui.fuel_management.new_ride_fragments.remains_fuel.RemainsFuelNewRideFragment
import com.example.helpwheel.ui.fuel_management.new_ride_fragments.spendFuel.SpendFuelNewRideFragment
import com.example.helpwheel.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class FuelManagementFragment: Fragment(), BottomSheetCallBack {

    lateinit var binding: FragmentFuelManagementBinding
    private lateinit var bottomSheetDialogFuelStats: BottomSheetDialog
    private lateinit var bottomSheetDialogFuelInTank: BottomSheetDialog
    private lateinit var fuelStats: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val lastRideFragments = ArrayList<Fragment>()
    private val newRideFragments = ArrayList<Fragment>()
    private val sharedPreferencesHolder = SharedPreferencesHolder()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFuelManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fuelStats = requireContext().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        editor = fuelStats.edit()
        if (fuelStats.getFloat(APP_PREFERENCES_ODOMETER, 0.0f) == 0.0f)
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_notifications_to_firstOdometerReadingFragment)
        inflateFragmentArrayLists()
        initializeViewPagersAndTabs()
        initializeBottomSheetDialogs()
        initListeners()
        displayFuelInTank()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<CardView>(R.id.customBnb).visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        showBalloon()
        editor.putBoolean(Constants.IS_FIRST_LAUNCHED_FUEL_MANAGEMENT, false).apply()
    }

    private fun inflateFragmentArrayLists() {
        lastRideFragments.add(DistanceLastRideFragment())
        lastRideFragments.add(CostLastRideFragment())
        lastRideFragments.add(SpentFuelLastRideFragment())
        lastRideFragments.add(EcologyLastRideFragment())

        newRideFragments.add(DistanceNewRideFragment())
        newRideFragments.add(RemainsFuelNewRideFragment())
        newRideFragments.add(CostNewRideFragment())
        newRideFragments.add(SpendFuelNewRideFragment())
        newRideFragments.add(EcologyNewRideFragment())
    }

    private fun showBalloon() {
        if (fuelStats.getBoolean(Constants.IS_FIRST_LAUNCHED_FUEL_MANAGEMENT, true)) {
            BuildBalloon(requireContext(), getString(R.string.balloon_refueling), viewLifecycleOwner).balloon.showAlignBottom(binding.editButton)
            BuildBalloon(requireContext(), getString(R.string.balloon_fuelStats), viewLifecycleOwner).balloon.showAlignTop(binding.fuelInputButton)
        }
    }

    private fun initListeners() {
        binding.fuelInputButton.setOnClickListener{
            bottomSheetDialogFuelStats.show()
            val viewPager2BottomSheet = bottomSheetDialogFuelStats.findViewById<ViewPager2>(R.id.view_pager_bottom_sheet)
            viewPager2BottomSheet!!.adapter = BottomSheetViewPagerAdapter(this)
            TabLayoutMediator(Objects.requireNonNull(bottomSheetDialogFuelStats.findViewById(R.id.tab)), viewPager2BottomSheet) { _, _ -> }.attach()
        }

        binding.editButton.setOnClickListener{
            bottomSheetDialogFuelInTank.show()
            val tankFuelLevel = bottomSheetDialogFuelInTank.findViewById<EditText>(R.id.fuel_in_tank_text)
            val submit = bottomSheetDialogFuelInTank.findViewById<Button>(R.id.submit_btn_fuel_in_tank)
            val container = bottomSheetDialogFuelInTank.findViewById<ConstraintLayout>(R.id.full_tank)
            submit!!.setOnClickListener{
                if (tankFuelLevel!!.text.toString().isNotEmpty()) {
                    if (tankFuelLevel.text.toString().toFloat() > fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f))
                        showDialogOverFuel()
                    else {
                        editor.putFloat(FUEL_LEVEL_OLD, sharedPreferencesHolder.formattedNumber(tankFuelLevel.text.toString().toFloat()))
                        editor.putFloat(FUEL_LEVEL, sharedPreferencesHolder.formattedNumber(tankFuelLevel.text.toString().toFloat()))
                        editor.apply()
                        bottomSheetDialogFuelInTank.dismiss()
                        sharedPreferencesHolder.calculateRemainsFuel()
                    }
                } else {
                    bottomSheetDialogFuelInTank.dismiss()
                }
            }
            container!!.setOnClickListener{
                tankFuelLevel!!.setText(fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f).toString())
            }
        }
    }

    private fun initializeBottomSheetDialogs(){
        bottomSheetDialogFuelStats = BottomSheetDialog(requireContext())
        bottomSheetDialogFuelStats.setContentView(R.layout.bottom_sheet_dialog_fuel_stats)
        bottomSheetDialogFuelInTank = BottomSheetDialog(requireContext())
        bottomSheetDialogFuelInTank.setContentView(R.layout.bottom_sheet_fuel_in_tank)
    }

    private fun initializeViewPagersAndTabs(){
        val newRideVP = binding.viewPagerNewRide
        val lastRideVP = binding.viewPagerLastRide
        lastRideVP.adapter = LastRideAdapter(this, lastRideFragments)
        newRideVP.adapter = NewRideAdapter(this, newRideFragments)
        TabLayoutMediator(binding.tabLastRide, binding.viewPagerLastRide) { _, _ -> }.attach()
        TabLayoutMediator(binding.tabNewRide, binding.viewPagerNewRide) { _, _ -> }.attach()
    }

    private fun displayFuelInTank(){
        val fuelLevel = fuelStats.getFloat(FUEL_LEVEL, fuelStats.getFloat(FUEL_TANK_CAPACITY, 0.0f))
        if (fuelLevel <= 0.0f){
            editor.putFloat(FUEL_LEVEL, 0.0f).apply()
            binding.fuelLevel.text = String.format("%s %s", 0.0f , getString(R.string.litres_have_left))
            binding.fuelLevel.setTextColor(requireContext().getColor(R.color.red))
        } else {
            binding.fuelLevel.setTextColor(requireContext().getColor(R.color.white))
            binding.fuelLevel.text = String.format("%s %s", fuelLevel, getString(R.string.litres_have_left))
            editor.putFloat(FUEL_LEVEL, fuelLevel).apply()
        }
    }

    private fun showDialogOverFuel(){
        val builder = AlertDialog.Builder(requireContext())
        val customLayout = layoutInflater.inflate(R.layout.edit_fuel_level_error_diallog, null)
        builder.setView(customLayout)
        val dialog = builder.create()
        dialog.show()
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        val okBtn = dialog.findViewById<Button>(R.id.ok_button)
        okBtn!!.setOnClickListener{dialog.dismiss()}
    }

    override fun dismissBottomSheet() {
        bottomSheetDialogFuelStats.dismiss()
    }

}