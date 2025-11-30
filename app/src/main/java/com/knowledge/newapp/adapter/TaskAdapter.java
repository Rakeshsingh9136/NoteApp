package com.knowledge.newapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.knowledge.newapp.models.Entities.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskVH> {

    public interface OnTaskClickListener {
        void onEdit(Task task);
        void onDelete(Task task);
    }

    private OnTaskClickListener listener;

    public void setOnTaskClickListener(OnTaskClickListener l) {
        listener = l;
    }

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    // 🔥 Correct DiffUtil
    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Task>() {
                @Override
                public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle())
                            && oldItem.getCategory().equals(newItem.getCategory())
                            && oldItem.getDateTime() == newItem.getDateTime()
                            && oldItem.getRawInput().equals(newItem.getRawInput());
                }
            };

    @NonNull
    @Override
    public TaskVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskVH(LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskVH holder, int position) {
        Task task = getItem(position);
        holder.bind(task);

        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu menu = new PopupMenu(v.getContext(), v);
            menu.getMenu().add("Edit");
            menu.getMenu().add("Delete");

            menu.setOnMenuItemClickListener(item -> {
                if (listener == null) return false;

                if ("Edit".equals(item.getTitle())) {
                    listener.onEdit(task);
                } else if ("Delete".equals(item.getTitle())) {
                    listener.onDelete(task);
                }
                return true;
            });

            menu.show();
            return true;
        });
    }

    static class TaskVH extends RecyclerView.ViewHolder {

        TextView titleView, dateView;

        public TaskVH(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(android.R.id.text1);
            dateView = itemView.findViewById(android.R.id.text2);
        }

        void bind(Task task) {
            titleView.setText(task.getTitle());
            dateView.setText(new SimpleDateFormat("EEE, MMM d - hh:mm a", Locale.getDefault())
                    .format(new Date(task.getDateTime())));
        }
    }
}
