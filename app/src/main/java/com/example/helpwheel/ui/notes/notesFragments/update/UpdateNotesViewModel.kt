package com.example.helpwheel.ui.notes.notesFragments.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpwheel.ui.notes.model.NoteModel
import com.example.helpwheel.utils.REPOSITORY
import kotlinx.coroutines.launch

class UpdateNotesViewModel: ViewModel() {
    fun update(noteModel: NoteModel) =
        viewModelScope.launch {
            REPOSITORY.updateNote(noteModel) {}
        }
}