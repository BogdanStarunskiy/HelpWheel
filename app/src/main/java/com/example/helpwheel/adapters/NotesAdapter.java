package com.example.helpwheel.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpwheel.Models.NotesModel;
import com.example.helpwheel.R;
import com.example.helpwheel.updateNotesActivity.UpdateNotesActivity;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    Context context;
    Activity activity;
    List<NotesModel> notesList;

    public NotesAdapter(Context context, Activity activity, List<NotesModel> notesList) {
        this.context = context;
        this.activity = activity;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(notesList.get(position).getTitle());
        holder.description.setText(notesList.get(position).getDescription());

        holder.layout.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateNotesActivity.class);
            intent.putExtra("title", notesList.get(position).getTitle());
            intent.putExtra("description", notesList.get(position).getDescription());
            intent.putExtra("id", notesList.get(position).getId());

            activity.startActivity(intent);
        });

    }
    //получить количество заметок
    @Override
    public int getItemCount() {
        return notesList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, description;
        RelativeLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            layout = itemView.findViewById(R.id.note_layout);
        }
    }


    //получить список заметок
    public List<NotesModel> getList(){
        return notesList;
    }

    //удаление заметок
    public void removeItem(int position){
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    //восстановление заметок
    public void restoreItem(NotesModel item, int position){
        notesList.add(position, item);
        notifyItemInserted(position);
    }
}