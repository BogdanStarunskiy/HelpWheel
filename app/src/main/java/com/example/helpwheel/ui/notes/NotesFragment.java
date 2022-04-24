package com.example.helpwheel.ui.notes;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpwheel.ui.notes.models.NotesModel;
import com.example.helpwheel.R;
import com.example.helpwheel.ui.notes.adapters.NotesAdapter;
import com.example.helpwheel.databinding.FragmentNotesBinding;
import com.example.helpwheel.ui.notes.databases.DatabaseClass;
import com.example.helpwheel.ui.notes.interfaces.NotesInterface;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment implements NotesInterface {

    private FragmentNotesBinding binding;

    NotesAdapter adapter;
    List<NotesModel> notesList;
    DatabaseClass databaseClass;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        requireActivity().findViewById(R.id.customBnb).setVisibility(View.VISIBLE);
        super.onViewCreated(view, savedInstanceState);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(binding.recyclerView);

        notesList = new ArrayList<>();
        databaseClass = new DatabaseClass(getContext());
        fetchAllNotesFromDatabase();

        adapter = new NotesAdapter(getContext(), getActivity(), notesList, this);
        binding.recyclerView.setAdapter(adapter);

        //Launch AddNotesFragment
        binding.fab.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_navigation_home_to_addNotesFragment));

    }

    //Taking all notes from database
    void fetchAllNotesFromDatabase() {
        Cursor cursor = databaseClass.readAllData();
        //If we don`t have any notes
        if (cursor.getCount() == 0) {
            try {
                binding.emptyText.setVisibility(View.VISIBLE);
                binding.emptyImage.setVisibility(View.VISIBLE);
            } catch (Exception e){

            }
        }
        else {
            while (cursor.moveToNext()) {
                notesList.add(new NotesModel(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(2)));
            }
        }

    }
    //Swipes
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        //Notates removing
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            NotesModel item = adapter.getList().get(position);
            adapter.removeItem(position);
            //Snack bar for cancelling action
            Snackbar snackbar = Snackbar.make(binding.notesLayout, R.string.on_item_deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo, view -> {
                        adapter.restoreItem(item, position);
                        binding.recyclerView.scrollToPosition(position);
                    }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            if (!(event == DISMISS_EVENT_ACTION)){
                                DatabaseClass db = new DatabaseClass(getContext());
                                db.deleteSingleItem(item.getId());
                                fetchAllNotesFromDatabase();
                            }
                        }
                    });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    };

    @Override
    public void fragmentChange(String title, String description, String id) {
        NotesFragmentDirections.ActionNavigationHomeToUpdateNotesFragment action = NotesFragmentDirections.actionNavigationHomeToUpdateNotesFragment();
        action.setTitle(title);
        action.setDescription(description);
        action.setId(id);
        NavHostFragment.findNavController(this).navigate(action);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}