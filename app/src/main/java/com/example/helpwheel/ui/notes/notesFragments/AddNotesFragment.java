package com.example.helpwheel.ui.notes.notesFragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpwheel.R;
import com.example.helpwheel.databinding.FragmentAddNotesBinding;
import com.example.helpwheel.ui.notes.databases.DatabaseClass;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class AddNotesFragment extends Fragment {
    EditText title, description, webURL;
    Button addNote;
    FragmentAddNotesBinding binding;
    private MaterialDatePicker<Long> materialDatePicker;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAddNotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        materialDatePicker = materialDateBuilder.build();
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
            }
            if (title.getText().toString().isEmpty()){
                binding.titleEditText.setError(getString(R.string.field_must_be_filled));
            }
            if (description.getText().toString().isEmpty()){
                binding.descriptionEditText.setError(getString(R.string.field_must_be_filled));
            }

        });
        binding.descriptionEditText.setEndIconOnClickListener(v1 -> materialDatePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(this::getDate);
    }
    private void getDate(Long selection){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(selection);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate  = format.format(calendar.getTime());
        binding.description.setText(formattedDate);
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.GONE);
    }
}
