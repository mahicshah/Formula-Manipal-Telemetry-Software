package com.example.javafx_arduino_live;

import java.util.logging.Level;
import java.util.logging.Logger;
import eu.hansolo.medusa.skins.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jssc.*;
import static jssc.SerialPort.MASK_RXCHAR;
import eu.hansolo.medusa.Gauge;
import java.io.*;

public class Code extends Application {

    BorderPane layout= new BorderPane();

    Gauge gauge1 = new Gauge();
    Gauge gauge2 = new Gauge();
    Gauge gauge3 = new Gauge();
    Gauge gauge4 = new Gauge();
    Gauge gauge5 = new Gauge();
    Gauge gauge6 = new Gauge();
    Gauge gauge7 = new Gauge();
    Gauge gauge8 = new Gauge();
    Gauge gauge9 = new Gauge();
    Gauge gauge10 = new Gauge();
    Gauge gauge11 = new Gauge();
    Gauge gauge12 = new Gauge();

    Menu File = new Menu("File");
    Menu Save = new Menu("Save");
    Menu Tools = new Menu("Tools");
    Menu Ports = new Menu("Ports");

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
    public void start(Stage primaryStage) throws FileNotFoundException {
        detectPort();
        final ComboBox comboBoxPorts = new ComboBox(portList);
        comboBoxPorts.valueProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            System.out.println(newValue);
            disconnectArduino();
            connectArduino(newValue);
        });

        File.getItems().add(Save);
        Tools.getItems().add(Ports);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        gauge1.setTitle("RPM");
        gauge1.setTitleColor(Color.WHITE);
        gauge1.setMinValue(0.00);
        gauge1.setMaxValue(1023.00);
        gauge1.setNeedleColor(Color.WHITE);
        gauge1.setUnitColor(Color.WHITE);
        gauge1.setValueColor(Color.WHITE);
        gauge1.setForegroundBaseColor(Color.WHITE);
//        gauge1.setThreshold(900);
//        gauge1.setThresholdVisible(true);
//        gauge1.setThresholdColor(Color.RED);
        gauge1.setTickLabelsVisible(false);

        gauge2.setTitle("OPS");
        gauge2.setTitleColor(Color.WHITE);
        gauge2.setMinValue(0.00);
        gauge2.setMaxValue(1023.00);
        gauge2.setNeedleColor(Color.WHITE);
        gauge2.setUnitColor(Color.WHITE);
        gauge2.setValueColor(Color.WHITE);
        gauge2.setForegroundBaseColor(Color.WHITE);
        //gauge2.setSkin(new TileSparklineSkin(gauge2));
        gauge2.setTickLabelsVisible(false);

        gauge3.setTitle("ECT 1");
        gauge3.setTitleColor(Color.WHITE);
        gauge3.setMinValue(0.00);
        gauge3.setMaxValue(1023.00);
        gauge3.setNeedleColor(Color.WHITE);
        gauge3.setUnitColor(Color.WHITE);
        gauge3.setValueColor(Color.WHITE);
        gauge3.setForegroundBaseColor(Color.WHITE);
        //gauge3.setSkin(new HSkin(gauge3));
        gauge3.setTickLabelsVisible(false);

        gauge4.setTitle("ECT 2");
        gauge4.setTitleColor(Color.WHITE);
        gauge4.setMinValue(0.00);
        gauge4.setMaxValue(1023.00);
        gauge4.setNeedleColor(Color.WHITE);
        gauge4.setUnitColor(Color.WHITE);
        gauge4.setValueColor(Color.WHITE);
        gauge4.setForegroundBaseColor(Color.WHITE);
        //gauge4.setSkin(new DigitalSkin(gauge4));
        gauge4.setTickLabelsVisible(false);

        gauge5.setTitle("WS FR");
        gauge5.setTitleColor(Color.WHITE);
        gauge5.setMinValue(0.00);
        gauge5.setMaxValue(1023.00);
        gauge5.setNeedleColor(Color.WHITE);
        gauge5.setUnitColor(Color.WHITE);
        gauge5.setValueColor(Color.WHITE);
        gauge5.setForegroundBaseColor(Color.WHITE);
        //gauge5.setSkin(new BulletChartSkin(gauge5));
        gauge5.setTickLabelsVisible(false);

        gauge6.setTitle("WS FL");
        gauge6.setTitleColor(Color.WHITE);
        gauge6.setMinValue(0.00);
        gauge6.setMaxValue(1023.00);
        gauge6.setNeedleColor(Color.WHITE);
        gauge6.setUnitColor(Color.WHITE);
        gauge6.setValueColor(Color.WHITE);
        gauge6.setForegroundBaseColor(Color.WHITE);
        //gauge6.setSkin(new AmpSkin(gauge6));
        gauge6.setTickLabelsVisible(false);

        gauge7.setTitle("WS RR");
        gauge7.setTitleColor(Color.WHITE);
        gauge7.setMinValue(0.00);
        gauge7.setMaxValue(1023.00);
        gauge7.setNeedleColor(Color.WHITE);
        gauge7.setUnitColor(Color.WHITE);
        gauge7.setValueColor(Color.WHITE);
        gauge7.setForegroundBaseColor(Color.WHITE);
        //gauge7.setSkin(new QuarterSkin(gauge7));
        gauge7.setTickLabelsVisible(false);

        gauge8.setTitle("WS RL");
        gauge8.setTitleColor(Color.WHITE);
        gauge8.setMinValue(0.00);
        gauge8.setMaxValue(1023.00);
        gauge8.setNeedleColor(Color.WHITE);
        gauge8.setUnitColor(Color.WHITE);
        gauge8.setValueColor(Color.WHITE);
        gauge8.setForegroundBaseColor(Color.WHITE);
        //gauge8.setSkin(new SlimSkin(gauge8));
        gauge8.setTickLabelsVisible(false);

        gauge9.setTitle("TPS");
        gauge9.setTitleColor(Color.WHITE);
        gauge9.setMinValue(0.00);
        gauge9.setMaxValue(1023.00);
        gauge9.setNeedleColor(Color.WHITE);
        gauge9.setUnitColor(Color.WHITE);
        gauge9.setValueColor(Color.WHITE);
        gauge9.setForegroundBaseColor(Color.WHITE);
        //gauge9.setSkin(new SlimSkin(gauge8));
        gauge9.setTickLabelsVisible(false);

        gauge10.setTitle("BT VOL");
        gauge10.setTitleColor(Color.WHITE);
        gauge10.setMinValue(0.00);
        gauge10.setMaxValue(1023.00);
        gauge10.setNeedleColor(Color.WHITE);
        gauge10.setUnitColor(Color.WHITE);
        gauge10.setValueColor(Color.WHITE);
        gauge10.setForegroundBaseColor(Color.WHITE);
        //gauge10.setSkin(new SlimSkin(gauge8));
        gauge10.setTickLabelsVisible(false);

        gauge11.setTitle("FP");
        gauge11.setTitleColor(Color.WHITE);
        gauge11.setMinValue(0.00);
        gauge11.setMaxValue(1023.00);
        gauge11.setNeedleColor(Color.WHITE);
        gauge11.setUnitColor(Color.WHITE);
        gauge11.setValueColor(Color.WHITE);
        gauge11.setForegroundBaseColor(Color.WHITE);
        //gauge11.setSkin(new SlimSkin(gauge8));
        gauge11.setTickLabelsVisible(false);

        gauge12.setTitle("EXTRA");
        gauge12.setTitleColor(Color.WHITE);
        gauge12.setMinValue(0.00);
        gauge12.setMaxValue(1023.00);
        gauge12.setNeedleColor(Color.WHITE);
        gauge12.setUnitColor(Color.WHITE);
        gauge12.setValueColor(Color.WHITE);
        gauge12.setForegroundBaseColor(Color.WHITE);
        //gauge12.setSkin(new SlimSkin(gauge8));
        gauge12.setTickLabelsVisible(false);

        GridPane gridpane = new GridPane();
        gridpane.setBackground(new Background(new BackgroundFill(Color.valueOf("#1c1d1f"), CornerRadii.EMPTY, Insets.EMPTY)));
        ColumnConstraints column0 = new ColumnConstraints(bounds.getWidth()/6);
        column0.setHgrow(Priority.ALWAYS);
        ColumnConstraints column1 = new ColumnConstraints(bounds.getWidth()/6);
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints(bounds.getWidth()/6);
        column2.setHgrow(Priority.ALWAYS);
        ColumnConstraints column3 = new ColumnConstraints(bounds.getWidth()/6);
        column3.setHgrow(Priority.ALWAYS);
        ColumnConstraints column4 = new ColumnConstraints(bounds.getWidth()/6);
        column4.setHgrow(Priority.ALWAYS);
        ColumnConstraints column5 = new ColumnConstraints(bounds.getWidth()/6);
        column5.setHgrow(Priority.ALWAYS);
        RowConstraints row0 = new RowConstraints(bounds.getHeight()*0.1);
        row0.setVgrow(Priority.ALWAYS);
        RowConstraints row1 = new RowConstraints(bounds.getHeight()*0.4);
        row1.setVgrow(Priority.ALWAYS);
        RowConstraints row2 = new RowConstraints(bounds.getHeight()*0.4);
        row2.setVgrow(Priority.ALWAYS);

        gridpane.getColumnConstraints().addAll(column0, column1, column2, column3);
        gridpane.getRowConstraints().addAll(row0, row1, row2);
        gridpane.add(comboBoxPorts, 0, 0);
        gridpane.add(gauge1, 0, 1);
        gridpane.add(gauge2, 1, 1);
        gridpane.add(gauge3, 2, 1);
        gridpane.add(gauge4, 3, 1);
        gridpane.add(gauge5, 4, 1);
        gridpane.add(gauge6, 5, 1);
        gridpane.add(gauge7, 0, 2);
        gridpane.add(gauge8, 1, 2);
        gridpane.add(gauge9, 2, 2);
        gridpane.add(gauge10, 3, 2);
        gridpane.add(gauge11, 4, 2);
        gridpane.add(gauge12, 5, 2);

        FileInputStream imageStream = new FileInputStream("/Users/MahicShah/Library/CloudStorage/OneDrive-ManipalAcademyofHigherEducation/Formula Manipal/Software/JavaFX_Arduino_Live/FM_Logo trans white.png");
        Image image = new Image(imageStream);
        ImageView imageView = new ImageView(image);
        imageView.setX(170);
        imageView.setY(10);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        gridpane.add(imageView, 5, 0);
        gridpane.setHalignment(imageView, HPos.RIGHT);

        FileInputStream imageStream1 = new FileInputStream("/Users/MahicShah/Library/CloudStorage/OneDrive-ManipalAcademyofHigherEducation/Formula Manipal/Software/JavaFX_Arduino_Live/Transperant logo.png");
        Image image1 = new Image(imageStream1);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(File, Tools);

        layout.setTop(menuBar);
        layout.setCenter(gridpane);

        Scene scene = new Scene(layout);

        primaryStage.setTitle("Genesis");
        primaryStage.getIcons().add(image1);
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public boolean connectArduino(String port){
        System.out.println("Arduino is connected.");
        boolean success = false;
        SerialPort serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if (serialPortEvent.isRXCHAR()) {
                    try {
                        byte[] b = serialPort.readBytes(10);

                        int x = b[0] & 0xFF;
                        int y = b[1] & 0xFF;
                        int id = ((x * 256) + y);

                        if (id == 1520) {
                            int rpm_1 = b[3] & 0xFF;
                            int rpm_2 = b[4] & 0xFF;
                            int rpm_total = ((rpm_1 * 256) + rpm_2);
                            String rpm_str = String.valueOf(rpm_total);
                            System.out.println("RPM: " +rpm_str);
                            Platform.runLater(() -> gauge1.setValue(Double.parseDouble(rpm_str)));

                            int ops_1 = b[5] & 0xFF;
                            int ops_2 = b[6] & 0xFF;
                            int ops_total = ((ops_1 * 256) + ops_2);
                            String ops_str = String.valueOf(ops_total);
                            System.out.println("OPS: " +ops_str);
                            Platform.runLater(() -> gauge2.setValue(Double.parseDouble(ops_str)));

                            int ect1_1 = b[7] & 0xFF;
                            int ect1_2 = b[8] & 0xFF;
                            int ect1_total = ((ect1_1 * 256) + ect1_2);
                            String ect1_str = String.valueOf(ect1_total);
                            System.out.println("ECT1: " +ect1_str);
                            Platform.runLater(() -> gauge3.setValue(Double.parseDouble(ect1_str)));

                            int ect2_1 = b[9] & 0xFF;
                            int ect2_2 = b[10] & 0xFF;
                            int ect2_total = ((ect2_1 * 256) + ect2_2);
                            String ect2_str = String.valueOf(ect2_total);
                            System.out.println("ECT2: " +ect2_str);
                            Platform.runLater(() -> gauge4.setValue(Double.parseDouble(ect2_str)));
                        }

                        if (id == 1521) {
                            int wsfr_1 = b[3] & 0xFF;
                            int wsfr_2 = b[4] & 0xFF;
                            int wsfr_total = ((wsfr_1 * 256) + wsfr_2);
                            String wsfr_str = String.valueOf(wsfr_total);
                            System.out.println("WS FR: " +wsfr_str);
                            Platform.runLater(() -> gauge5.setValue(Double.parseDouble(wsfr_str)));

                            int wsfl_1 = b[5] & 0xFF;
                            int wsfl_2 = b[6] & 0xFF;
                            int wsfl_total = ((wsfl_1 * 256) + wsfl_2);
                            String wsfl_str = String.valueOf(wsfl_total);
                            System.out.println("WS FL: " +wsfl_str);
                            Platform.runLater(() -> gauge6.setValue(Double.parseDouble(wsfl_str)));

                            int wsrr_1 = b[7] & 0xFF;
                            int wsrr_2 = b[8] & 0xFF;
                            int wsrr_total = ((wsrr_1 * 256) + wsrr_2);
                            String wsrr_str = String.valueOf(wsrr_total);
                            System.out.println("WS RR: " +wsrr_str);
                            Platform.runLater(() -> gauge7.setValue(Double.parseDouble(wsrr_str)));

                            int wsrl_1 = b[9] & 0xFF;
                            int wsrl_2 = b[10] & 0xFF;
                            int wsrl_total = ((wsrl_1 * 256) + wsrl_2);
                            String wsrl_str = String.valueOf(wsrl_total);
                            System.out.println("WS RL: " +wsrl_str);
                            Platform.runLater(() -> gauge8.setValue(Double.parseDouble(wsrl_str)));
                        }

                        if (id == 1522) {
                            int tps_1 = b[3] & 0xFF;
                            int tps_2 = b[4] & 0xFF;
                            int tps_total = ((tps_1 * 256) + tps_2);
                            String tps_str = String.valueOf(tps_total);
                            System.out.println("TPS: " +tps_str);
                            Platform.runLater(() -> gauge9.setValue(Double.parseDouble(tps_str)));

                            int vol_1 = b[5] & 0xFF;
                            int vol_2 = b[6] & 0xFF;
                            int vol_total = ((vol_1 * 256) + vol_2);
                            String vol_str = String.valueOf(vol_total);
                            System.out.println("BT VOL: " +vol_str);
                            Platform.runLater(() -> gauge10.setValue(Double.parseDouble(vol_str)));

                            int fp_1 = b[7] & 0xFF;
                            int fp_2 = b[8] & 0xFF;
                            int fp_total = ((fp_1 * 256) + fp_2);
                            String fp_str = String.valueOf(fp_total);
                            System.out.println("FP: " +fp_str);
                            Platform.runLater(() -> gauge11.setValue(Double.parseDouble(fp_str)));
                        }

                        MenuItem Sub_Ports = new MenuItem(serialPort.getPortName());
                        Ports.getItems().add(Sub_Ports);

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

    public static void main(String[] args) {
        launch(args);
    }
}
