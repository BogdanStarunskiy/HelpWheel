package com.example.helpwheel.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpwheel.interfaces.NotesInterface;
import com.example.helpwheel.Models.NotesModel;
import com.example.helpwheel.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    Context context;
    Activity activity;
    List<NotesModel> notesList;
    NotesInterface callback;

    public NotesAdapter(Context context, Activity activity, List<NotesModel> notesList, NotesInterface mcallback) {
        this.callback = mcallback;
        this.context = context;
        this.activity = activity;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_recycler_view_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(notesList.get(position).getTitle());
        holder.description.setText(notesList.get(position).getDescription());

        holder.layout.setOnClickListener(view -> {
           callback.fragmentChange(notesList.get(position).getTitle(), notesList.get(position).getDescription(), notesList.get(position).getId());
        });

    }
    //получить количество заметок
    @Override
    public int getItemCount() {
        return notesList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, description, webURL;
        RelativeLayout layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            webURL = itemView.findViewById(R.id.web_url);
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