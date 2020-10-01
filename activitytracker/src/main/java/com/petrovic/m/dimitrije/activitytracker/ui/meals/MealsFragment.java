package com.petrovic.m.dimitrije.activitytracker.ui.meals;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentActivitiesBinding;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentMealsBinding;
import com.petrovic.m.dimitrije.activitytracker.ui.me.ItemsFragment;
import com.petrovic.m.dimitrije.activitytracker.ui.me.ListViewAdapter;

public class MealsFragment extends Fragment {

    private OnAddFoodSelectedListener listener;

    private FragmentMealsBinding binding;

    private MealsViewModel mealsViewModel;
    private MealsListViewAdapter mealsListViewAdapter;

    // Container Activity must implement this interface
    public interface OnAddFoodSelectedListener {
        public void onAddFoodSelected();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnAddFoodSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnAddFoodSelectedListener");
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMealsBinding.inflate(inflater, container, false);
        View root =  binding.getRoot();

        mealsViewModel = new
                ViewModelProvider(this).get(MealsViewModel.class);

//        mealsViewModel.getText().observe(getViewLifecycleOwner(), s -> binding.textMeals.setText(s));

        SpannableString kcal = new SpannableString("1500 kcal");
        kcal.setSpan(new RelativeSizeSpan(2f), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.mealInfo.setText(kcal);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] dateIds = {"06/09/2020", "04/09/2020", "01/09/2020", "29/08/2020", "28/08/2020", "22/08/2020", "15/08/2020"};
        String[] kcalIds = {"1600 kcal", "1900 kcal", "1850 kcal", "2000 kcal", "1800 kcal", "1700 kcal", "1800 kcal" };

        mealsListViewAdapter = new MealsListViewAdapter(this.getContext(),  dateIds, kcalIds);
        binding.mealList.setAdapter(mealsListViewAdapter);
        binding.mealList.setOnItemClickListener((parent, view1, position, id) -> {
            // TODO replace fragment

            listener.onAddFoodSelected();

            Toast.makeText(MealsFragment.this.getContext(), "Item " + position + " clicked!", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}