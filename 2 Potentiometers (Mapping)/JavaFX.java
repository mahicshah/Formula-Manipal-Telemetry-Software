package com.example.javafx_arduino_live;

import java.util.logging.Level;
import java.util.logging.Logger;

import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.skins.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jssc.SerialPort;
import static jssc.SerialPort.MASK_RXCHAR;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;
import eu.hansolo.medusa.Gauge;
import org.apache.poi.xddf.usermodel.BlackWhiteMode;

import  java.io.*;

public class Code extends Application {
    Gauge gauge = new Gauge();
    Gauge gauge2 = new Gauge();
    SerialPort arduinoPort = null;
    ObservableList<String> portList;

    Label labelValue;
    final int NUM_OF_POINT = 100;
    XYChart.Series series;

    private void detectPort(){

        portList = FXCollections.observableArrayList();

        String[] serialPortNames = SerialPortList.getPortNames();
        for(String name: serialPortNames){
            System.out.println(name);
            portList.add(name);
        }
    }

    @Override
    public void start(Stage primaryStage) {

        labelValue = new Label();
        labelValue.setFont(new Font("Arial", 28));


        detectPort();
        final ComboBox comboBoxPorts = new ComboBox(portList);
        comboBoxPorts.valueProperty()
                .addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {

                        System.out.println(newValue);
                        disconnectArduino();
                        connectArduino(newValue);
                    }
                });

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Value");

        final LineChart<Number,Number> lineChart =
                new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("Real Time Data Line Chart");

        series = new XYChart.Series();
        lineChart.setLegendVisible(false);
        lineChart.getData().add(series);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);

        for(byte i=0; i<NUM_OF_POINT; i++) {
            series.getData().add(new XYChart.Data(i, 0));
        }

        gauge.setTitle("Potentiometer 1");
        gauge.setMinValue(0.00);
        gauge.setMaxValue(1023.00);
        gauge.setMaxHeight(100000);
        gauge.setMaxWidth(100000);
        gauge.setSkin(new QuarterSkin(gauge));

        gauge2.setTitle("LDR");
        gauge2.setForegroundBaseColor(Color.RED);
        gauge2.setValueColor(Color.RED);
        gauge2.setTitleColor(Color.RED);
        gauge2.setNeedleColor(Color.RED);
        gauge2.setMinValue(0.00);
        gauge2.setMaxValue(1023.00);
        gauge2.setMaxHeight(100000);
        gauge2.setMaxWidth(100000);
        gauge2.setSkin(new QuarterSkin(gauge2));

        VBox vBox = new VBox();
        VBox vBox2 = new VBox();

        vBox.getChildren().addAll(comboBoxPorts, lineChart);
        vBox2.getChildren().addAll(comboBoxPorts, gauge, gauge2);

        StackPane root = new StackPane();

        root.getChildren().add(vBox2);

        Scene scene = new Scene(root, 500, 400);

        primaryStage.setTitle("GUI Multi-Node Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void shiftSeriesData(float newValue)
    {
        for(int i=0; i<NUM_OF_POINT-1; i++){
            XYChart.Data<String, Number> ShiftDataUp =
                    (XYChart.Data<String, Number>)series.getData().get(i+1);
            Number shiftValue = ShiftDataUp.getYValue();
            XYChart.Data<String, Number> ShiftDataDn =
                    (XYChart.Data<String, Number>)series.getData().get(i);
            ShiftDataDn.setYValue(shiftValue);
        }
        XYChart.Data<String, Number> lastData =
                (XYChart.Data<String, Number>)series.getData().get(NUM_OF_POINT-1);
        lastData.setYValue(newValue);
    }

    public boolean connectArduino(String port){

        System.out.println("Arduino is connected.");

        boolean success = false;
        SerialPort serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if(serialPortEvent.isRXCHAR()){
                    try {

                        int[] intarray = serialPort.readIntArray();

                        int pot1 = intarray[0];
                        int pot2 = intarray[1];

                        int data1 = pot1 * (1023/255);
                        int data2 = pot2 * (1023/255);

                        System.out.println(data1);
                        System.out.println(data2);
                        System.out.println("\n");

                        String str1 = String.valueOf(data1);
                        String str2 = String.valueOf(data2);

                        Platform.runLater(() -> {
                            labelValue.setText(String.valueOf(data1));
                            gauge.setValue(Double.parseDouble(str1));
                            gauge2.setValue(Double.parseDouble(str2));
                            shiftSeriesData((float)data1);
                        });

                    } catch (SerialPortException ex) {
                        Logger.getLogger(Code.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
            });

            arduinoPort = serialPort;
            success = true;
        } catch (SerialPortException ex) {
            Logger.getLogger(Code.class.getName())
                    .log(Level.SEVERE, null, ex);
            System.out.println("SerialPortException: " + ex);
        }
        return success;
    }

    public void disconnectArduino(){

        System.out.println("Arduino is disconnected.");
        if(arduinoPort != null){
            try {
                arduinoPort.removeEventListener();
                if(arduinoPort.isOpened()){
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
