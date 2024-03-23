package com.devlogex.yue.android.ui.call;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.devlogex.yue.android.R;
import com.devlogex.yue.android.controllers.CallManagement;
import com.devlogex.yue.android.controllers.WebRTC;
import com.devlogex.yue.android.databinding.FragmentCallBinding;
import com.devlogex.yue.android.exceptions.PermissionRequireException;
import com.devlogex.yue.android.ui.SharedViewModel;


public class CallFragment extends Fragment {

    private FragmentCallBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CallViewModel callViewModel = new ViewModelProvider(this).get(CallViewModel.class);
        SharedViewModel sharedViewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        binding = FragmentCallBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // check call conditions
        if (!sharedViewModel.getIsLogin().getValue()) {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.navigation_home);
        }

        CallManagement.getInstance(getActivity()).startCall();
        return root;
    }

    @Override
    public void onDestroyView() {
        CallManagement.getInstance(getActivity()).endCall();
        super.onDestroyView();
        binding = null;
    }
}