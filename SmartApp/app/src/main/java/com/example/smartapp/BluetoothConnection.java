package com.example.smartapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import java.util.UUID;



public class BluetoothConnection{
    BluetoothAdapter btAdapter = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public void connect(String btAddress){
        try
        {
            if (btSocket == null || !isBtConnected)
            {
                btAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice remoteDevice = btAdapter.getRemoteDevice(btAddress);//connects to the device's address and checks if it's available
                btSocket = remoteDevice.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
            }
            else if (btSocket != null){
                btSocket.close();
                btAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice dispositivo = btAdapter.getRemoteDevice(btAddress);//connects to the device's address and checks if it's available
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
            }
        }
        catch (IOException e)
        {
            System.out.println("Connection failed, IO EXCEPTION LINE 44.");
        }

    }
    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            {System.out.println("IO Exception line 48 BluetoothConnection.");}
        }
    }
    public void sendCommand(String command){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(command.getBytes());
            }
            catch (IOException e)
            {
                System.out.println("Didn't send data. BluetoothConnection line 63. IO Exception.");
            }
        }
    }
    public boolean receive(){
        if (btSocket != null){
            try{
                String incorrectPassword = btSocket.getInputStream().toString();
                if(incorrectPassword == "incorrect password"){
                    return true;
                }
            }catch(IOException e1){
                System.out.println("Couldn't retrieve data");
            }
        }
        return false;
    }
}