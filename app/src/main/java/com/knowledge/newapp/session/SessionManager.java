package com.knowledge.newapp.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    public void setLoggedIn(boolean value) {
        prefs.edit().putBoolean("isLoggedIn", value).apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean("isLoggedIn", false);
    }
}
