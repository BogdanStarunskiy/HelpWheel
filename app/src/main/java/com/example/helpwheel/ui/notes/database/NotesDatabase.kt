package com.example.helpwheel.ui.notes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.helpwheel.ui.notes.database.dao.NoteDao
import com.example.helpwheel.ui.notes.model.NoteModel

@Database (entities = [NoteModel::class], version = 1)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    companion object {
        @Synchronized
        fun getInstance(context: Context): NotesDatabase {
            return if (database == null) {
                database = Room.databaseBuilder(context, NotesDatabase::class.java, "db").build()
                database as NotesDatabase
            } else {
                database as NotesDatabase
            }
        }

        private var database: NotesDatabase? = null
    }
}