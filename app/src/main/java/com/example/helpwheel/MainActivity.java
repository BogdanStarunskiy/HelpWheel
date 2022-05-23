package com.example.helpwheel;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.helpwheel.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.helpwheel.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        this.setTheme(R.style.Theme_HelpWheel);
        setContentView(binding.getRoot());
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        try {
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        Configuration configuration = new Configuration(newBase.getResources().getConfiguration());
        configuration.fontScale = 1.0f;
        applyOverrideConfiguration(configuration);
    }
}