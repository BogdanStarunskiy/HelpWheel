package com.example.helpwheel.ui.home;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.helpwheel.R;
import com.example.helpwheel.adapters.NotesAdapter;
import com.example.helpwheel.Models.NotesModel;
import com.example.helpwheel.databases.DatabaseClass;
import com.example.helpwheel.databinding.FragmentHomeBinding;
import com.example.helpwheel.notesFragments.AddNotesFragment;
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
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(binding.recyclerView);

        notesList = new ArrayList<>();
        databaseClass = new DatabaseClass(getContext());
        fetchAllNotesFromDatabase();

        adapter = new NotesAdapter(getContext(), getActivity(), notesList);
        binding.recyclerView.setAdapter(adapter);
        //Запуск Фрагмента для добавления заметок через плавающую кнопку
        binding.fab.setOnClickListener(view ->{
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_home_to_addNotesFragment);
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