package com.petrovic.m.dimitrije.activitytracker.data;

import com.petrovic.m.dimitrije.activitytracker.data.model.LoggedInUser;
import com.petrovic.m.dimitrije.activitytracker.data.pojo.Token;
import com.petrovic.m.dimitrije.activitytracker.data.pojo.User;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            Token fakeToken = new Token("123456");
            User fakeUser = new User("email@email.com", "name", "other");
            LoggedInUser fakeLoggedInUser =
                    new LoggedInUser(fakeToken, fakeUser, null);
            return new Result.Success<>(fakeLoggedInUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}