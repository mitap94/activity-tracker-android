package com.petrovic.m.dimitrije.activitytracker.ui.meals;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentDailyMealsBinding;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentMealsBinding;

public class DailyMealsFragment extends Fragment {

    private FragmentDailyMealsBinding binding;

    private MealsViewModel mealsViewModel;
    private MealsListViewAdapter mealsListViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDailyMealsBinding.inflate(inflater, container, false);
        View root =  binding.getRoot();

        mealsViewModel = new
                ViewModelProvider(this).get(MealsViewModel.class);

//        mealsViewModel.getText().observe(getViewLifecycleOwner(), s -> binding.textMeals.setText(s));

//        SpannableString kcal = new SpannableString("1500 kcal");
//        kcal.setSpan(new RelativeSizeSpan(2f), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        binding.mealInfo.setText(kcal);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] b_dataIds = {"Lemonade", "Oats", "Peaches"};
        String[] b_kcalIds = {"40 kcal", "250 kcal", "110 kcal"};

        mealsListViewAdapter = new MealsListViewAdapter(this.getContext(),  b_dataIds, b_kcalIds);
        binding.breakfastList.setAdapter(mealsListViewAdapter);
        binding.breakfastList.setOnItemClickListener((parent, view1, position, id) -> {
            // TODO replace fragment

            Toast.makeText(DailyMealsFragment.this.getContext(), "Item " + position + " clicked!", Toast.LENGTH_LONG).show();
        });

        String[] l_dataIds = {"Chicken wings", "Bread", "Tomato"};
        String[] l_kcalIds = {"540 kcal", "130 kcal", "30 kcal"};

        mealsListViewAdapter = new MealsListViewAdapter(this.getContext(),  l_dataIds, l_kcalIds);
        binding.launchList.setAdapter(mealsListViewAdapter);
        binding.launchList.setOnItemClickListener((parent, view1, position, id) -> {
            // TODO replace fragment

            Toast.makeText(DailyMealsFragment.this.getContext(), "Item " + position + " clicked!", Toast.LENGTH_LONG).show();
        });

        String[] d_dataIds = {"White fish", "Rice"};
        String[] d_kcalIds = {"200 kcal", "260 kcal"};

        mealsListViewAdapter = new MealsListViewAdapter(this.getContext(),  d_dataIds, d_kcalIds);
        binding.dinnerList.setAdapter(mealsListViewAdapter);
        binding.dinnerList.setOnItemClickListener((parent, view1, position, id) -> {
            // TODO replace fragment

            Toast.makeText(DailyMealsFragment.this.getContext(), "Item " + position + " clicked!", Toast.LENGTH_LONG).show();
        });

        String[] s_dataIds = {"Apple"};
        String[] s_kcalIds = {"40 kcal"};

        mealsListViewAdapter = new MealsListViewAdapter(this.getContext(),  s_dataIds, s_kcalIds);
        binding.snackList.setAdapter(mealsListViewAdapter);
        binding.snackList.setOnItemClickListener((parent, view1, position, id) -> {
            // TODO replace fragment

            Toast.makeText(DailyMealsFragment.this.getContext(), "Item " + position + " clicked!", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}