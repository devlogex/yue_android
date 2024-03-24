package com.devlogex.yue.android.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.devlogex.yue.android.R;
import com.devlogex.yue.android.controllers.ShareStorage;
import com.devlogex.yue.android.controllers.GoogleSSO;
import com.devlogex.yue.android.databinding.FragmentHomeBinding;
import com.devlogex.yue.android.serializers.UserSerializer;
import com.devlogex.yue.android.ui.SharedViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        SharedViewModel sharedViewModel =
                new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        sharedViewModel.getIsLogin().observe(getViewLifecycleOwner(), isLogin -> {
            if (isLogin) {
                UserSerializer userInfo = ShareStorage.getUserInfo(getActivity());
                String token = ShareStorage.getToken(getActivity());
                TextView txtName = getActivity().findViewById(R.id.txt_name);
                TextView txtEmail = getActivity().findViewById(R.id.txt_email);
                TextView txtToken = getActivity().findViewById(R.id.txt_token);
                txtName.setText(userInfo.getFirstName() + " " + userInfo.getLastName());
                txtEmail.setText(userInfo.getEmail());
                txtToken.setText(token);
            }
        });

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button loginButton = root.findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSSO.getInstance(getActivity()).login();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}