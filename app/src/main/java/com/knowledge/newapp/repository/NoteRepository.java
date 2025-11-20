package com.knowledge.newapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.knowledge.newapp.models.Dao.NoteDao;
import com.knowledge.newapp.models.Db.AppDatabase;
import com.knowledge.newapp.models.Entities.NoteEntity;

import java.util.List;
import java.util.concurrent.Executors;

public class NoteRepository {

    private final NoteDao noteDao;
    private final LiveData<List<NoteEntity>> allNotes;

    public NoteRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        noteDao = db.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public LiveData<List<NoteEntity>> getAllNotes() { return allNotes; }

    public void insert(NoteEntity note) {
        Executors.newSingleThreadExecutor().execute(() -> noteDao.insert(note));
    }

    public void update(NoteEntity note) {
        Executors.newSingleThreadExecutor().execute(() -> noteDao.update(note));
    }

    public void delete(NoteEntity note) {
        Executors.newSingleThreadExecutor().execute(() -> noteDao.delete(note));
    }
}

