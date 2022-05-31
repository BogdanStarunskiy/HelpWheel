package com.example.helpwheel.ui.notes.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.helpwheel.R
import com.example.helpwheel.ui.notes.interfaces.NotesInterface
import com.example.helpwheel.ui.notes.interfaces.RecyclerViewLongClick
import com.example.helpwheel.ui.notes.models.NotesModel
import com.google.android.material.button.MaterialButton

class NotesAdapter(
    var context: Context?,
    private var notesList: MutableList<NotesModel>?,
    private var callback: NotesInterface?,
    private var longClick: RecyclerViewLongClick?
) : RecyclerView.Adapter<NotesAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_recycler_view_layout, parent, false)
        return MyViewHolder(view, longClick)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.title.text = notesList!![position].title
        val splitDesc = notesList!![position].description.split(" ").toTypedArray()
        val descParsed = splitDesc[0]
        if (splitDesc.size != 1) {
            holder.button.visibility = View.VISIBLE
            holder.openUrl.setOnClickListener {
                var parsedWebUrl = splitDesc[1]
                if (!parsedWebUrl.startsWith("http://") && !parsedWebUrl.startsWith("https://")) {
                    parsedWebUrl = "http://$parsedWebUrl"
                }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(parsedWebUrl))
                context!!.startActivity(intent)
            }
        }
        holder.description.text = descParsed
        holder.layout.setOnClickListener {
            callback!!.fragmentChange(
                notesList!![position].title,
                notesList!![position].description,
                notesList!![position].id
            )
        }
    }

    override fun getItemCount(): Int {
        return notesList!!.size
    }

    class MyViewHolder(itemView: View, callback: RecyclerViewLongClick?) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var description: TextView
        var layout: ConstraintLayout
        var button: ConstraintLayout
        var openUrl: MaterialButton
        private var deleteButton: LottieAnimationView

        init {
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            layout = itemView.findViewById(R.id.note_layout)
            button = itemView.findViewById(R.id.link_button)
            openUrl = itemView.findViewById(R.id.openURL)
            deleteButton = itemView.findViewById(R.id.delete_note)
            itemView.setOnLongClickListener {
                deleteButton.visibility = View.VISIBLE
                deleteButton.setOnClickListener {
                    callback!!.onRecyclerViewLongClick(
                        layoutPosition
                    )
                }
                true
            }
        }
    }


    fun getList(): List<NotesModel>? {
        return notesList
    }

    fun removeItem(position: Int) {
        notesList?.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: NotesModel, position: Int) {
        notesList?.add(position, item)
        notifyItemInserted(position)
    }
}