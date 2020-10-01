package com.petrovic.m.dimitrije.activitytracker.ui.home;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
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
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentHomeBinding;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentItemsBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel = new
                ViewModelProvider(this).get(HomeViewModel.class);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> binding.textHome.setText(s));

        SpannableString steps = new SpannableString("576 steps   47 kcal");
        steps.setSpan(new RelativeSizeSpan(2f), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        steps.setSpan(new RelativeSizeSpan(2f), 12, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.stepsInfo.setText(steps);

        SpannableString kcal = new SpannableString("1800 kcal");
        kcal.setSpan(new RelativeSizeSpan(2f), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.mealInfo.setText(kcal);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}