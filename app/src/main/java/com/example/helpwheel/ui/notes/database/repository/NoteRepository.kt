package com.example.helpwheel.ui.notes.database.repository

import androidx.lifecycle.LiveData
import com.example.helpwheel.ui.notes.model.NoteModel

interface NoteRepository {
    val allNotes: LiveData<List<NoteModel>>
    suspend fun insertNote(noteModel: NoteModel, onSuccess:() -> Unit)
    suspend fun deleteNote(noteModel: NoteModel, onSuccess:() -> Unit)
}