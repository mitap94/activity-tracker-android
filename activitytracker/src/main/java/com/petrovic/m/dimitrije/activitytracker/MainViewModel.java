package com.petrovic.m.dimitrije.activitytracker;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.petrovic.m.dimitrije.activitytracker.data.model.LoggedInUser;
import com.petrovic.m.dimitrije.activitytracker.rest.APIService;
import com.petrovic.m.dimitrije.activitytracker.rest.APIUtils;
import com.petrovic.m.dimitrije.activitytracker.rest.SessionManager;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

public class MainViewModel extends AndroidViewModel {

    private static final String LOG_TAG = Utils.getLogTag(MainViewModel.class);

    private MutableLiveData<SessionManager> sessionManagerMutableLiveData = new MutableLiveData<>();
    private APIService apiService;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.apiService = APIUtils.getApiService(application.getApplicationContext());
        this.sessionManagerMutableLiveData.postValue(SessionManager.getInstance(application.getApplicationContext()));
    }

    public MutableLiveData<SessionManager> getSessionManagerMutableLiveData() {
        Log.d(LOG_TAG, "getSessionManager");
        return sessionManagerMutableLiveData;
    }

    public void logout() {
        Log.d(LOG_TAG, "logout");
        SessionManager sessionManager = sessionManagerMutableLiveData.getValue();
        LoggedInUser user = sessionManager.getUser();

        // Remove auth token
        sessionManager.removeAuthToken();

        sessionManager.setUser(null);

        sessionManagerMutableLiveData.postValue(sessionManager);
    }
}
