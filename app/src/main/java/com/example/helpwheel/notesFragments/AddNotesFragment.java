package com.example.helpwheel.notesFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.helpwheel.MainActivity;
import com.example.helpwheel.databases.DatabaseClass;
import com.example.helpwheel.databinding.FragmentAddNotesBinding;


public class AddNotesFragment extends Fragment {
    EditText title, description;
    Button addNote;
    FragmentAddNotesBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAddNotesBinding.inflate(inflater, container, false);
        title = binding.title;
        description = binding.description;
        addNote = binding.addNote;

        addNote.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString()))
            {
                DatabaseClass db = new DatabaseClass(getContext());
                db.addNotes(title.getText().toString(), description.getText().toString());
                Intent intent = new Intent(getContext(), MainActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                Toast.makeText(getContext(), "Both Fields Required", Toast.LENGTH_SHORT).show();
            }

        });
        return binding.getRoot();
    }
}