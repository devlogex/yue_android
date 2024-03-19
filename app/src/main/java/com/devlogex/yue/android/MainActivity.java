package com.devlogex.yue.android;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.devlogex.yue.android.exceptions.PermissionRequireException;
import com.devlogex.yue.android.utils.SpeechRecognition;
import com.devlogex.yue.android.utils.TTS;
import com.devlogex.yue.android.utils.WebRTC;
import com.devlogex.yue.android.utils.impl.SpeechRecognitionImpl;
import com.devlogex.yue.android.utils.impl.TTSImpl;
import com.devlogex.yue.android.utils.impl.WebRTCImpl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.devlogex.yue.android.databinding.ActivityMainBinding;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturer;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private SpeechRecognition speechRecognition;
    private TTS tts;

    private WebRTC webRTC = WebRTCImpl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_call, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        tts = new TTSImpl(this);


        speechRecognition = new SpeechRecognitionImpl(this);

        TextView textView = findViewById(R.id.text_home);

        try {
            webRTC.createConnection(this);
        } catch (PermissionRequireException e) {
            textView.setText(e.getMessage());
            e.printStackTrace();
        }
    }


    public void speak(View view) throws JSONException {
//        tts.speak("Hello, how are you?");

//        speechRecognition.startListening();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg_type", "statement");
        jsonObject.put("content", "Hello, how are you?");
        webRTC.send(jsonObject.toString());

    }


    @Override
    public void onDestroy() {
        tts.destroy();
        speechRecognition.destroy();
        webRTC.closeConnection();

        super.onDestroy();
    }
}