package com.example.smartapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartapp.R;

public class Skylight extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fregment_skylight, container, false);
    }


    public void open(){
        ((cBaseApplication) getContext().getApplicationContext()).bluetoothConnection.sendCommand("open skylight");
    }
    public void close(){
        ((cBaseApplication) getContext().getApplicationContext()).bluetoothConnection.sendCommand("close skylight");
    }
}
