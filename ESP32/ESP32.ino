#include <ESP32Servo.h>

#include <Key.h>
#include <Keypad.h>

#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;
String incommingString;
char incommingInfo;
String correctPassword;
int garageLights = 18;
int houseLights = 17;
int garageUp = 5;
int garageDown = 4;
int vrataOdprtaLed = 16;
int vrataZaprtaLed = 19;
const byte ROWS = 4; 
const byte COLS = 4;
char hexaKeys[ROWS][COLS] = {
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};

int vrataMotor = 21;

byte rowPins[ROWS] = {13, 12, 14, 27}; 
byte colPins[COLS] = {26, 25, 33, 32};

String password = "69420";
String passwordInput = "";

Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);
Servo garageServo;
Servo vrataServo;


void setup() {
  Serial.begin(115200);
  SerialBT.begin("Smart house"); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");
  garageServo.attach(2);
  pinMode(garageLights, OUTPUT);
  pinMode(houseLights, OUTPUT);
  pinMode(garageUp, INPUT);
  pinMode(garageDown, INPUT_PULLDOWN);
  pinMode(vrataMotor, OUTPUT);
  pinMode(vrataOdprtaLed, OUTPUT);
  pinMode(vrataZaprtaLed, OUTPUT);
  vrataServo.attach(vrataMotor);
  vrataServo.write(180);
  digitalWrite(garageLights, HIGH);
  digitalWrite(houseLights, HIGH);
}

void loop() {
  char customKey = customKeypad.getKey();
  if(vrataServo.read() >=180){
    digitalWrite(vrataZaprtaLed, HIGH);
    digitalWrite(vrataOdprtaLed, LOW);
  }
  if (vrataServo.read() <= 95){
    digitalWrite(vrataZaprtaLed, LOW);
    digitalWrite(vrataOdprtaLed, HIGH);
  }
  
  if (customKey){
    passwordInput.concat(customKey);
    if (passwordInput.length() == 5){
      if (passwordInput == password){
        Serial.println(passwordInput);
        openDoorFun();
        digitalWrite(vrataZaprtaLed, LOW);
        digitalWrite(vrataOdprtaLed, HIGH);
        delay(10000);
        closeDoorFun();
        passwordInput = "";
      }
      else{
        Serial.println("wrong pass  " + passwordInput);
        byte incorrectPassword = 'incorrect password';
        digitalWrite(vrataZaprtaLed, HIGH);
        delay(200);
        digitalWrite(vrataZaprtaLed, LOW);
        delay(200);
        digitalWrite(vrataZaprtaLed, HIGH);
        SerialBT.write(incorrectPassword);
        passwordInput = "";
      }
    }
  }

  if (SerialBT.available()){
      for(int i=0; i<20; i ++){
        if (SerialBT.available() > 0){
          incommingInfo = SerialBT.read();
          incommingString.concat(incommingInfo);
        }
        else{
          break;
        }
      }
      Serial.println(incommingString);

      if (incommingString == "open garage"){
        openGarageServo();
      }
      if (incommingString == "close garage"){
        closeGarageServo();
      }
      if (incommingString == "close door"){
        closeDoorFun();
      }
      if (incommingString == "open door"){
        openDoorFun();
      }
      if (incommingString[0] == 'p'){
        incommingString.remove(0,1);
        password = incommingString;
      }
      if (incommingString == "garage lights on"){
        garageLightsOn();
      }
      if (incommingString == "garage lights off"){
        garageLightsOff();
      }
      if (incommingString == "house lights on"){
        houseLightsOn();
      }
      if (incommingString == "house lights off"){
        houseLightsOff();
      }
      incommingString = "";

  delay(20);
  }
}
void garageLightsOn(){
  digitalWrite(garageLights, LOW);
}

void garageLightsOff(){
  digitalWrite(garageLights, HIGH);   
}

void houseLightsOn(){
  digitalWrite(houseLights, LOW);
}

void houseLightsOff(){
  digitalWrite(houseLights, HIGH);
}

void openGarageServo(){
  garageServo.attach(2);
  while(digitalRead(garageUp) == HIGH){
    garageServo.write(10);
    }
    garageServo.detach();
    return;
}
void closeGarageServo(){
  garageServo.attach(2);
  while(digitalRead(garageDown) != HIGH){
    garageServo.write(99);
  }
  garageServo.detach();
  return;
}

void openDoorFun(){
  vrataServo.write(90);
}
void closeDoorFun(){
  vrataServo.write(180);
}
