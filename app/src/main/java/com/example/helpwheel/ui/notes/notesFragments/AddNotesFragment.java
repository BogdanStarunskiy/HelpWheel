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
import com.example.helpwheel.databinding.FragmentAddNotesBinding;
import com.example.helpwheel.ui.notes.databases.DatabaseClass;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.HashMap;


public class AddNotesFragment extends Fragment {
    EditText title, description, webURL;
    Button addNote;
    FragmentAddNotesBinding binding;
    HashMap<String, String> months;
    private MaterialDatePicker materialDatePicker;
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
        months = new HashMap<>();
        hashMapCreator();
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
        binding.calendarBtn.setOnClickListener(v1 -> {
            materialDatePicker.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
        });
        materialDatePicker.addOnPositiveButtonClickListener(selection -> getDate());
    }
    private void getDate(){
        String date = materialDatePicker.getHeaderText();
        String[] dateList = date.split(",");
        String[] monthAndDay = dateList[0].split(" ");
        String month =  months.get(monthAndDay[0]);
        String day = monthAndDay[1];
        if (Integer.parseInt(day) < 10)
            day = "0" + day;
        String year = dateList[1].trim();
        binding.description.setText(String.format("%s.%s.%s", day, month, year));
    }

    private void hashMapCreator(){
        months.put("Jan", "01");
        months.put("Feb", "02");
        months.put("Mar", "03");
        months.put("Apr", "04");
        months.put("May", "05");
        months.put("Jun", "06");
        months.put("Jul", "07");
        months.put("Aug", "08");
        months.put("Sep", "09");
        months.put("Oct", "10");
        months.put("Nov", "11");
        months.put("Dec", "12");
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.GONE);
    }
}
