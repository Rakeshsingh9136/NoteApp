package com.knowledge.newapp.repository;

import android.content.Context;

import com.knowledge.newapp.models.Dao.UserDao;
import com.knowledge.newapp.models.Db.AppDatabase;
import com.knowledge.newapp.models.Entities.User;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
    }

    public void registerUser(User user) {
        userDao.insertUser(user);
    }

    public User login(String email, String password) {
        return userDao.login(email, password);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public void resetPassword(String email, String newPassword) {
        userDao.updatePassword(email, newPassword);
    }
}
