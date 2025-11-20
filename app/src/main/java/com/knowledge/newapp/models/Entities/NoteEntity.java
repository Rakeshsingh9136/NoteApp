package com.knowledge.newapp.models.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "notes")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private long timestamp;

    public NoteEntity(String title, String description, long timestamp) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public long getTimestamp() { return timestamp; }
}
