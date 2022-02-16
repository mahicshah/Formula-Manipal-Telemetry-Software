int value1 = 0;
int value2 = 0;
int data1 = 0;
int data2 = 0;
int data3 = 0;
int data1mapped = 0;
int data2mapped = 0;
int data3mapped = 0;

int pin1 = A0;
int pin2 = A1;
int pin3 = A2;

void setup() {
  Serial.begin(9600);
}

void loop() {

 data1 = analogRead(pin1);
 data2 = analogRead(pin2);
 data3 = analogRead(pin3);
 
 data1mapped = map(data1, 0, 1023, 0, 255);
 data2mapped = map(data2, 0, 1023, 0, 255);
 data3mapped = map(data3, 0, 1023, 0, 255);

 uint8_t Potentiometer[4] = {data1mapped, data2mapped, data3mapped};
 
 Serial.write(Potentiometer, 4);

}
