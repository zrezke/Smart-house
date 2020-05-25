package com.example.smartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private SpeechRecognition speechRecognition;
    private ForntDoor frontDoor;
    private GarageDoor garageDoor;
    private Lights lights;
    private Skylight skylight;
    public Connect connect;
    private ImageView microphoneIV;
    public String newPassword;
    public char[] possibleCharArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        speechRecognition = new SpeechRecognition();
        frontDoor = new ForntDoor();
        garageDoor = new GarageDoor();
        lights = new Lights();
        skylight = new Skylight();
        connect = new Connect();
        possibleCharArray = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', '*', '#'};



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container,
                    connect).commit();
            navigationView.setCheckedItem(R.id.nav_connect);
        }
    }

    public void getSpeech(View view){
        speechRecognition.getSpeechInput(view);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_connect:
                getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container,
                        connect).commit();
                break;

            case R.id.nav_speech:
                getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container,
                        speechRecognition).commit();
                break;

            case R.id.nav_door:
                getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container,
                        frontDoor).commit();
                break;

            case R.id.nav_garage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container,
                        garageDoor).commit();
                break;

            case R.id.nav_lights:
                getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container,
                        lights).commit();
                break;

                /*                               ##  Currently not available. Motors haven't arrived
            case R.id.nav_skylight:
                getSupportFragmentManager().beginTransaction().replace(R.id.fregment_container,
                        skylight).commit();
                break;

                 */
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openSkylight(View view){
        skylight.open();
    }
    public void closeSkylight(View view){
        skylight.close();
    }

    public void turnGarageLightsOn(View view){
        lights.turnOnGarage();
    }
    public void turnGarageLightsOff(View view){
        lights.turnOffGarage();
    }
    public void turnOnHouseLights(View view){
        lights.turnOnHouse();
    }
    public void turnOffHouseLights(View view){
        lights.turnOffHouse();
    }

    public void enableDisableBt(View view){
        connect.enableDisableBT(view);
}
    public void discoverDevices(View view){
        connect.startDiscovery(view);
    }

    public void openDoor(View view){
        frontDoor.open();
    }

    public void closeGarage(View view){
        garageDoor.close();
    }
    public void openGarage(View view){
        garageDoor.open();
    }

    public void changePass(View view){
        int wrongChar;
        int rightChar = 0;
        EditText newPass = findViewById(R.id.newPasswordTxt);
        newPassword = newPass.getText().toString().toUpperCase();
        if (newPassword.length() == 5){
            for(int element = 0; element < newPassword.length(); element++){
                wrongChar = 0;
                for (int i = 0; i<possibleCharArray.length; i++){
                    if (newPassword.charAt(element) != possibleCharArray[i]){
                        wrongChar++;
                    }
                    else{
                        rightChar++;
                    }
                    if (wrongChar == 16) {
                        Toast.makeText(getApplicationContext(), "Illegal password: illegal character", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if(rightChar == 5){
                    newPassword = "p" + newPassword;
                    ((cBaseApplication) getApplicationContext()).bluetoothConnection.sendCommand(newPassword);
                    break;
                }

            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Illegal password: Must be 5 characters!", Toast.LENGTH_SHORT).show();
        }
    }

    public void closeDoor(View view){
        frontDoor.close();
    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
