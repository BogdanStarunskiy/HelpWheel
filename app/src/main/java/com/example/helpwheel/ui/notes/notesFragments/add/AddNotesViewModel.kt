package com.example.helpwheel.ui.notes.notesFragments.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpwheel.ui.notes.model.NoteModel
import com.example.helpwheel.utils.REPOSITORY
import kotlinx.coroutines.launch

class AddNotesViewModel: ViewModel() {
    fun insert(noteModel: NoteModel, onSuccess: () -> Unit) =
        viewModelScope.launch {
            REPOSITORY.insertNote(noteModel) {
                onSuccess()
            }
        }
}