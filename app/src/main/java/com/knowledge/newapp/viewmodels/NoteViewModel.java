package com.knowledge.newapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.knowledge.newapp.models.Entities.NoteEntity;
import com.knowledge.newapp.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<NoteEntity>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public LiveData<List<NoteEntity>> getAllNotes() { return allNotes; }
    public void insert(NoteEntity note) { repository.insert(note); }
    public void update(NoteEntity note) { repository.update(note); }
    public void delete(NoteEntity note) { repository.delete(note); }
}
