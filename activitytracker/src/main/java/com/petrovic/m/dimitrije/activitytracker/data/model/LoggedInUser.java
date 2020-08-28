package com.petrovic.m.dimitrije.activitytracker.data.model;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.petrovic.m.dimitrije.activitytracker.data.pojo.Token;
import com.petrovic.m.dimitrije.activitytracker.data.pojo.User;

public class LoggedInUser {

    private Token token = null;
    private User user = null;
    private GoogleSignInAccount googleAccount = null;

    public LoggedInUser() { }

    public LoggedInUser(Token token, User user, GoogleSignInAccount googleAccount) {
        this.token = token;
        this.user = user;
        this.googleAccount = googleAccount;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GoogleSignInAccount getGoogleAccount() {
        return googleAccount;
    }

    public void setGoogleAccount(GoogleSignInAccount googleAccount) {
        this.googleAccount = googleAccount;
    }

    public String getDisplayName() {
        return !user.getName().isEmpty() ? user.getName() : user.getEmail();
    }
}