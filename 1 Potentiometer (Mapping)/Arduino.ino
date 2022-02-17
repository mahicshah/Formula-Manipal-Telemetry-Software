void setup() {
    Serial.begin(115200);
}

void loop() {
  int analogValue = analogRead(A0);
  analogValue = map(analogValue, 0, 1023, 0, 255);

  Serial.write(analogValue);
}
