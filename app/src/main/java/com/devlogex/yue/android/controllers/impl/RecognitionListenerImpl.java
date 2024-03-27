package com.devlogex.yue.android.controllers.impl;

import android.app.Activity;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

import androidx.annotation.NonNull;

import com.devlogex.yue.android.controllers.SpeechRecognition;
import com.devlogex.yue.android.controllers.WebRTC;

import org.json.JSONObject;

import java.util.ArrayList;

public class RecognitionListenerImpl implements RecognitionListener {
    private SpeechRecognition speechRecognition;
    private Activity activity;

    private long lastMsgTimestamp = System.currentTimeMillis();


    private static RecognitionListenerImpl instance = null;

    public static RecognitionListenerImpl getInstance(SpeechRecognition speechRecognition, Activity activity) {
        if (instance == null) {
            instance = new RecognitionListenerImpl(speechRecognition, activity);
        }
        return instance;
    }

    private RecognitionListenerImpl(SpeechRecognition speechRecognition, Activity activity) {
        this.speechRecognition = speechRecognition;
        this.activity = activity;
    }

    public static void releaseInstance() {
        if (instance != null) {
            instance = null;
        }

    }

    private void sendMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg_type", "statement");
            jsonObject.put("content", message);
            WebRTC.getInstance(activity).send(jsonObject.toString());
        } catch (Exception e) {
            // TODO: handle send msg failure
        }
    }

    private void sendRaw() {
        try {
            if (System.currentTimeMillis() - lastMsgTimestamp >= 500) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("msg_type", "raw");
                WebRTC.getInstance(activity).send(jsonObject.toString());
            }
        } catch (Exception e) {
            // TODO: handle send msg failure
        }
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null && !matches.isEmpty()) {
            sendMessage(matches.get(0));
            System.out.println("TESTING listener onResults: " + matches.get(0));
        } else {
            System.out.println("TESTING listener onResults: No speech input recognized.");
        }
        speechRecognition.startListening();
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        System.out.println("TESTING listener onReadyForSpeech");

    }

    @Override
    public void onBeginningOfSpeech() {
//        executorService.execute(sendRawTask);
        System.out.println("TESTING listener onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
//        if (executorService != null && !executorService.isShutdown()) {
//            executorService.shutdownNow();
//        }
    }

    @Override
    public void onError(int error) {
        System.out.println("TESTING listener onError " + error + " " + String.valueOf(SpeechRecognizer.ERROR_NO_MATCH == error));
        speechRecognition.startListening();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        System.out.println("TESTING listener onPartialResults " + partialResults);
        sendRaw();

    }

    @Override
    public void onSegmentResults(@NonNull Bundle segmentResults) {
        RecognitionListener.super.onSegmentResults(segmentResults);
        System.out.println("TESTING listener onSegmentResults " + segmentResults);
    }

    @Override
    public void onEndOfSegmentedSession() {
        RecognitionListener.super.onEndOfSegmentedSession();
        System.out.println("TESTING listener onEndOfSegmentedSession ");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }
}
