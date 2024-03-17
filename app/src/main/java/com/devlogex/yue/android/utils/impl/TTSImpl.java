package com.devlogex.yue.android.utils.impl;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.devlogex.yue.android.utils.TTS;

import java.util.Locale;

public class TTSImpl implements TTS {
    private static TextToSpeech tts;
    private static Activity activity;

    public TTSImpl(Activity activity) {
        this.activity = activity;
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
    }

    @Override
    public void speak(String text) {
        Bundle params = new Bundle();
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, null);
    }

    @Override
    public void destroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
