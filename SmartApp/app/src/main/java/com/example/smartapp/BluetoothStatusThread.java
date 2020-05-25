package com.example.smartapp;


import android.bluetooth.BluetoothAdapter;
import android.widget.Button;

class BluetoothStatusThread extends Thread{
    Connect connect;
    BluetoothAdapter btAdapter;
    Button startDiscoveryBtn;
    Button enableBtBtn;
    BluetoothStatusThread(Connect connect, BluetoothAdapter btAdapter){
        this.connect = connect;
        this.btAdapter = btAdapter;
    }
    public void run(){
        startDiscoveryBtn = connect.startDiscoveryBtn;
        enableBtBtn = connect.enableBtBtn;
        while(!interrupted()){
            try {
                if (btAdapter.isDiscovering()) {
                    startDiscoveryBtn.setBackgroundColor(0xFFFF7500);
                    startDiscoveryBtn.setText("Discovering");
                }
                if (!btAdapter.isDiscovering()) {
                    startDiscoveryBtn.setBackgroundColor(0xFF00B6D6);
                    startDiscoveryBtn.setText("Discover devices");
                }
                if (btAdapter.isEnabled()) {
                    enableBtBtn.setText("Disable bluetooth");
                    enableBtBtn.setBackgroundColor(0xFFFF7500);
                }
                if (!btAdapter.isEnabled()) {
                    enableBtBtn.setBackgroundColor(0xFF00C986);
                    enableBtBtn.setText("Enable bluetooth");
                }
            }
            catch (NullPointerException e){
                assert true;
            }
        }
    }
}