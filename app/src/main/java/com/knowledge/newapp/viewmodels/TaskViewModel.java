package com.knowledge.newapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.knowledge.newapp.models.Entities.Task;
import com.knowledge.newapp.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;

    public TaskViewModel(@NonNull Application app) {
        super(app);
        repository = new TaskRepository(app);
    }

    public void addTask(Task task) {
        repository.insert(task);
    }

    public void updateTask(Task task) {
        repository.update(task);
    }

    public void deleteTask(Task task) {
        repository.delete(task);
    }

    public LiveData<List<Task>> getTasks() {
        return repository.getAllTasks();
    }
}
