package com.example.helpwheel.notesActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpwheel.MainActivity;
import com.example.helpwheel.R;
import com.example.helpwheel.databases.DatabaseClass;
import com.example.helpwheel.databinding.ActivityAddNotesBinding;
import com.example.helpwheel.databinding.FragmentHomeBinding;
import com.example.helpwheel.ui.home.HomeFragment;

public class AddNotesActivity extends Fragment {
    EditText title, description;
    Button addNote;
    ActivityAddNotesBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNotesBinding.inflate(inflater, container, false);
//        setContentView(R.layout.activity_add_notes);
//        title = findViewById(R.id.title);
//        description = findViewById(R.id.description);
//        addNote = findViewById(R.id.addNote);
        title = binding.title;
        description = binding.description;
        addNote = binding.addNote;

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString()))
                {
//                    DatabaseClass db = new DatabaseClass(AddNotesActivity.this);
                    DatabaseClass db = new DatabaseClass(getContext());
                    db.addNotes(title.getText().toString(), description.getText().toString());
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Intent intent = new Intent(getContext(), MainActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
//                    finish();
                }else {
//                    Toast.makeText(AddNotesActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Both Fields Required", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return binding.getRoot();
    }
}