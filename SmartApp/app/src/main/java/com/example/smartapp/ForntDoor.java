package com.example.smartapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartapp.R;

public class ForntDoor extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(((cBaseApplication) getContext().getApplicationContext()).bluetoothConnection.receive()){
            Toast.makeText(getContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
        }
        return inflater.inflate(R.layout.fregment_front_door, container, false);
    }

    public void open(){
        ((cBaseApplication) getContext().getApplicationContext()).bluetoothConnection.sendCommand("open door");
    }
    public void close(){
        ((cBaseApplication) getContext().getApplicationContext()).bluetoothConnection.sendCommand("close door");
    }
}
