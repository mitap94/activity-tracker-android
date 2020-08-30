package com.petrovic.m.dimitrije.activitytracker.ui.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.petrovic.m.dimitrije.activitytracker.MainViewModel;
import com.petrovic.m.dimitrije.activitytracker.MainViewModelFactory;
import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.data.model.LoggedInUser;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private MainViewModel viewModel;

    ArrayAdapter<CharSequence> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_values_array,R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.genderSpinner.setAdapter(adapter);
        binding.genderSpinner.setEnabled(false);

        viewModel = new ViewModelProvider(requireActivity(), new MainViewModelFactory(requireActivity().getApplication())).get(MainViewModel.class);

        binding.editProfile.setOnClickListener(v -> {
            binding.editProfile.setVisibility(View.GONE);
            binding.saveProfile.setVisibility(View.VISIBLE);
            binding.cancelEdit.setVisibility(View.VISIBLE);
            binding.name.setEnabled(true);
            binding.genderSpinner.setEnabled(true);
        });

        binding.saveProfile.setOnClickListener(v -> {
            binding.editProfile.setVisibility(View.VISIBLE);
            binding.saveProfile.setVisibility(View.GONE);
            binding.cancelEdit.setVisibility(View.GONE);
            binding.name.setEnabled(false);
            binding.genderSpinner.setEnabled(false);
        });

        binding.cancelEdit.setOnClickListener(v -> {
            binding.editProfile.setVisibility(View.VISIBLE);
            binding.saveProfile.setVisibility(View.GONE);
            binding.cancelEdit.setVisibility(View.GONE);
            binding.name.setEnabled(false);
            binding.genderSpinner.setEnabled(false);
        });

        viewModel.getSessionManagerMutableLiveData().observe(requireActivity(), sessionManager -> {
            if (sessionManager.getUser() != null) {
                loadProfileInfo(sessionManager.getUser());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadProfileInfo(LoggedInUser user) {
        String profilePictureUrl = user.getUser().getProfilePicture();

        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Glide.with(this).asBitmap().load(profilePictureUrl).centerCrop().into(binding.profilePicture);
        } else {
            binding.profilePicture.setImageResource(R.mipmap.ic_launcher_round);
        }

        // load name
        binding.name.setText(user.getUser().getName());

        // load email
        binding.email.setText(user.getUser().getEmail());

        // load gender
        binding.genderSpinner.setSelection(adapter.getPosition(user.getUser().getGender()));
    }
}