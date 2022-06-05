package com.example.helpwheel.ui.notes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.helpwheel.R
import com.example.helpwheel.ui.notes.NotesFragment
import com.example.helpwheel.ui.notes.model.NoteModel
import com.google.android.material.button.MaterialButton

class NoteAdapter(private val notesFragment: NotesFragment): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

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
            holder.itemView.findViewById<CardView>(R.id.item_notes_card_view).setOnLongClickListener {
                holder.itemView.findViewById<LottieAnimationView>(R.id.delete_note).visibility = View.VISIBLE
                holder.itemView.findViewById<LottieAnimationView>(R.id.delete_note).setOnClickListener {
                    notesFragment.onRecyclerViewLongClick(notesModel = listNote[position])
                }
                true
            }
            var url = listNote[position].web_site
            if (url.isNotEmpty()){
                holder.itemView.findViewById<ConstraintLayout>(R.id.link_button).visibility = View.VISIBLE
                holder.itemView.findViewById<MaterialButton>(R.id.openURL).setOnClickListener {
                    if (!url.startsWith("http://") || !url.startsWith("https://"))
                        url = "http://$url"
                    notesFragment.openLink(url)
                }
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

        fun removeAt(position: Int):NoteModel {
            return listNote[position]
         }
    }
