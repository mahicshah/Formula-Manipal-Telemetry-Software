void setup() {
  Serial.begin(115200);
}

void loop() {
  int pot = analogRead(A0);
  byte higherbyte = pot % 256;
  byte lowerbyte = pot / 256;
  byte arr[11] = {5, 240, 8, higherbyte, lowerbyte, 145, 2, 100, 3, higherbyte, lowerbyte};
  Serial.write(arr, 11);
  delay(100);
}
