package com.petrovic.m.dimitrije.activitytracker.rest;

import android.content.Context;
import android.content.SharedPreferences;

import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.data.model.LoggedInUser;

/**
 * Session manager to save and fetch data from SharedPreferences
 */
public class SessionManager {

    public static final String TOKEN = "token";

    private static volatile SessionManager instance = null;

    private SharedPreferences sharedPrefs;
    private LoggedInUser user = null;

    private SessionManager(Context context) {
        this.sharedPrefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    // singleton
    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    /**
     * Function to save auth token
     */
    public void saveAuthToken(String token) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

    /**
     * Function to fetch auth token
     */
    public String fetchAuthToken() {
        return sharedPrefs.getString(TOKEN, null);
    }

    /**
     * Function to remove auth token
     */
    public void removeAuthToken() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(TOKEN);
        editor.apply();
    }

    public SharedPreferences getSharedPrefs() {
        return sharedPrefs;
    }

    public void setSharedPrefs(SharedPreferences sharedPrefs) {
        this.sharedPrefs = sharedPrefs;
    }

    public LoggedInUser getUser() {
        return user;
    }

    public void setUser(LoggedInUser user) {
        this.user = user;
    }
}