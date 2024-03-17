package com.devlogex.yue.android.ui.call;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.devlogex.yue.android.databinding.FragmentCallBinding;

import java.util.ArrayList;

public class CallFragment extends Fragment {

    private FragmentCallBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CallViewModel callViewModel =
                new ViewModelProvider(this).get(CallViewModel.class);

        binding = FragmentCallBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
//        callViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        ///////////////////////////////////////
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && matches.size() > 0) {
                    // Use the first match as the most accurate result
                    String text = matches.get(0);
                    // Handle the recognized text
                    System.out.println("Got a text result: " + text);
                    textView.setText(text);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }

            // Implement other methods from RecognitionListener as needed
        });

        // Start listening
        speechRecognizer.startListening(intent);
        System.out.println("Started ................");




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}