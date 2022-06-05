package com.example.helpwheel.ui.notes.interfaces

import com.example.helpwheel.ui.notes.model.NoteModel

interface RecyclerViewOnClick {
    fun onRecyclerViewLongClick(notesModel: NoteModel)
    fun onRecyclerViewClick(notesModel: NoteModel)
}
