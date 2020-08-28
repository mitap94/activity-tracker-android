package com.petrovic.m.dimitrije.activitytracker.ui.meals;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MealsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MealsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is meals fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}