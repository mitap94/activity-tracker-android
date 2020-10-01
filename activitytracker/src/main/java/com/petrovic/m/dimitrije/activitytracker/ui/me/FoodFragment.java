package com.petrovic.m.dimitrije.activitytracker.ui.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentFoodBinding;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentItemsBinding;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

public class FoodFragment extends Fragment {

    private static final String LOG_TAG = Utils.getLogTag(FoodFragment.class);

    private FragmentFoodBinding binding;

    FoodListViewAdapter foodListViewAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFoodBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String[] textIds = {"Chicken", "Almond", "Fish"};
        int[] iconIds = {R.drawable.chicken, R.drawable.almond, R.drawable.fish};
        String[] size = {"100 gr", "100 gr", "100 gr"};
        String[] kcal = {"287 kcal", "600 kcal", "252 kcal"};

        foodListViewAdapter = new FoodListViewAdapter(this.getContext(), textIds, iconIds, size, kcal);
        binding.foodList.setAdapter(foodListViewAdapter);
        binding.foodList.setOnItemClickListener((parent, view1, position, id) -> {
            // TODO replace fragment



            
            Toast.makeText(FoodFragment.this.getContext(), "Item " + position + " clicked!", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}