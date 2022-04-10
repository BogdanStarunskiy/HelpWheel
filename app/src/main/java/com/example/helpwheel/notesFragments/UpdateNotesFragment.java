package com.example.helpwheel.notesFragments;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.helpwheel.MainActivity;
import com.example.helpwheel.databases.DatabaseClass;
import com.example.helpwheel.databinding.FragmentHomeBinding;
import com.example.helpwheel.databinding.FragmentUpdateNotesBinding;


public class UpdateNotesFragment extends Fragment {
    private FragmentUpdateNotesBinding binding;
    String id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UpdateNotesFragmentArgs args = UpdateNotesFragmentArgs.fromBundle(getArguments());
        binding = FragmentUpdateNotesBinding.inflate(inflater, container, false);
        binding.title.setText(args.getTitle());
        String[] splitDesc = args.getDescription().split(" ");
        String descParser = splitDesc[0];
        if (splitDesc.length != 1){
            String webUrlParser = splitDesc[1];
            binding.webUlr.setText(webUrlParser);
        };
        binding.description.setText(descParser);


        id = args.getId();
        binding.updateNote.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(binding.title.getText().toString()) && !TextUtils.isEmpty(binding.description.getText().toString())){
                String descWebUrlCombined = binding.description.getText().toString().trim() + " " + binding.webUlr.getText().toString().trim();
                DatabaseClass db = new DatabaseClass(getContext());
                db.updateNotes(binding.title.getText().toString(), descWebUrlCombined, id);
                Intent i1 = new Intent(getContext(), MainActivity.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i1);
            }else {
                Toast.makeText(getContext(), "Both Fields Required", Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }


}