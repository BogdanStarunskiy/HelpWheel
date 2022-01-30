package com.example.helpwheel.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpwheel.Adapter;
import com.example.helpwheel.MainActivity;
import com.example.helpwheel.Model;
import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentHomeBinding;
import com.example.helpwheel.notesActivity.AddNotesActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    Adapter adapter;
    List<Model> notesList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddNotesActivity.class);
                startActivity(intent);
            }
        });
        binding.recyclerView.setAdapter(adapter);
        return binding.getRoot();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}