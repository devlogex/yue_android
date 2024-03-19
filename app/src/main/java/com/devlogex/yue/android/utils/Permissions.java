package com.devlogex.yue.android.utils;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.CAMERA;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.app.Activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {

    public static void requestAudioPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{RECORD_AUDIO}, 1);
        }
    }

    public static void requestCameraPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, CAMERA) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{CAMERA}, 1);
        }
    }

    public static boolean hasAudioPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, RECORD_AUDIO) == PERMISSION_GRANTED;
    }

    public static boolean hasCameraPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, CAMERA) == PERMISSION_GRANTED;
    }
}
