package com.example.javafx_arduino_live;

import java.util.logging.Level;
import java.util.logging.Logger;
import eu.hansolo.medusa.skins.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jssc.SerialPort;
import static jssc.SerialPort.MASK_RXCHAR;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;
import eu.hansolo.medusa.Gauge;
import java.io.*;

public class Code extends Application {

    Gauge gauge = new Gauge();

    SerialPort arduinoPort = null;
    ObservableList<String> portList;

    private void detectPort() {
        portList = FXCollections.observableArrayList();
        String[] serialPortNames = SerialPortList.getPortNames();
        for (String name : serialPortNames) {
            System.out.println(name);
            portList.add(name);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        detectPort();
        final ComboBox comboBoxPorts = new ComboBox(portList);
        comboBoxPorts.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
                disconnectArduino();
                connectArduino(newValue);
            }
        });

        gauge.setTitle("Potentiometer 1");
        gauge.setMinValue(0.00);
        gauge.setMaxValue(1023.00);
        gauge.setMaxHeight(100000);
        gauge.setMaxWidth(100000);
        gauge.setSkin(new TileSparklineSkin(gauge));

        VBox vBox = new VBox();
        vBox.getChildren().addAll(comboBoxPorts, gauge);
        vBox.setAlignment(Pos.CENTER);
        StackPane root = new StackPane();
        root.getChildren().add(vBox);
        Scene scene = new Scene(root);

        primaryStage.setTitle("GUI Test using ASCII");

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public boolean connectArduino(String port) {
        System.out.println("Arduino is connected.");
        boolean success = false;
        SerialPort serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if (serialPortEvent.isRXCHAR()) {
                    try {

                        byte[] b = serialPort.readBytes();

                        int value0 = b[0];
                        int i0 = Character.getNumericValue(value0);

                        int value1 = b[1];
                        int i1 = Character.getNumericValue(value1);

                        int value2 = b[2];
                        int i2 = Character.getNumericValue(value2);

                        int value3 = b[3];
                        int i3 = Character.getNumericValue(value3);

                        int value = (i0*1000 + i1*100 + i2*10 + i3);
                        String str = String.valueOf(value);
                        System.out.println(str);

                        Platform.runLater(() -> {
                            gauge.setValue(Double.parseDouble(str));
                        });

                    } catch (SerialPortException ex) {
                        Logger.getLogger(Code.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            arduinoPort = serialPort;
            success = true;
        } catch (SerialPortException ex) {
            Logger.getLogger(Code.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SerialPortException: " + ex);
        }
        return success;
    }

    public void disconnectArduino() {
        System.out.println("Arduino has been disconnected.");
        if (arduinoPort != null) {
            try {
                arduinoPort.removeEventListener();
                if (arduinoPort.isOpened()) {
                    arduinoPort.closePort();
                }
            } catch (SerialPortException ex) {
                Logger.getLogger(Code.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void stop() throws Exception {
        disconnectArduino();
        super.stop();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
