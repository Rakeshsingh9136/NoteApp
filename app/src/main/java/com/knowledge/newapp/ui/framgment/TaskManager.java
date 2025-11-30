package com.knowledge.newapp.ui.framgment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.knowledge.newapp.ML.NLPParser;
import com.knowledge.newapp.adapter.TaskAdapter;
import com.knowledge.newapp.databinding.FragmentTaskManagerBinding;
import com.knowledge.newapp.models.Entities.Task;
import com.knowledge.newapp.viewmodels.TaskViewModel;

public class TaskManager extends Fragment {

    private TaskViewModel viewModel;
    private TaskAdapter adapter;
    private FragmentTaskManagerBinding binding;

    public TaskManager() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskManagerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Adapter + Recycler
        adapter = new TaskAdapter();
        binding.taskRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.taskRecycler.setAdapter(adapter);

        // Observe LiveData
        viewModel.getTasks().observe(getViewLifecycleOwner(), list -> adapter.submitList(list));

        // Add Task Button
        binding.addTaskBtn.setOnClickListener(v -> addTask());

        // Long press Edit/Delete menu
        adapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onEdit(Task task) { showEditDialog(task); }

            @Override
            public void onDelete(Task task) {
                cancelReminder(task); // cancel alarm
                viewModel.deleteTask(task);
            }
        });

        // Swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView,
                                  @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder,
                                  @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                Task t = adapter.getCurrentList().get(viewHolder.getAdapterPosition());
                cancelReminder(t); // cancel alarm
                viewModel.deleteTask(t);
            }
        }).attachToRecyclerView(binding.taskRecycler);
    }

    /** Add a task with NLP parsing + schedule 5-min reminder */
    private void addTask() {
        String input = binding.inputTask.getText().toString().trim();
        if (input.isEmpty()) return;

        NLPParser.ParsedResult parsed = NLPParser.parse(input);

        Task task = new Task();
        task.setTitle(parsed.title);
        task.setCategory(parsed.category);

        // Set reminder 5 minutes from now
        long reminderTime = System.currentTimeMillis() + 5 * 60 * 1000;
        task.setDateTime(reminderTime);

        task.setRawInput(parsed.raw);

        viewModel.addTask(task);
        binding.inputTask.setText("");

        scheduleReminder(task); // schedule notification
    }

    /** Show edit dialog and reschedule reminder */
    private void showEditDialog(Task task) {
        EditText edit = new EditText(getContext());
        edit.setText(task.getRawInput());

        new AlertDialog.Builder(requireContext())
                .setTitle("Edit Task")
                .setView(edit)
                .setPositiveButton("Save", (d, i) -> {
                    NLPParser.ParsedResult parsed = NLPParser.parse(edit.getText().toString());

                    task.setTitle(parsed.title);
                    task.setCategory(parsed.category);

                    // Update reminder 5 min from now
                    long reminderTime = System.currentTimeMillis() + 5 * 60 * 1000;
                    task.setDateTime(reminderTime);

                    task.setRawInput(parsed.raw);

                    viewModel.updateTask(task);
                    scheduleReminder(task); // reschedule notification
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /** Schedule AlarmManager notification for this task */
    private void scheduleReminder(Task task) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), TaskReminderReceiver.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("id", task.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(),
                task.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    task.getDateTime(),
                    pendingIntent
            );
        }
    }

    /** Cancel existing alarm if task is deleted */
    private void cancelReminder(Task task) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getContext(), TaskReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(),
                task.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // avoid memory leaks
    }
}
