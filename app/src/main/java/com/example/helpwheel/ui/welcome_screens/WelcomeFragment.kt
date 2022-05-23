package com.example.helpwheel.ui.welcome_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentWelcomeBinding


class WelcomeFragment: Fragment() {
    lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.welcomeNextBtn.setOnClickListener{NavHostFragment.findNavController(this).navigate(R.id.action_welcomeFragment_to_enterWelcomeDataFragment)}
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