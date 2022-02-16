void setup() {
  Serial.begin(9600);
}

void loop() {
  int ar[4] = {0};
  int data = analogRead(A0);
  
  ar[0] = (((data / 1000) % 10) + '0');
  ar[1] = (((data / 100) % 10) + '0');
  ar[2] = (((data / 10) % 10) + '0');
  ar[3] = (((data / 1) % 10) + '0');
  
  for(int i=0; i<4; i++){
  Serial.write(ar[i]);
  }
}
