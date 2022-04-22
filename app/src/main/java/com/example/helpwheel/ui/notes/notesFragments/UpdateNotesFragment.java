package com.example.helpwheel.ui.notes.notesFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentUpdateNotesBinding;
import com.example.helpwheel.ui.notes.databases.DatabaseClass;


public class UpdateNotesFragment extends Fragment {
    private FragmentUpdateNotesBinding binding;
    String id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUpdateNotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UpdateNotesFragmentArgs args = UpdateNotesFragmentArgs.fromBundle(getArguments());
        binding.title.setText(args.getTitle());
        String[] splitDesc = args.getDescription().split(" ");
        String descParser = splitDesc[0];
        if (splitDesc.length != 1) {
            String webUrlParser = splitDesc[1];
            binding.webUlr.setText(webUrlParser);
        }

        binding.description.setText(descParser);


        id = args.getId();
        binding.updateNote.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.title.getText().toString()) && !TextUtils.isEmpty(binding.description.getText().toString())) {
                String descWebUrlCombined = binding.description.getText().toString().trim() + " " + binding.webUlr.getText().toString().trim();
                DatabaseClass db = new DatabaseClass(getContext());
                db.updateNotes(binding.title.getText().toString(), descWebUrlCombined, id);
                NavHostFragment.findNavController(this).navigate(R.id.action_updateNotesFragment_to_navigation_home);
            } else
                Toast.makeText(getContext(), R.string.both_fields_required, Toast.LENGTH_SHORT).show();
        });
    }
}