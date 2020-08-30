package com.petrovic.m.dimitrije.activitytracker.ui.me;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayoutMediator;
import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentMeBinding;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

public class MeFragment extends Fragment {

    private static final String LOG_TAG = Utils.getLogTag(MeFragment.class);

    public static final String INIT_FRAGMENT = "com.petrovic.m.dimitrije.activitytracker.INIT_FRAGMENT";
    public enum FRAGMENT_POSITION {
        INFO_FRAGMENT,
        ITEMS_FRAGMENT,
        PROFILE_FRAGMENT
    }

    private FragmentMeBinding binding;

    private MeViewModel meViewModel;

    private TabsAdapter tabsAdapter;
    private String[] tabNameArray;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");

        binding = FragmentMeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onViewCreated");

        meViewModel = new
                ViewModelProvider(this).get(MeViewModel.class);

        tabNameArray = getResources().getStringArray(R.array.me_tabs_array);
        tabsAdapter = new TabsAdapter(this);
        binding.viewPager.setAdapter(tabsAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(tabNameArray[position])
        ).attach();

        // Open tab of specific fragment in case requested
        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt(INIT_FRAGMENT, 0);
            Log.d(LOG_TAG, "set current viewpager tab: " + tabNameArray[position] + ", position: " + position);
            binding.viewPager.postDelayed(() -> binding.viewPager.setCurrentItem(position), 50);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}