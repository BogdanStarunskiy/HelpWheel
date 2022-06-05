package com.example.helpwheel.ui.notes.notesFragments.update

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentUpdateNotesBinding
import com.example.helpwheel.ui.notes.model.NoteModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class UpdateNotesFragment: Fragment() {
    private lateinit var binding: FragmentUpdateNotesBinding
    private lateinit var updateNotesViewModel: UpdateNotesViewModel
    private lateinit var currentNote: NoteModel
    private lateinit var materialDatePicker: MaterialDatePicker<Long>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateNotesBinding.inflate(inflater, container, false)
        updateNotesViewModel = ViewModelProvider(this)[UpdateNotesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentNote = arguments?.getSerializable("note") as NoteModel
        materialDatePicker =  MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_a_date))
            .build()
        binding.title.setText(currentNote.title)
        binding.description.setText(currentNote.description)
        binding.webUlr.setText(currentNote.web_site)
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<CardView>(R.id.customBnb).visibility = View.GONE
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(selection: Long) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = selection
        val format = SimpleDateFormat("dd.MM.yyyy")
        val formattedDate = format.format(calendar.time)
        binding.description.setText(formattedDate)
    }

    private fun initListeners() {
        binding.updateNote.setOnClickListener{
            val title = binding.title.text.toString()
            val description = binding.description.text.toString()
            val webURL = binding.webUlr.text.toString()
            if (title.isNotEmpty() && description.isNotEmpty() && webURL.isNotEmpty()) {
                updateNotesViewModel.update(NoteModel(id = currentNote.id ,title = title, description = description, web_site = webURL))
                NavHostFragment.findNavController(this).navigate(R.id.action_updateNotesFragment_to_navigation_home)
            } else if (title.isNotEmpty() && description.isNotEmpty()) {
                updateNotesViewModel.update(NoteModel(id = currentNote.id, title = title, description = description))
                NavHostFragment.findNavController(this).navigate(R.id.action_updateNotesFragment_to_navigation_home)
            }
            if (title.isEmpty())
                binding.titleEditText.error = getString(R.string.field_must_be_filled)
            if (description.isEmpty())
                binding.descriptionEditText.error = getString(R.string.field_must_be_filled)
        }
        binding.descriptionEditText.setEndIconOnClickListener{materialDatePicker.show(childFragmentManager, "MATERIAL_DATE_PICKER")}
        binding.description.setOnClickListener{ binding.descriptionEditText.error = null }
        materialDatePicker.addOnPositiveButtonClickListener(this::getDate)
    }
}