package com.petrovic.m.dimitrije.activitytracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.petrovic.m.dimitrije.activitytracker.ui.login.LoginViewModel;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class MainViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Application application;

    public MainViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(application);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}