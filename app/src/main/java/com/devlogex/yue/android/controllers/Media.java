package com.devlogex.yue.android.controllers;


import static com.devlogex.yue.android.utils.Permissions.hasAudioPermission;
import static com.devlogex.yue.android.utils.Permissions.requestAudioPermission;

import android.app.Activity;

import com.devlogex.yue.android.exceptions.PermissionRequireException;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnectionFactory;

public class Media {
    private static Media instance = null;
    private Media() {
    }

    public static Media getInstance() {
        if(instance == null) {
            instance = new Media();
        }
        return instance;
    }
    public AudioTrack getAudio(Activity activity, PeerConnectionFactory peerConnectionFactory) throws PermissionRequireException {
        requestAudioPermission(activity);
        if (!hasAudioPermission(activity)) {
            throw new PermissionRequireException("Audio permission required");
        }
        AudioSource audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
        AudioTrack audioTrack = peerConnectionFactory.createAudioTrack("audioTrack", audioSource);
        return audioTrack;
    }

}
