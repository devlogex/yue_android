package com.devlogex.yue.android.controllers.impl;

import static com.devlogex.yue.android.utils.Constants.API_CLOSE_CONNECTION_URL;
import static com.devlogex.yue.android.utils.Constants.API_OFFER_URL;
import static com.devlogex.yue.android.utils.Constants.ICE_SERVER;
import static com.devlogex.yue.android.utils.RestAPI.aPost;
import static com.devlogex.yue.android.utils.RestAPI.post;

import android.app.Activity;

import com.devlogex.yue.android.controllers.ShareStorage;
import com.devlogex.yue.android.controllers.SpeechRecognition;
import com.devlogex.yue.android.exceptions.PermissionRequireException;
import com.devlogex.yue.android.controllers.Media;
import com.devlogex.yue.android.controllers.WebRTC;

import org.json.JSONObject;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class WebRTCImpl implements WebRTC {
    private PeerConnection peerConnection;

    private PeerConnectionFactory peerConnectionFactory;

    private DataChannel dataChannel;

    private MediaStream mediaStream;

    private SpeechRecognition speechRecognition;

    private String connectionId;

    private Activity activity;
    private static WebRTCImpl instance = null;

    public static WebRTCImpl getInstance(Activity activity) {
        if (instance == null) {
            instance = new WebRTCImpl(activity);
        }
        return instance;
    }

    private WebRTCImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void createConnection() throws PermissionRequireException {
        PeerConnectionFactory.InitializationOptions initializationOptions =
                PeerConnectionFactory.InitializationOptions.builder(activity)
                        .createInitializationOptions();
        PeerConnectionFactory.initialize(initializationOptions);

        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        peerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(options)
                .createPeerConnectionFactory();

        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
        iceServers.add(PeerConnection.IceServer.builder(ICE_SERVER).createIceServer());

        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);

        createPeerConnection(peerConnectionFactory, rtcConfig);
        createDataChannel();
        addMediaTracks(peerConnectionFactory);
        signaling(peerConnection);

    }

    private void signaling(PeerConnection peerConnection) {
        peerConnection.createOffer(new SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                try {
                    peerConnection.setLocalDescription(this, sessionDescription);

                    JSONObject json = new JSONObject();
                    json.put("type", sessionDescription.type.canonicalForm());
                    json.put("sdp", sessionDescription.description);

                    Map<String, String> headers = Collections.singletonMap("Authorization", "Bearer " + ShareStorage.getToken(activity));
                    Response response = post(API_OFFER_URL, json.toString(), headers);
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONObject jsonSdpAnswer = jsonResponse.getJSONObject("s");
                        String sdp = jsonSdpAnswer.getString("sdp");
                        String type = jsonSdpAnswer.getString("type");
                        connectionId = jsonResponse.getString("c");
                        SessionDescription sdpAnswer = new SessionDescription(SessionDescription.Type.fromCanonicalForm(type), sdp);

                        peerConnection.setRemoteDescription(new SdpObserver() {
                            @Override
                            public void onSetSuccess() {
                                // Remote description set successfully
                            }

                            @Override
                            public void onCreateSuccess(SessionDescription sessionDescription) {
                                // Not used here
                            }

                            @Override
                            public void onSetFailure(String s) {
                                // TODO: handle create connection failed
                            }

                            @Override
                            public void onCreateFailure(String s) {
                                // TODO: handle create connection failed
                            }
                        }, sdpAnswer);

                    } else {
                        // TODO: handle create connection failed
                    }

                } catch (Exception e) {
                    // TODO: handle create connection failed
                }
            }

            @Override
            public void onSetSuccess() {

            }

            @Override
            public void onCreateFailure(String s) {

            }

            @Override
            public void onSetFailure(String s) {

            }

            // Implement the other abstract methods...
        }, new MediaConstraints());

    }

    private void addMediaTracks(PeerConnectionFactory peerConnectionFactory) throws PermissionRequireException {
        mediaStream = peerConnectionFactory.createLocalMediaStream("localStream");
        AudioTrack audioTrack = Media.getInstance().getAudio(activity, peerConnectionFactory);
        mediaStream.addTrack(audioTrack);
        for (MediaStreamTrack track : mediaStream.audioTracks) {
            peerConnection.addTrack(track, Collections.singletonList(String.valueOf(mediaStream)));
        }
    }

    private void createPeerConnection(PeerConnectionFactory peerConnectionFactory, PeerConnection.RTCConfiguration rtcConfig) {
        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, new PeerConnection.Observer() {
            @Override
            public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            }

            @Override
            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            }

            @Override
            public void onIceConnectionReceivingChange(boolean b) {
            }

            @Override
            public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            }

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
            }

            @Override
            public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
            }

            @Override
            public void onRemoveStream(MediaStream mediaStream) {
            }

            @Override
            public void onDataChannel(DataChannel dc) {
                dataChannel = dc;
            }

            @Override
            public void onRenegotiationNeeded() {
            }

            @Override
            public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
            }
        });
    }

    private void createDataChannel() {
        DataChannel.Init init = new DataChannel.Init();
        dataChannel = peerConnection.createDataChannel("dataChannel", init);

        dataChannel.registerObserver(new DataChannel.Observer() {
            @Override
            public void onBufferedAmountChange(long l) {
                // Handle buffered amount change
            }

            @Override
            public void onStateChange() {
                // Handle state change
                System.out.println("Data channel state: " + dataChannel.state());
                if (dataChannel.state() == DataChannel.State.OPEN) {
                    activity.runOnUiThread(() -> {
                        speechRecognition = SpeechRecognitionImpl.getInstance(activity, instance);
                        speechRecognition.startListening();
                    });
                }
            }

            @Override
            public void onMessage(DataChannel.Buffer buffer) {
                // Handle received message
            }
        });

    }

    @Override
    public void send(String message) {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        dataChannel.send(new DataChannel.Buffer(buffer, false));
    }

    @Override
    public void closeConnection() {
        try {
            speechRecognition.destroy();
        } catch (Exception e) {
            // TODO: handle close connection failed
            System.out.println("ERROR close connection: " +e.getMessage());
        }
        try {
            if (dataChannel != null) {
                dataChannel.close();
            }
        } catch (Exception e) {
            // TODO: handle close connection failed
            System.out.println("ERROR close connection: " +e.getMessage());
        }
        try {
            if (peerConnection != null) {
                peerConnection.close();
            }
        } catch (Exception e) {
            // TODO: handle close connection failed
            System.out.println("ERROR close connection: " +e.getMessage());
        }
        try {
            if (mediaStream != null) {
                for (AudioTrack audioTrack : mediaStream.audioTracks) {
                    audioTrack.dispose();
                }
                mediaStream.dispose();
            }
        } catch (Exception e) {
            // TODO: handle close connection failed
            System.out.println("ERROR close connection: " +e.getMessage());
        }
        try {
            if (peerConnectionFactory != null) {
                peerConnectionFactory.dispose();
            }
        } catch (Exception e) {
            // TODO: handle close connection failed
            System.out.println("ERROR close connection: " +e.getMessage());
        }
        try {
            aPost(API_CLOSE_CONNECTION_URL, null, null, null);
        } catch (Exception e) {
            // TODO: handle close connection failed
            System.out.println("ERROR close connection: " +e.getMessage());
        }
        instance = null;
    }

}
