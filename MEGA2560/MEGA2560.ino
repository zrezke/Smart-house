#include <LiquidCrystal.h>

LiquidCrystal lcd(12, 11, 5, 4, 3, 2);

void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);

}

void loop() {
  int solar1 = analogRead(A0);
  int solar2 = analogRead(A1);
  int solar3 = analogRead(A2);
  int solar4 = analogRead(A3);
  int solar5 = analogRead(A4);
  int solar6 = analogRead(A5);
  int solar7 = analogRead(A6);
  int solar8 = analogRead(A7);
  int allSolar = solar1 + solar2 + solar3 + solar4 + solar5 + solar6 + solar7 + solar8;
  float solarVoltage = allSolar *(5.0/1023.0);
  lcd.setCursor(0, 1);
  Serial.println(solarVoltage);
  lcd.print("Voltage: ");
  lcd.print(solarVoltage);
  delay(300);
  }
