package com.knowledge.newapp.repository;

import android.content.Context;

import com.knowledge.newapp.models.Dao.UserDao;
import com.knowledge.newapp.models.Db.AppDatabase;
import com.knowledge.newapp.models.Entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private UserDao userDao;
    private ExecutorService executorService;

    public interface LoginCallback {
        void onSuccess(User user);
        void onFailure(String msg);
    }

    public interface RegisterCallback {
        void onSuccess();
        void onFailure(String msg);
    }

    public interface PasswordResetCallback {
        void onSuccess();
        void onFailure(String msg);
    }

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();

        // Background thread for Room operations
        executorService = Executors.newSingleThreadExecutor();
    }

    // ---------------- REGISTER ----------------
    public void registerUser(User user, RegisterCallback callback) {
        executorService.execute(() -> {
            try {
                User existingUser = userDao.getUserByEmail(user.getEmail());
                if (existingUser != null) {
                    callback.onFailure("User already exists");
                } else {
                    userDao.insertUser(user);
                    callback.onSuccess();
                }
            } catch (Exception e) {
                callback.onFailure("Registration failed");
            }
        });
    }

    // ---------------- LOGIN ----------------
    public void login(String email, String password, LoginCallback callback) {
        executorService.execute(() -> {
            try {
                User user = userDao.login(email, password);
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onFailure("Invalid email or password");
                }
            } catch (Exception e) {
                callback.onFailure("Login failed");
            }
        });
    }

    // ---------------- RESET PASSWORD ----------------
    public void resetPassword(String email, String newPassword, PasswordResetCallback callback) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserByEmail(email);
                if (user == null) {
                    callback.onFailure("User not found");
                } else {
                    userDao.updatePassword(email, newPassword);
                    callback.onSuccess();
                }
            } catch (Exception e) {
                callback.onFailure("Password reset failed");
            }
        });
    }
}
