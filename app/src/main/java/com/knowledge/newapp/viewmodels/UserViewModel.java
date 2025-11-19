package com.knowledge.newapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.knowledge.newapp.models.Entities.User;
import com.knowledge.newapp.repository.UserRepository;
import com.knowledge.newapp.session.SessionManager;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private SessionManager session;

    public MutableLiveData<User> loginResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> registerResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> passwordResetResult = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        session = new SessionManager(application);
    }

    // ---------------- LOGIN ----------------
    public void login(String email, String password) {
        repository.login(email, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                session.setLoggedIn(true);   // Save login state
                loginResult.postValue(user);
            }

            @Override
            public void onFailure(String msg) {
                error.postValue(msg);
                loginResult.postValue(null);
            }
        });
    }

    // ---------------- LOGOUT ----------------
    public void logout() {
        session.setLoggedIn(false);
    }

    // ---------------- REGISTER ----------------
    public void register(User user) {
        repository.registerUser(user, new UserRepository.RegisterCallback() {
            @Override
            public void onSuccess() {
                registerResult.postValue(true);
            }

            @Override
            public void onFailure(String msg) {
                error.postValue(msg);
                registerResult.postValue(false);
            }
        });
    }

    // ---------------- RESET PASSWORD ----------------
    public void resetPassword(String email, String newPassword) {
        repository.resetPassword(email, newPassword, new UserRepository.PasswordResetCallback() {
            @Override
            public void onSuccess() {
                passwordResetResult.postValue(true);
            }

            @Override
            public void onFailure(String msg) {
                error.postValue(msg);
                passwordResetResult.postValue(false);
            }
        });
    }

    public boolean isLoggedIn() {
        return session.isLoggedIn();
    }
}
