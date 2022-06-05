package com.example.helpwheel.ui.notes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.helpwheel.R
import com.example.helpwheel.databinding.FragmentNotesBinding
import com.example.helpwheel.ui.notes.adapter.NoteAdapter
import com.example.helpwheel.ui.notes.interfaces.OpenLink
import com.example.helpwheel.ui.notes.interfaces.RecyclerViewOnClick
import com.example.helpwheel.ui.notes.model.NoteModel

class NotesFragment: Fragment(), RecyclerViewOnClick, OpenLink {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        notesViewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<CardView>(R.id.customBnb).visibility = View.VISIBLE
    }

    private fun init(){
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        binding.fab.setOnClickListener{NavHostFragment.findNavController(this).navigate(R.id.action_navigation_home_to_addNotesFragment)}
        notesViewModel.initDatabase()
        adapter = NoteAdapter(this)
        binding.recyclerView.adapter = adapter
        notesViewModel.getAllNotes().observe(viewLifecycleOwner) {
            adapter.setList(it)
            showEmptyPlaceHolder(it)
        }
    }

    private fun showEmptyPlaceHolder(it: List<NoteModel>){
        try {
            if (it.isEmpty()) {
                binding.emptyText.visibility = View.VISIBLE
                binding.emptyImage.visibility = View.VISIBLE
                binding.emptyImage.playAnimation()
            } else {
                binding.emptyText.visibility = View.GONE
                binding.emptyImage.visibility = View.GONE
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onRecyclerViewLongClick(notesModel: NoteModel) {
        notesViewModel.delete(notesModel)
    }

    override fun onRecyclerViewClick(notesModel: NoteModel) {
        val bundle = Bundle()
        bundle.putSerializable("note", notesModel)
        NavHostFragment.findNavController(this).navigate(R.id.action_navigation_home_to_updateNotesFragment, bundle)
    }

    override fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private val swipeHandler = object : SwipeToDeleteCallback() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            notesViewModel.delete(adapter.removeAt(viewHolder.adapterPosition))
        }
    }
    private val itemTouchHelper = ItemTouchHelper(swipeHandler)

}