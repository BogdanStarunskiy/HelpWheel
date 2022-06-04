package com.example.helpwheel.ui.notes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note_table")
class NoteModel (
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        @ColumnInfo
        var title: String = "",
        @ColumnInfo
        var description: String = "",
        @ColumnInfo
        var web_site: String = ""
) : Serializable

