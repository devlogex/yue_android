package com.devlogex.yue.android;


import static com.devlogex.yue.android.controllers.ShareStorage.getToken;
import static com.devlogex.yue.android.controllers.ShareStorage.getUserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.devlogex.yue.android.controllers.Authenticate;
import com.devlogex.yue.android.controllers.SpeechRecognition;
import com.devlogex.yue.android.controllers.TTS;
import com.devlogex.yue.android.controllers.WebRTC;
import com.devlogex.yue.android.controllers.impl.GoogleSSO;
import com.devlogex.yue.android.controllers.impl.WebRTCImpl;
import com.devlogex.yue.android.serializers.UserSerializer;
import com.devlogex.yue.android.ui.SharedViewModel;
import com.devlogex.yue.android.ui.call.CallViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.devlogex.yue.android.databinding.ActivityMainBinding;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    NavController navController;

    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    }


    public void speak(View view) throws JSONException {
//        tts.speak("Hello, how are you?");

//        speechRecognition.startListening();

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("msg_type", "statement");
//        jsonObject.put("content", "Hello, how are you?");
//        webRTC.send(jsonObject.toString());

//        authenticate = GoogleSSO.getInstance(this);
//        authenticate.login(this);
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
//        tts.destroy();
//        speechRecognition.destroy();

        super.onDestroy();
    }
}