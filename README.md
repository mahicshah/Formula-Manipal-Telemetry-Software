# JavaFX_GUI

## Project:

Display real time data from multiple potentiometers simultaneously in a Graphical User Interface (GUI).

### Components required:

- Arduino Uno (SMD preferrable)
- USB A to USB B Cable
- Breadboard
- Jumper Wires
- Potentiometers (multiple)
- Light Dependent Resistor (optional)

### Softwares used:

- Arduino IDE
- IntelliJ Idea

### Hardware Setup:

![WhatsApp Image 2022-02-07 at 11 36 20 AM](https://user-images.githubusercontent.com/82862036/152735262-eaa6aa0f-6919-4306-be1b-79684143f72f.jpeg)

1. Connect a jumper wire from the 5V pin of the Arduino to the breadboard. This will act as the main 5V supply to all the potentiometers.
2. Connect a jumper wire from the GND pin of the Arduino to the breadboard. This will act as the common ground to all the potentiometers.
3. Connect one extreme end of all the potentiometers to the 5V supply line.
4. Connect the other extreme end of all the potentiometers to the GND line.
5. Connect the signal terminal of all the potentiometers to the Analog Pins (A0, A1, A2, etc.) of the Arduino.

### Software Setup:

1. Connect the Arduino to your laptop.
2. Open Arduino IDE, and paste the code (depends on the number of potentiometers used).
3. Open IntelliJ Idea, create a new JavaFX project with the name 'JavaFX_Arduino_Live'.
4. 
