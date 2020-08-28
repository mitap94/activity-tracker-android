package com.petrovic.m.dimitrije.activitytracker.ui.login;

import androidx.annotation.Nullable;

import com.petrovic.m.dimitrije.activitytracker.data.model.LoggedInUser;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUser success;
    @Nullable
    private Throwable error;

    LoginResult(@Nullable Throwable error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUser success) {
        this.success = success;
    }

    @Nullable
    LoggedInUser getSuccess() {
        return success;
    }

    @Nullable
    Throwable getError() {
        return error;
    }
}