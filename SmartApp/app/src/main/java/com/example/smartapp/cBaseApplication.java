package com.example.smartapp;

import android.app.Application;


public class cBaseApplication extends Application {
    public BluetoothConnection bluetoothConnection;

    @Override
    public void onCreate()
    {
        super.onCreate();
        bluetoothConnection = new BluetoothConnection();
    }
}
