package com.knowledge.newapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.knowledge.newapp.models.Dao.TaskDao;
import com.knowledge.newapp.models.Db.AppDatabase;
import com.knowledge.newapp.models.Entities.Task;

import java.util.List;
import java.util.concurrent.Executors;

public class TaskRepository {

    private TaskDao taskDao;

    public TaskRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        taskDao = db.taskDao();
    }

    public void insert(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.insert(task));
    }

    public void update(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.delete(task));
    }

    public LiveData<List<Task>> getAllTasks() {
        return taskDao.getAllTasks();
    }
}
