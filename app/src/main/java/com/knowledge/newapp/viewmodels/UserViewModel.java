package com.knowledge.newapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.knowledge.newapp.models.Entities.User;
import com.knowledge.newapp.repository.UserRepository;

public class UserViewModel  extends AndroidViewModel {

    private UserRepository repository;

    public MutableLiveData<User> loginResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> registerResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> passwordResetResult = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void register(User user) {
        repository.registerUser(user);
        registerResult.setValue(true);
    }

    public void login(String email, String password) {
        User user = repository.login(email, password);
        loginResult.setValue(user);
    }

    public void resetPassword(String email, String newPassword) {
        repository.resetPassword(email, newPassword);
        passwordResetResult.setValue(true);
    }
}