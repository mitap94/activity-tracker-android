package com.petrovic.m.dimitrije.activitytracker.ui.me;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentMeBinding;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

public class MeFragment extends Fragment {

    private static final String LOG_TAG = Utils.getLogTag(MeFragment.class);

    public static final String INIT_FRAGMENT = "com.petrovic.m.dimitrije.activitytracker.INIT_FRAGMENT";

    private FragmentMeBinding binding;

    private MeViewModel meViewModel;

    private TabsAdapter tabsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreateView");

        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        meViewModel = new
                ViewModelProvider(this).get(MeViewModel.class);

        meViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                return;
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabsAdapter = new TabsAdapter(this);
        binding.viewPager.setAdapter(tabsAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    switch(position) {
                        case 0:
                            tab.setText("Info");
                            break;
                        case 1:
                            tab.setText("Items");
                            break;
                        case 2:
                            tab.setText("Profile");
                            break;
                    }
                }
        ).attach();

        // Open tab of specific fragment in case requested
        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d(LOG_TAG, "bundle != null");
            int position = bundle.getInt(INIT_FRAGMENT, 0);
            binding.viewPager.setCurrentItem(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}