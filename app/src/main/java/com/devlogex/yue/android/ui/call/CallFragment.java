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
import com.devlogex.yue.android.controllers.WebRTC;
import com.devlogex.yue.android.controllers.impl.WebRTCImpl;
import com.devlogex.yue.android.databinding.FragmentCallBinding;
import com.devlogex.yue.android.exceptions.PermissionRequireException;
import com.devlogex.yue.android.ui.SharedViewModel;


public class CallFragment extends Fragment {

    private FragmentCallBinding binding;

    private WebRTC webRTC;


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

        startCall(sharedViewModel);
        return root;
    }

    private void startCall(SharedViewModel sharedViewModel) {
        webRTC = WebRTCImpl.getInstance(getActivity());
        try {
            sharedViewModel.setIsCalling(true);
            webRTC.createConnection();


        } catch (PermissionRequireException e) {
            // TODO: handle lacking permission

            sharedViewModel.setIsCalling(false);
        } catch (Exception e) {
            // TODO: handle create connection failed

            sharedViewModel.setIsCalling(false);
        }

    }


    @Override
    public void onDestroyView() {
        if (webRTC != null) {
            webRTC.closeConnection();
        }
        super.onDestroyView();
        binding = null;
    }
}