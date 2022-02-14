package com.example.helpwheel;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.helpwheel.databinding.ActivityMainBinding;
import com.example.helpwheel.notesActivity.AddNotesActivity;
import com.example.helpwheel.ui.dashboard.DashboardFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FloatingActionButton fab = findViewById(R.id.fab);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AddNotesActivity fr = new AddNotesActivity();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ft.replace(R.id.nav_host_fragment_activity_main, fr);
                ft.commit();
            }
        });



        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        try{
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NavigationUI.setupWithNavController(binding.navView, navController);


    }
}