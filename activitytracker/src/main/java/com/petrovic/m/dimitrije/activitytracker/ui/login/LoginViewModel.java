package com.petrovic.m.dimitrije.activitytracker.ui.login;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.petrovic.m.dimitrije.activitytracker.MainActivity;
import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.data.model.LoggedInUser;
import com.petrovic.m.dimitrije.activitytracker.data.pojo.Token;
import com.petrovic.m.dimitrije.activitytracker.data.pojo.User;
import com.petrovic.m.dimitrije.activitytracker.rest.APIService;
import com.petrovic.m.dimitrije.activitytracker.rest.APIUtils;
import com.petrovic.m.dimitrije.activitytracker.rest.SessionManager;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginViewModel extends AndroidViewModel {

    public static final String LOG_TAG = Utils.getLogTag(LoginViewModel.class);

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private APIService apiService;
    private SessionManager sessionManager;

    LoginViewModel(Application application) {
        super(application);
        this.apiService = APIUtils.getApiService(application.getApplicationContext());
        this.sessionManager = SessionManager.getInstance(application.getApplicationContext());
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        Log.d(LOG_TAG, "Login logic");

        LoggedInUser loggedInUser = new LoggedInUser();

        // Get token
        apiService.token(email, password)
                .doOnSuccess(token -> {
                    sessionManager.saveAuthToken(token.toString());
                })
                .doOnError(e -> {
                    Log.d(LOG_TAG, ((HttpException) e).response().errorBody().string());
                    loginResult.postValue(new LoginResult(e));
                })
                .flatMap(token -> apiService.getUserInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOG_TAG, "onSubscribe");
                    }

                    @Override
                    public void onSuccess(User user) {
                        Log.d(LOG_TAG, "onSuccess user = " + user);
                        loggedInUser.setUser(user);
                        sessionManager.setUser(loggedInUser);
                        loginResult.postValue(new LoginResult(loggedInUser));
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG, "error getting user info");
                        sessionManager.removeAuthToken();
                        sessionManager.setUser(null);
                        try {
                            Log.d(LOG_TAG, ((HttpException) e).response().errorBody().string());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        loginResult.postValue(new LoginResult(e));
                    }
                });
    }

    public void loginGoogle(GoogleSignInAccount googleAccount) {
        Log.d(LOG_TAG, "Login Google logic");

        LoggedInUser loggedInUser = new LoggedInUser();
        loggedInUser.setGoogleAccount(googleAccount);

        String authCode = googleAccount.getServerAuthCode();
        Log.d(LOG_TAG, "authCode = " + authCode);

        apiService.googleToken(authCode)
                .doOnSuccess(token -> {
                    Log.d(LOG_TAG, "success getting token from auth code " + token.toString());
                    sessionManager.saveAuthToken(token.toString());
                })
                .doOnError(e -> {
                    Log.e(LOG_TAG, "error getting token from auth code");
                    Log.e(LOG_TAG, ((HttpException) e).response().errorBody().string());
                    loginResult.postValue(new LoginResult(e));
                })
                .flatMap(token -> apiService.getUserInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOG_TAG, "onSubscribe");
                    }

                    @Override
                    public void onSuccess(User user) {
                        Log.d(LOG_TAG, "onSuccess user = " + user);
                        loggedInUser.setUser(user);
                        sessionManager.setUser(loggedInUser);
                        loginResult.postValue(new LoginResult(loggedInUser));
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG, "error getting user info");
                        sessionManager.removeAuthToken();
                        sessionManager.setUser(null);
                        try {
                            Log.d(LOG_TAG, ((HttpException) e).response().errorBody().string());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        loginResult.postValue(new LoginResult(e));
                    }
                });
    }

    public void silentLogin(GoogleSignInAccount googleAccount) {
        Log.d(LOG_TAG, "Silent login logic");

        LoggedInUser loggedInUser = new LoggedInUser();

        if (googleAccount != null) {
            loggedInUser.setGoogleAccount(googleAccount);
        }

        String token = sessionManager.fetchAuthToken();
        if (token != null && !token.isEmpty()) {
            apiService.getUserInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<User>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(LOG_TAG, "onSubscribe");
                        }

                        @Override
                        public void onSuccess(User user) {
                            Log.d(LOG_TAG, "onSuccess user = " + user);
                            loggedInUser.setUser(user);
                            sessionManager.setUser(loggedInUser);
                            loginResult.postValue(new LoginResult(loggedInUser));
                        }
                        @Override
                        public void onError(Throwable e) {
                            Log.d(LOG_TAG, "error getting user info");
                            sessionManager.setUser(null);
                            try {
                                Log.d(LOG_TAG, ((HttpException) e).response().errorBody().string());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            loginResult.postValue(new LoginResult());
                        }
                    });
        } else {
            Log.d(LOG_TAG, "No token stored, silent login not possible");
            loginResult.postValue(new LoginResult());
        }
    }

    // Not used
    public void silentLoginGoogle(GoogleSignInAccount googleAccount) {
        Log.d(LOG_TAG, "Silent login Google logic");

        LoggedInUser loggedInUser = new LoggedInUser();
        loggedInUser.setGoogleAccount(googleAccount);

        String authCode = googleAccount.getServerAuthCode();
        Log.d(LOG_TAG, "authCode = " + authCode);

        if (authCode == null) {
            silentLogin(googleAccount);
            return;
        }

        apiService.googleToken(authCode)
                .doOnSuccess(token -> {
                    sessionManager.saveAuthToken(token.toString());
                })
                .doOnError(e -> {
                    Log.d(LOG_TAG, ((HttpException) e).response().errorBody().string());
                    loginResult.postValue(new LoginResult());
                })
                .flatMap(token -> apiService.getUserInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOG_TAG, "onSubscribe");
                    }

                    @Override
                    public void onSuccess(User user) {
                        Log.d(LOG_TAG, "onSuccess user = " + user);
                        loggedInUser.setUser(user);
                        sessionManager.setUser(loggedInUser);
                        loginResult.postValue(new LoginResult(loggedInUser));
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOG_TAG, "error getting user info");
                        sessionManager.removeAuthToken();
                        sessionManager.setUser(null);
                        try {
                            Log.d(LOG_TAG, ((HttpException) e).response().errorBody().string());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        loginResult.postValue(new LoginResult());
                    }
                });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}