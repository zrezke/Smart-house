package com.example.smartapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartapp.R;

public class Lights extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fregment_lights, container, false);
    }
    public void turnOnGarage(){
        ((cBaseApplication) getContext().getApplicationContext()).bluetoothConnection.sendCommand("garage lights on");
    }
    public void turnOffGarage(){
        ((cBaseApplication) getContext().getApplicationContext()).bluetoothConnection.sendCommand("garage lights off");
    }
    public void turnOnHouse(){
        ((cBaseApplication) getContext().getApplicationContext()).bluetoothConnection.sendCommand("house lights on");
    }
    public void turnOffHouse(){
        ((cBaseApplication) getContext().getApplicationContext()).bluetoothConnection.sendCommand("house lights off");
    }
}
