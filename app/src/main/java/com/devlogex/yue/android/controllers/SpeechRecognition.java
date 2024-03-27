package com.devlogex.yue.android.controllers;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.devlogex.yue.android.utils.Permissions.requestAudioPermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import com.devlogex.yue.android.controllers.impl.RecognitionListenerImpl;

public class SpeechRecognition {

    public static final int RC_SPEECH_RECOGNITION = 216;

    private volatile boolean enableListening = true;

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private Activity activity;


    private static SpeechRecognition instance = null;

    public static SpeechRecognition getInstance(Activity activity) {
        if(instance == null) {
            instance = new SpeechRecognition(activity);
        }
        return instance;
    }

    public boolean isEnableListening() {
        return enableListening;
    }

    public void setEnableListening(boolean enableListening) {
        this.enableListening = enableListening;
    }

    private SpeechRecognition(Activity activity) {
        this.activity = activity;
        requestAudioPermission(activity);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        speechRecognizer.setRecognitionListener(RecognitionListenerImpl.getInstance(this, activity));

    }

    public static void releaseInstance() {
        if (instance != null) {
            instance.destroy();
        }
    }

    public void startListening() {
        if (enableListening) {
            System.out.println("TESTING listener start status " + enableListening);
            speechRecognizer.startListening(recognizerIntent);
        }

    }

    public void stopListening() {
        speechRecognizer.stopListening();
    }

    public void destroy() {
        if (instance != null) {
            stopListening();
            instance = null;
            speechRecognizer.destroy();
        }
    }

}
