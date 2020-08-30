package com.petrovic.m.dimitrije.activitytracker.ui.me;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

public class TabsAdapter extends FragmentStateAdapter {

    private static final String LOG_TAG = Utils.getLogTag(TabsAdapter.class);

    public TabsAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d(LOG_TAG, "createFragment position " + position);

        Fragment fragment = new InfoFragment();
        switch(position) {
            case 1:
                fragment = new ItemsFragment();
                break;
            case 2:
                fragment = new ProfileFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}