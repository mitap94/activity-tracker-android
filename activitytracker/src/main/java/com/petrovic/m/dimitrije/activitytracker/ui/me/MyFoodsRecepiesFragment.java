package com.petrovic.m.dimitrije.activitytracker.ui.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentInfoBinding;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentMyfoodsrecepiesBinding;

public class MyFoodsRecepiesFragment extends Fragment {

    private FragmentMyfoodsrecepiesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMyfoodsrecepiesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}