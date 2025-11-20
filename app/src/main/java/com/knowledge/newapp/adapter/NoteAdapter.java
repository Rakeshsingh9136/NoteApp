package com.knowledge.newapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.knowledge.newapp.R;
import com.knowledge.newapp.models.Entities.NoteEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<NoteEntity> notes = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NoteEntity note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setNotes(List<NoteEntity> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public List<NoteEntity> getNotes() { return notes; }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteEntity note = notes.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
        holder.time.setText(dateFormat.format(new Date(note.getTimestamp())));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(note);
        });
    }

    @Override
    public int getItemCount() { return notes.size(); }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, time;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.itemDescription);
            time = itemView.findViewById(R.id.itemTime);
        }
    }
}
