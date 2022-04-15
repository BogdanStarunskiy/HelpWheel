package com.example.helpwheel;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.helpwheel.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static final String MY_SETTINGS = "settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
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
        if(isFirstInitShared()){
        showWelcomeDialog();
        changeUiGreeting();
        }

    }
    private void showWelcomeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.WelcomeAlertDialog);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_ok_dialog, (ConstraintLayout)findViewById(R.id.layoutDialogContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.greeting_auth));
        ((EditText)view.findViewById(R.id.textMessage)).setHint(getResources().getString(R.string.enter_name_auth));
        ((Button)view.findViewById(R.id.buttonOK)).setText(getResources().getString(R.string.btn_auth));

        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if(alertDialog.getWindow() != null ){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }
    private boolean isFirstInitShared() {
        SharedPreferences sp = getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        boolean hasVisited = sp.getBoolean("hasVisited", false);

        if (!hasVisited) {
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.apply(); // applying changes
        }
        return !hasVisited;

    }
    public String getUsername(){
        String username;
        EditText enterUsername = findViewById(R.id.textMessage);
        username = enterUsername.getText().toString().trim();

        return username;
    }
    private void changeUiGreeting(){
        TextView greeting = findViewById(R.id.greeting_text);
        greeting.setText(getUsername());
    }
}