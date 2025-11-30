package com.knowledge.newapp.models.Db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.knowledge.newapp.models.Dao.NoteDao;
import com.knowledge.newapp.models.Dao.TaskDao;
import com.knowledge.newapp.models.Dao.UserDao;
import com.knowledge.newapp.models.Entities.NoteEntity;
import com.knowledge.newapp.models.Entities.Task;
import com.knowledge.newapp.models.Entities.User;

@Database(entities = {User.class, NoteEntity.class, Task.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract NoteDao noteDao();
    public  abstract TaskDao taskDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "Notes_database")
                    .allowMainThreadQueries() // For simplicity; remove in production
                    .build();
        }
        return instance;
    }
}
