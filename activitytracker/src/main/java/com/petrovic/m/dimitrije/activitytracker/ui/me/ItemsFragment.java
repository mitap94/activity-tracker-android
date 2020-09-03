package com.petrovic.m.dimitrije.activitytracker.ui.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentItemsBinding;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentMeBinding;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

public class ItemsFragment extends Fragment {

    private static final String LOG_TAG = Utils.getLogTag(ItemsFragment.class);

    private FragmentItemsBinding binding;

    ListViewAdapter listViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentItemsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        int[] textIds = {R.string.my_recipes, R.string.my_foods};
        int[] iconIds = {R.drawable.baseline_menu_book_24, R.drawable.baseline_fastfood_24};

        listViewAdapter = new ListViewAdapter(this.getContext(),  textIds, iconIds);
        binding.itemList.setAdapter(listViewAdapter);
        binding.itemList.setOnItemClickListener((parent, view1, position, id) -> {
            // TODO replace fragment
            
            Toast.makeText(ItemsFragment.this.getContext(), "Item " + position + " clicked!", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}