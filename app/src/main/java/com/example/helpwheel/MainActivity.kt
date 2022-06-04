package com.example.helpwheel

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.helpwheel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        this.setTheme(R.style.Theme_HelpWheel)
        setContentView(binding.root)
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
        ).build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)
        try {
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        val configuration = Configuration(newBase.resources.configuration)
        configuration.fontScale = 1.0f
        applyOverrideConfiguration(configuration)
    }
}