package com.example.helpwheel.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;


import androidx.fragment.app.Fragment;


import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpwheel.Adapter;


import com.example.helpwheel.Model;

import com.example.helpwheel.databases.DatabaseClass;
import com.example.helpwheel.databinding.FragmentHomeBinding;
import com.example.helpwheel.notesActivity.AddNotesActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    Adapter adapter;
    List<Model> notesList;
    DatabaseClass databaseClass;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddNotesActivity.class);
            startActivity(intent);
        });
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(binding.recyclerView);

        notesList = new ArrayList<>();
        databaseClass = new DatabaseClass(getContext());
        fetchAllNotesFromDatabase();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), getActivity(), notesList);
        binding.recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    void fetchAllNotesFromDatabase() {
        Cursor cursor = databaseClass.readAllData();

        if (cursor.getCount() == 0) {
            binding.emptyText.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                notesList.add(new Model(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }

    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Model item = adapter.getList().get(position);

            adapter.removeItem(position);

            Snackbar snackbar = Snackbar.make(binding.notesLayout, "Item Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", view -> {
                        adapter.restoreItem(item, position);
                        binding.recyclerView.scrollToPosition(position);
                    }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            if (!(event == DISMISS_EVENT_ACTION)){
                                DatabaseClass db = new DatabaseClass(getContext());
                                db.deleteSingleItem(item.getId());
                            }
                        }
                    });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}