package com.example.helpwheel.ui.notes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.helpwheel.R
import com.example.helpwheel.ui.notes.NotesFragment
import com.example.helpwheel.ui.notes.model.NoteModel

class NoteAdapter(val notesFragment: NotesFragment): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

        private var listNote = emptyList<NoteModel>()

        class NoteViewHolder(view: View): RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_recycler_view_layout, parent,false)
            return NoteViewHolder(view)
        }

        override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.title).text = listNote[position].title
            holder.itemView.findViewById<TextView>(R.id.description).text = listNote[position].description
            holder.itemView.findViewById<CardView>(R.id.item_notes_card_view).setOnClickListener {
                notesFragment.onRecyclerViewClick(listNote[position])
            }
        }

        override fun getItemCount(): Int {
            return listNote.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun setList(list: List<NoteModel>){
            listNote = list
            notifyDataSetChanged()
        }

        override fun onViewAttachedToWindow(holder: NoteViewHolder) {
            super.onViewAttachedToWindow(holder)
            holder.itemView.setOnClickListener{

            }
        }

        override fun onViewDetachedFromWindow(holder: NoteViewHolder) {
            holder.itemView.setOnClickListener(null)
        }
    }
