package com.devlogex.yue.android.controllers;


import static android.content.Context.AUDIO_SERVICE;
import static com.devlogex.yue.android.utils.Permissions.hasAudioPermission;
import static com.devlogex.yue.android.utils.Permissions.requestAudioPermission;

import android.app.Activity;
import android.media.AudioManager;

import com.devlogex.yue.android.exceptions.PermissionRequireException;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnectionFactory;

public class Media {
    private int originalVolume = 0;
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

    public void muteBeepSound(Activity activity) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(AUDIO_SERVICE);
        originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);

        audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public void unmuteBeepSound(Activity activity) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, originalVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

}
