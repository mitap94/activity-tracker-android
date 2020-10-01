package com.petrovic.m.dimitrije.activitytracker.ui.me;

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

import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentInfoBinding;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentItemsBinding;
import com.petrovic.m.dimitrije.activitytracker.ui.meals.MealsFragment;
import com.petrovic.m.dimitrije.activitytracker.ui.meals.MealsListViewAdapter;

public class InfoFragment extends Fragment {

    private FragmentInfoBinding binding;
    private InfoListViewAdapter infolistAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        binding.currentValue.setText("105.3 kg");
        binding.targetValue.setText("95 kg");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] dateIds = {"06/09/2020", "04/09/2020", "01/09/2020", "29/08/2020", "28/08/2020", "22/08/2020", "15/08/2020"};
        String[] weightIds = {"105.3 kg", "106.7 kg", "107.3 kg", "108 kg", "108.5 kg", "109 kg", "112 kg" };

        infolistAdapter = new InfoListViewAdapter(this.getContext(),  dateIds, weightIds);
        binding.infoList.setAdapter(infolistAdapter);
        binding.infoList.setOnItemClickListener((parent, view1, position, id) -> {
            // TODO replace fragment

            Toast.makeText(InfoFragment.this.getContext(), "Item " + position + " clicked!", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}