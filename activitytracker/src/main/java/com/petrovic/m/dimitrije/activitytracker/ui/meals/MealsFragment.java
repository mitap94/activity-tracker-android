package com.petrovic.m.dimitrije.activitytracker.ui.meals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.petrovic.m.dimitrije.activitytracker.R;

public class MealsFragment extends Fragment {

    private MealsViewModel mealsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mealsViewModel = new
                ViewModelProvider(this).get(MealsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_meals, container, false);
        final TextView textView = root.findViewById(R.id.text_meals);
        mealsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}