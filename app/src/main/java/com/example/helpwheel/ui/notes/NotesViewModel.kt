package com.example.helpwheel.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.helpwheel.ui.notes.database.NotesDatabase
import com.example.helpwheel.ui.notes.database.repository.NoteRealisation
import com.example.helpwheel.ui.notes.model.NoteModel
import com.example.helpwheel.utils.REPOSITORY

class NotesViewModel(application: Application): AndroidViewModel(application) {
    private val context = application

    fun initDatabase(){
        val daoNote = NotesDatabase.getInstance(context).getNoteDao()
        REPOSITORY = NoteRealisation(daoNote)
    }

    fun getAllNotes(): LiveData<List<NoteModel>> {
        return REPOSITORY.allNotes
    }
}