package com.devlogex.yue.android.controllers;


import android.app.Activity;

import com.devlogex.yue.android.controllers.impl.RecognitionListenerImpl;
import com.devlogex.yue.android.exceptions.PermissionRequireException;
import com.devlogex.yue.android.repositories.FirestoreRepository;

public class CallManagement {

    private Activity activity;
    private static CallManagement instance = null;
    private CallManagement(Activity activity) {
        this.activity = activity;
        FirestoreRepository.getInstance();
    }

    public static CallManagement getInstance(Activity activity) {
        if(instance == null) {
            instance = new CallManagement(activity);
        }
        return instance;
    }

    public static void releaseInstance() {
        if (instance != null) {
            instance.destroy();
        }
    }

    public void startCall() {
        try {
            Media.getInstance().muteBeepSound(activity);
            TTS.getInstance(activity);
            WebRTC.getInstance(activity).createConnection();

        } catch (PermissionRequireException e) {
            // TODO: handle lacking permission

        } catch (Exception e) {
            // TODO: handle create connection failed

        }
    }

    public void endCall() {
        WebRTC.releaseInstance();
        RecognitionListenerImpl.releaseInstance();
        SpeechRecognition.releaseInstance();
        FirestoreRepository.releaseInstance();
        TTS.releaseInstance();
        Media.getInstance().unmuteBeepSound(activity);
    }

    public void destroy() {
        if (instance != null) {
            endCall();
            instance = null;
        }
    }
}
