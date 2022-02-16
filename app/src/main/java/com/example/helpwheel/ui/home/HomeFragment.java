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


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpwheel.R;
import com.example.helpwheel.adapters.NotesAdapter;


import com.example.helpwheel.Models.NotesModel;

import com.example.helpwheel.databases.DatabaseClass;
import com.example.helpwheel.databinding.FragmentHomeBinding;
import com.example.helpwheel.notesActivity.AddNotesActivity;

import com.example.helpwheel.ui.dashboard.DashboardFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    NotesAdapter adapter;
    List<NotesModel> notesList;
    DatabaseClass databaseClass;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        //Запуск активити для добавления заметок через плавающую кнопку
//        binding.fab.setOnClickListener(view -> {
//
//            Intent intent = new Intent(getContext(), AddNotesActivity.class);
//            startActivity(intent);
//
//        });

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(binding.recyclerView);

        notesList = new ArrayList<>();
        databaseClass = new DatabaseClass(getContext());
        fetchAllNotesFromDatabase();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotesAdapter(getContext(), getActivity(), notesList);
        binding.recyclerView.setAdapter(adapter);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AddNotesActivity fr = new AddNotesActivity();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft.replace(R.id.notes_fragment_place, fr);
                ft.commit();
            }
        });

        return binding.getRoot();
    }
    //достаём данные (заметки) из бд
    void fetchAllNotesFromDatabase() {
        //переменная с данными с бд
        Cursor cursor = databaseClass.readAllData();
        //проверка количества заметок
        if (cursor.getCount() == 0) {
            binding.emptyText.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                notesList.add(new NotesModel(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }

    }
    //обработка свайпов
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        //удаление заметки
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            NotesModel item = adapter.getList().get(position);

            adapter.removeItem(position);
            //Snack bar для отмены удаления
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