package com.example.smartapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class SpeechRecognition extends Fragment{

    private String speechText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fregment_speech_rec, container, false);
        
        return rootView;
    }
    BluetoothConnection bluetoothConnection = new BluetoothConnection();

    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(getContext(), "Your device doesn't support this function!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechText = result.get(0);
                    sendSpeechText();

                }
                break;
        }
    }
    public void sendSpeechText() {
        String command;
        command = null;
        //  command to open the door
        if (speechText.contains("open") && speechText.contains("door") && !speechText.contains("garage")) {
            command = "open door";
        }
        if (speechText.contains("close") && speechText.contains("door") && !speechText.contains("garage")) {
            command = "close door";
        }

        //  command to open garage
        if (speechText.contains("open") && speechText.contains("garage")) {
            command = "open garage";
        }

        //  command to close garage
        if (speechText.contains("close") && speechText.contains("garage")) {
            command = "close garage";
        }

        //  command to turn garage lights on
        if (speechText.contains("light") && speechText.contains("on") && speechText.contains("garage")) {
            command = "garage lights on";
        }

        //  command to turn garage lights off
        if (speechText.contains("light") && speechText.contains("off") && speechText.contains("garage")) {
            command = "garage lights off";
        }

        //  command to turn house lights on
        if (speechText.contains("light") && speechText.contains("on") && speechText.contains("house")) {
            command = "house lights on";
        }

        //  command to turn house lights off
        if (speechText.contains("light") && speechText.contains("off") && speechText.contains("house")) {
            command = "house lights off";
        }

        //  command to open skylight
        if (speechText.contains("open") && speechText.contains("skylight") || speechText.contains("roof window")) {
            command = "open skylight";
        }
        //  command to close skylight
        if (speechText.contains("close") && speechText.contains("skylight") || speechText.contains("roof window")) {
            command = "close skylight";
        }

        //  check if command was valid
        if (command != null) {
            System.out.println(command);
            ((cBaseApplication) getContext().getApplicationContext()).bluetoothConnection.sendCommand(command);
        } else {
            Toast.makeText(getContext(), "No such command!", Toast.LENGTH_SHORT).show();
        }


    }


}
