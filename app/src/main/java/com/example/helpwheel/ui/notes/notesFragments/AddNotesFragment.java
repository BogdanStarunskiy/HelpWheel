package com.example.helpwheel.ui.notes.notesFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpwheel.R;
import com.example.helpwheel.ui.notes.databases.DatabaseClass;
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
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.GONE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = binding.title;
        description = binding.description;
        addNote = binding.addNote;
        webURL = binding.webUlr;
        addNote.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString())) {
                String descUrl = description.getText().toString().trim() + " " + webURL.getText().toString().trim();
                DatabaseClass db = new DatabaseClass(getContext());
                db.addNotes(title.getText().toString().trim(), descUrl);
                NavHostFragment.findNavController(this).navigate(R.id.action_addNotesFragment_to_navigation_home);
            } else {
                Toast.makeText(getContext(), R.string.both_fields_required, Toast.LENGTH_SHORT).show();
            }

        });
    }
}
