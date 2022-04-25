package com.example.helpwheel.ui.welcome_screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentWelcomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class WelcomeFragment extends Fragment {
    FragmentWelcomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.welcomeNextBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_welcomeFragment_to_enterWelcomeDataFragment));
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.GONE);
    }
}