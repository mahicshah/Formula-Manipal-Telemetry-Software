void setup() {
  Serial.begin(9600);
}
 
void loop() {
  
  int pot = analogRead(A0);
  byte b1 = pot / 256;
  byte b2 = pot % 256;

  Serial.write(b1);
  Serial.write(b2);
}
