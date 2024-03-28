package com.devlogex.yue.android;


import static com.devlogex.yue.android.utils.Permissions.hasAudioPermission;
import static com.devlogex.yue.android.utils.Permissions.requestAudioPermission;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.devlogex.yue.android.configs.GlobalExceptionHandler;
import com.devlogex.yue.android.controllers.CallManagement;
import com.devlogex.yue.android.controllers.GoogleSSO;
import com.devlogex.yue.android.ui.SharedViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.devlogex.yue.android.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_call, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // request media permissions
        if (!hasAudioPermission(this)) {
            requestAudioPermission(this);
        }

    }

    public void onClickTest(View view) {
//        SpeechRecognition.getInstance(this).startListening();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GoogleSSO.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSSO.getInstance(this).onGoogleSignInResult(task, sharedViewModel);
        }
    }



    @Override
    public void onDestroy() {
        CallManagement.releaseInstance();
        super.onDestroy();
    }
}