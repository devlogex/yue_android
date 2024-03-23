package com.devlogex.yue.android.controllers.impl;

import static android.Manifest.permission.RECORD_AUDIO;

import static com.devlogex.yue.android.utils.Permissions.requestAudioPermission;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import androidx.core.app.ActivityCompat;

import com.devlogex.yue.android.controllers.SpeechRecognition;
import com.devlogex.yue.android.controllers.WebRTC;
import com.devlogex.yue.android.utils.Permissions;

import org.webrtc.DataChannel;


public class SpeechRecognitionImpl implements SpeechRecognition {
    public static final int RC_SPEECH_RECOGNITION = 216;

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;

    private RecognitionListenerImpl recognitionListener;

    private WebRTC webRTC;

    private Activity activity;

    private static SpeechRecognitionImpl instance = null;

    public static SpeechRecognitionImpl getInstance(Activity activity, WebRTC webRTC) {
        if(instance == null) {
            instance = new SpeechRecognitionImpl(activity, webRTC);
        }
        return instance;
    }

    private SpeechRecognitionImpl(Activity activity, WebRTC webRTC) {
        this.activity = activity;
        this.webRTC = webRTC;
        requestAudioPermission(activity);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);

        recognitionListener = new RecognitionListenerImpl(webRTC, this);
        speechRecognizer.setRecognitionListener(recognitionListener);

        }

    @Override
    public void startListening() {
        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    public void stopListening() {
        speechRecognizer.stopListening();
    }

    @Override
    public void destroy() {
        if (instance != null) {
            stopListening();
            instance = null;
            speechRecognizer.destroy();
        }
    }
}
