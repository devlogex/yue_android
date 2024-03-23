package com.devlogex.yue.android.controllers;

import static com.devlogex.yue.android.utils.Permissions.requestAudioPermission;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import com.devlogex.yue.android.controllers.impl.RecognitionListenerImpl;

public class SpeechRecognition {

    public static final int RC_SPEECH_RECOGNITION = 216;

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;


    private static SpeechRecognition instance = null;

    public static SpeechRecognition getInstance(Activity activity) {
        if(instance == null) {
            instance = new SpeechRecognition(activity);
        }
        return instance;
    }

    private SpeechRecognition(Activity activity) {
        requestAudioPermission(activity);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        speechRecognizer.setRecognitionListener(RecognitionListenerImpl.getInstance(this));

    }

    public static void releaseInstance() {
        if (instance != null) {
            instance.destroy();
        }
    }

    public void startListening() {
        speechRecognizer.startListening(recognizerIntent);
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
