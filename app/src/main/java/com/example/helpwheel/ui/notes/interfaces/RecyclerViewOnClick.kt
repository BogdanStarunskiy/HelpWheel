package com.example.helpwheel.ui.notes.interfaces

import com.example.helpwheel.ui.notes.model.NoteModel

interface RecyclerViewOnClick {
    fun onRecyclerViewLongClick(position: Int)
    fun onRecyclerViewClick(notesModel: NoteModel)
}
