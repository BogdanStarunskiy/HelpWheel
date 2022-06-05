package com.example.helpwheel.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.helpwheel.ui.notes.database.NotesDatabase
import com.example.helpwheel.ui.notes.database.repository.NoteRealisation
import com.example.helpwheel.ui.notes.model.NoteModel
import com.example.helpwheel.utils.REPOSITORY
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {
    private val context = application

    fun initDatabase(){
        val daoNote = NotesDatabase.getInstance(context).getNoteDao()
        REPOSITORY = NoteRealisation(daoNote)
    }

    fun delete(noteModel: NoteModel){
        viewModelScope.launch {
            REPOSITORY.deleteNote(noteModel) {}
        }
    }

    fun getAllNotes(): LiveData<List<NoteModel>> {
        return REPOSITORY.allNotes
    }
}