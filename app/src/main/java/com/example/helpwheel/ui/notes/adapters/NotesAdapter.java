package com.example.helpwheel.ui.notes.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpwheel.ui.notes.models.NotesModel;
import com.example.helpwheel.R;
import com.example.helpwheel.ui.notes.interfaces.NotesInterface;
import com.google.android.material.button.MaterialButton;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_recycler_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(notesList.get(position).getTitle());

        String[] splitDesc = notesList.get(position).getDescription().split(" ");
        String descParsed = splitDesc[0];

        if (splitDesc.length != 1) {
            holder.button.setVisibility(View.VISIBLE);

            holder.openUrl.setOnClickListener(view -> {
                String parsedWebUrl = splitDesc[1];
                if (!parsedWebUrl.startsWith("http://") && !parsedWebUrl.startsWith("https://")) {
                    parsedWebUrl = "http://" + parsedWebUrl;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(parsedWebUrl));
                context.startActivity(intent);
            });
        }
        holder.description.setText(descParsed);
        holder.layout.setOnClickListener(view -> callback.fragmentChange(notesList.get(position).getTitle(), notesList.get(position).getDescription(), notesList.get(position).getId()));


    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        RelativeLayout layout, button;
        MaterialButton openUrl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            layout = itemView.findViewById(R.id.note_layout);
            button = itemView.findViewById(R.id.link_button);
            openUrl = itemView.findViewById(R.id.openURL);
        }
    }


    public List<NotesModel> getList() {
        return notesList;
    }

    public void removeItem(int position) {
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(NotesModel item, int position) {
        notesList.add(position, item);
        notifyItemInserted(position);
    }
}