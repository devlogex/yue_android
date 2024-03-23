package com.devlogex.yue.android.controllers;


import android.app.Activity;

import com.devlogex.yue.android.controllers.impl.RecognitionListenerImpl;
import com.devlogex.yue.android.exceptions.PermissionRequireException;

public class CallManagement {

    private Activity activity;
    private static CallManagement instance = null;
    private CallManagement(Activity activity) {
        this.activity = activity;
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
            WebRTC.getInstance(activity).createConnection();


        } catch (PermissionRequireException e) {
            // TODO: handle lacking permission

        } catch (Exception e) {
            // TODO: handle create connection failed

        }
    }

    public void endCall() {
        WebRTC.releaseInstance();
        SpeechRecognition.releaseInstance();
        RecognitionListenerImpl.releaseInstance();
    }

    public void destroy() {
        if (instance != null) {
            endCall();
            instance = null;
        }
    }
}
