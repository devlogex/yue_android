package com.devlogex.yue.android.utils.impl;

import static android.Manifest.permission.RECORD_AUDIO;

import static com.devlogex.yue.android.utils.Permissions.requestAudioPermission;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import androidx.core.app.ActivityCompat;

import com.devlogex.yue.android.utils.SpeechRecognition;


public class SpeechRecognitionImpl implements SpeechRecognition {

    private static SpeechRecognizer speechRecognizer;
    private static Intent recognizerIntent;

    private static RecognitionListenerImpl recognitionListener;

    private static Activity activity;


    public SpeechRecognitionImpl(Activity activity) {
        this.activity = activity;
        requestAudioPermission(activity);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");


        recognitionListener = new RecognitionListenerImpl();
        speechRecognizer.setRecognitionListener(recognitionListener);

        }

    @Override
    public void startListening() {
        ActivityCompat.requestPermissions(activity, new String[]{RECORD_AUDIO}, 1);
        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    public void stopListening() {
        speechRecognizer.stopListening();
    }

    @Override
    public void destroy() {
        speechRecognizer.destroy();
    }
}
