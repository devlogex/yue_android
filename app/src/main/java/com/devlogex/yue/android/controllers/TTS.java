package com.devlogex.yue.android.controllers;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;
import java.util.UUID;

public class TTS {

    private TextToSpeech tts;

    private static TTS instance = null;
    public static TTS getInstance(Activity activity) {
        if(instance == null) {
            instance = new TTS(activity);
        }
        return instance;
    }
    public static void releaseInstance() {
        if (instance != null) {
            instance.destroy();
            instance = null;
        }
    }

    private TTS(Activity activity) {
        tts = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.d("TTS", "onStart: " + utteranceId);
                activity.runOnUiThread(() -> {
                    SpeechRecognition.getInstance(activity).stopListening();
                    SpeechRecognition.getInstance(activity).setEnableListening(false);

                    // Wait for a short period to ensure SpeechRecognizer has stopped
                    new Handler().postDelayed(() -> {
                        SpeechRecognition.getInstance(activity).setEnableListening(false);
                    }, 500);

                });
            }

            @Override
            public void onDone(String utteranceId) {
                Log.d("TTS", "onDone: " + utteranceId);
                SpeechRecognition.getInstance(activity).setEnableListening(true);
                activity.runOnUiThread(() -> SpeechRecognition.getInstance(activity).startListening());
            }

            @Override
            public void onError(String utteranceId) {
                Log.d("TTS", "onError: " + utteranceId);
            }
        });
    }

    public void speak(String text) {
        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, UUID.randomUUID().toString());
    }

    public void destroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
