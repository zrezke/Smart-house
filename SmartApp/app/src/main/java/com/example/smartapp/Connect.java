package com.example.smartapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOError;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.UUID;

public class Connect extends Fragment implements AdapterView.OnItemClickListener {

    public static String EXTRA_ADDRESS = "device_address";

    public ArrayList<BluetoothDevice> deviceArray = new ArrayList<>();
    public BluetoothAdapter btAdapter;
    public ListView deviceListView;
    public DeviceListAdapter deviceListAdapter;
    Button startDiscoveryBtn;
    Button enableBtBtn;
    BluetoothStatusThread btStatus;
    BluetoothDevice device;
    BluetoothConnection bluetoothConnection;
    public BluetoothDevice btDevice;
    UUID MY_UUID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fregment_connect, container, false);
        enableBtBtn = rootView.findViewById(R.id.enableBt_btn);
        startDiscoveryBtn = rootView.findViewById(R.id.discoverButton);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceListView = rootView.findViewById(R.id.device_list);
        btStatus = new BluetoothStatusThread(this, btAdapter);
        btStatus.start();
        deviceListView.setOnItemClickListener(this);
        MY_UUID = UUID.fromString("c327bf7a-8bb3-11ea-bc55-0242ac130003");

        IntentFilter pairFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        getActivity().registerReceiver(broadcastReceiver3, pairFilter);

        startDiscoveryBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startDiscovery(view);
            }
        });

        enableBtBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                enableDisableBT(view);
            }

        });
        return rootView;
    }

    public void onDestroy(){
        super.onDestroy();
        BluetoothStatusThread.currentThread().interrupt();
    }





    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(btAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, btAdapter.ERROR);
                String btnText;
                Button btn = getView().findViewById(R.id.enableBt_btn);
                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(getActivity().getApplicationContext(), "Bluetooth is turned OFF", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(getActivity().getApplicationContext(), "Bluetooth is turned ON", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        }};

    private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceArray.add(device);
                deviceListAdapter = new DeviceListAdapter(getContext(), R.layout.device_listing_view, deviceArray);
                deviceListView.setAdapter(deviceListAdapter);
            }
        }
    };

    private BroadcastReceiver broadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED){
                    Toast.makeText(getContext(), "Bonding was successful", Toast.LENGTH_SHORT).show();
                }
                if (device.getBondState() == BluetoothDevice.BOND_BONDING){
                    Toast.makeText(getContext(), "Bonding", Toast.LENGTH_SHORT).show();
                }
                if (device.getBondState() == BluetoothDevice.BOND_NONE){
                    Toast.makeText(getContext(), "Bond didn't succeed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    public void enableDisableBT(View view){
        deviceArray.clear();

        try {
            deviceListAdapter.notifyDataSetChanged();
        }
        catch(NullPointerException e){
            assert true;
        }

        if (btAdapter == null){
            Toast.makeText(getContext(), "Your device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }
        if (!btAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivity(enableBtIntent);
            Intent enableDiscoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            enableDiscoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(enableDiscoveryIntent);

            IntentFilter BtIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(mBroadcastReceiver1, BtIntent);
        }
        if (btAdapter.isEnabled()){
            btAdapter.disable();
            IntentFilter  BtIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(mBroadcastReceiver1, BtIntent);
        }

    }

    public void startDiscovery(View view){

        deviceArray.clear();
        try {
            deviceListAdapter.notifyDataSetChanged();
        }
        catch(NullPointerException e){
            assert true;
        }

        if (btAdapter.isDiscovering()) {
            checkBTPermissions();
            btAdapter.startDiscovery();
            IntentFilter discoveryIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(broadcastReceiver2, discoveryIntent);
        }
        if (!btAdapter.isDiscovering()){
            checkBTPermissions();
            btAdapter.startDiscovery();
            IntentFilter discoveryIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(broadcastReceiver2, discoveryIntent);
        }
    }

    private void checkBTPermissions(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = ContextCompat.checkSelfPermission(getContext(), "Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += ContextCompat.checkSelfPermission(getContext(), "Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0){
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        btAdapter.cancelDiscovery();
        String deviceName = deviceArray.get(position).getName();
        String deviceAddress = deviceArray.get(position).getAddress();
        btDevice = deviceArray.get(position);
        String address = btDevice.getAddress();
        System.out.println(address);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            deviceArray.get(position).createBond();
            ((cBaseApplication) getContext().getApplicationContext()).bluetoothConnection.connect(address);
        }
    }
}
