package com.example.helpwheel.ui.home.notesFragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.helpwheel.MainActivity;
import com.example.helpwheel.R;
import com.example.helpwheel.ui.home.databases.DatabaseClass;
import com.example.helpwheel.databinding.FragmentAddNotesBinding;


public class AddNotesFragment extends Fragment {
    EditText title, description, webURL;
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
        webURL = binding.webUlr;
        addNote.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString())) {
                String descUrl = description.getText().toString().trim() + " " + webURL.getText().toString().trim();
                DatabaseClass db = new DatabaseClass(getContext());
                db.addNotes(title.getText().toString(), descUrl);
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), R.string.both_fields_required, Toast.LENGTH_SHORT).show();
            }

        });
        return binding.getRoot();
    }
}