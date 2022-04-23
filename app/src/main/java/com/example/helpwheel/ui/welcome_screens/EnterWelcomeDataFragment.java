package com.example.helpwheel.ui.welcome_screens;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentEnterWelcomeDataBinding;
import com.example.helpwheel.models.UserModel;

public class EnterWelcomeDataFragment extends Fragment {
    FragmentEnterWelcomeDataBinding binding;
    private SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    public static final String PREF = "user";
    public static final String USERNAME_PREF = "usernamePref";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEnterWelcomeDataBinding.inflate(inflater, container, false);
        sharedPrefs = requireContext().getSharedPreferences(PREF, getContext().MODE_PRIVATE);
        editor = sharedPrefs.edit();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonOK.setOnClickListener(v -> {
            String username = binding.enterName.getText().toString();
            editor.putString(USERNAME_PREF, username);
            editor.apply();
            NavHostFragment.findNavController(this).navigate(R.id.action_enterWelcomeDataFragment_to_navigation_dashboard);
        });
    }
}