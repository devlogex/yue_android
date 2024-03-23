package com.devlogex.yue.android.controllers.impl;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

import androidx.annotation.NonNull;

import com.devlogex.yue.android.controllers.SpeechRecognition;
import com.devlogex.yue.android.controllers.WebRTC;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecognitionListenerImpl implements RecognitionListener {
    private WebRTC webRTC;
    private SpeechRecognition speechRecognition;

    private ExecutorService executorService;
    private Runnable sendRawTask;

    public RecognitionListenerImpl(WebRTC webRTC, SpeechRecognition speechRecognition) {
        this.webRTC = webRTC;
        this.speechRecognition = speechRecognition;
        executorService = Executors.newSingleThreadExecutor();
        sendRawTask = new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        sendRaw();
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        };
    }

    private void sendMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg_type", "statement");
            jsonObject.put("content", message);
            webRTC.send(jsonObject.toString());
        } catch (Exception e) {
            // TODO: handle send msg failure
        }
    }

    private void sendRaw() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg_type", "raw");
            webRTC.send(jsonObject.toString());
        } catch (Exception e) {
            // TODO: handle send msg failure
        }
    }

    private void sendYueReplySignal() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg_type", "yue_reply");
            webRTC.send(jsonObject.toString());
        } catch (Exception e) {
            // TODO: handle send msg failure
        }
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        sendMessage(matches.get(0));
        System.out.println("onResults: " + matches.get(0));
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        System.out.println("TESTING listener onReadyForSpeech");

    }

    @Override
    public void onBeginningOfSpeech() {
        executorService.execute(sendRawTask);
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
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

    @Override
    public void onError(int error) {
        System.out.println("TESTING listener onError " + error + " " + String.valueOf(SpeechRecognizer.ERROR_NO_MATCH == error));
        speechRecognition.startListening();


    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        System.out.println("TESTING listener onPartialResults " + partialResults);

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
