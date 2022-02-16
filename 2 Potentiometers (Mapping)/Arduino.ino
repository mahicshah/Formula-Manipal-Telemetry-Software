int value1 = 0;
int value2 = 0;
int data1 = 0;
int data2 = 0;
int data1mapped = 0;
int data2mapped = 0;

int pin1 = A0;
int pin2 = A1;

void setup() {
  Serial.begin(9600);
}

void loop() {

 data1 = analogRead(pin1);
 data2 = analogRead(pin2);
 
 data1mapped = map(data1, 0, 1023, 0, 255);
 data2mapped = map(data2, 0, 1023, 0, 255);

 int Potentiometer[] = {data1mapped, data2mapped};
 
 for(int i=0; i<2; i++) {
  Serial.write(Potentiometer[i]);
 }

}
