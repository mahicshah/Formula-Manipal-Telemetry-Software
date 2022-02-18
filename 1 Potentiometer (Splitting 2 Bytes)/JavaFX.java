package com.example.javafx_arduino_live;

import java.util.logging.Level;
import java.util.logging.Logger;
import eu.hansolo.medusa.skins.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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
import jssc.*;

import static jssc.SerialPort.MASK_RXCHAR;
import static jssc.SerialPort.MASK_RXFLAG;

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
        comboBoxPorts.valueProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            System.out.println(newValue);
            disconnectArduino();
            connectArduino(newValue);
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

        primaryStage.setTitle("GUI Test (One Byte)");

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
                        byte[] b = serialPort.readBytes(2);

                        int x = b[0] & 0xFF;
                        int y = b[1] & 0xFF;

                        int total = ((x * 256) + y);

                        String str = String.valueOf(total);
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
