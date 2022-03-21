package fm.telemetrysoftware;

import java.util.logging.Level;
import java.util.logging.Logger;
import eu.hansolo.medusa.skins.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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

public class Controller extends Application {

    BorderPane layout = new BorderPane();

    final int NUM_OF_POINT = 50;
    XYChart.Series series1;
    XYChart.Series series2;

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
    Gauge gauge13 = new Gauge();
    Gauge gauge14 = new Gauge();

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
        comboBoxPorts.valueProperty().addListener(
                (ChangeListener<String>) (observable, oldValue, newValue) -> {
                    System.out.println(newValue);
                    disconnectArduino();
                    connectArduino(newValue);
                }
        );

        //LineChart
        final NumberAxis xAxis1 = new NumberAxis();
        final NumberAxis yAxis1 = new NumberAxis();
        final NumberAxis xAxis2 = new NumberAxis();
        final NumberAxis yAxis2 = new NumberAxis();
        xAxis1.setLabel("Time");
        yAxis1.setLabel("Value");
        xAxis2.setLabel("Time");
        yAxis2.setLabel("Value");
        final LineChart<Number,Number> lineChart1 = new LineChart<>(xAxis1,yAxis1);
        final LineChart<Number,Number> lineChart2 = new LineChart<>(xAxis2,yAxis2);
        series1 = new XYChart.Series();
        series1.setName("RPM");
        series2 = new XYChart.Series();
        series2.setName("OPS");
        lineChart1.getData().add(series1);
        lineChart2.getData().add(series2);
        lineChart1.setAnimated(false);
        lineChart2.setAnimated(false);

        for(int i=0; i<NUM_OF_POINT; i++){
            series1.getData().add(new XYChart.Data(i, 0));
            series2.getData().add(new XYChart.Data(i, 0));
        }

        File.getItems().add(Save);
        Tools.getItems().add(Ports);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        gauge1.setTitle("RPM");
        gauge1.setMinValue(0.00);
        gauge1.setMaxValue(1023.00);
        gauge1.setTickLabelsVisible(false);
        gauge1.setNeedleColor(Color.WHITE);
        gauge1.setUnitColor(Color.WHITE);
        gauge1.setValueColor(Color.WHITE);
        gauge1.setForegroundBaseColor(Color.WHITE);

        gauge2.setTitle("OPS");
        gauge2.setMinValue(0.00);
        gauge2.setMaxValue(1023.00);
        gauge2.setSkin(new LinearSkin(gauge2));
        gauge2.setOrientation(Orientation.VERTICAL);
        gauge2.setNeedleColor(Color.WHITE);
        gauge2.setUnitColor(Color.WHITE);
        gauge2.setValueColor(Color.WHITE);
        gauge2.setForegroundBaseColor(Color.WHITE);

        gauge3.setTitle("ECT 1");
        gauge3.setMinValue(0.00);
        gauge3.setMaxValue(1023.00);
        gauge3.setSkin(new LinearSkin(gauge3));
        gauge3.setOrientation(Orientation.VERTICAL);
        gauge3.setNeedleColor(Color.WHITE);
        gauge3.setUnitColor(Color.WHITE);
        gauge3.setValueColor(Color.WHITE);
        gauge3.setForegroundBaseColor(Color.WHITE);

        gauge4.setTitle("ECT 2");
        gauge4.setMinValue(0.00);
        gauge4.setMaxValue(1023.00);
        gauge4.setSkin(new LinearSkin(gauge4));
        gauge4.setOrientation(Orientation.VERTICAL);
        gauge4.setNeedleColor(Color.WHITE);
        gauge4.setUnitColor(Color.WHITE);
        gauge4.setValueColor(Color.WHITE);
        gauge4.setForegroundBaseColor(Color.WHITE);

        gauge5.setTitle("WS FR");
        gauge5.setMinValue(0.00);
        gauge5.setMaxValue(1023.00);
        gauge5.setTickLabelsVisible(false);
        gauge5.setNeedleColor(Color.WHITE);
        gauge5.setUnitColor(Color.WHITE);
        gauge5.setValueColor(Color.WHITE);
        gauge5.setForegroundBaseColor(Color.WHITE);

        gauge6.setTitle("WS FL");
        gauge6.setMinValue(0.00);
        gauge6.setMaxValue(1023.00);
        gauge6.setTickLabelsVisible(false);
        gauge6.setNeedleColor(Color.WHITE);
        gauge6.setUnitColor(Color.WHITE);
        gauge6.setValueColor(Color.WHITE);
        gauge6.setForegroundBaseColor(Color.WHITE);

        gauge7.setTitle("WS RR");
        gauge7.setMinValue(0.00);
        gauge7.setMaxValue(1023.00);
        gauge7.setTickLabelsVisible(false);
        gauge7.setNeedleColor(Color.WHITE);
        gauge7.setUnitColor(Color.WHITE);
        gauge7.setValueColor(Color.WHITE);
        gauge7.setForegroundBaseColor(Color.WHITE);

        gauge8.setTitle("WS RL");
        gauge8.setMinValue(0.00);
        gauge8.setMaxValue(1023.00);
        gauge8.setTickLabelsVisible(false);
        gauge8.setNeedleColor(Color.WHITE);
        gauge8.setUnitColor(Color.WHITE);
        gauge8.setValueColor(Color.WHITE);
        gauge8.setForegroundBaseColor(Color.WHITE);

        gauge9.setTitle("TPS");
        gauge9.setMinValue(0.00);
        gauge9.setMaxValue(1023.00);
        gauge9.setSkin(new LinearSkin(gauge9));
        gauge9.setOrientation(Orientation.VERTICAL);
        gauge9.setNeedleColor(Color.WHITE);
        gauge9.setUnitColor(Color.WHITE);
        gauge9.setValueColor(Color.WHITE);
        gauge9.setForegroundBaseColor(Color.WHITE);

        gauge10.setTitle("VOLTAGE");
        gauge10.setMinValue(0.00);
        gauge10.setMaxValue(1023.00);
        gauge10.setSkin(new LinearSkin(gauge10));
        gauge10.setOrientation(Orientation.VERTICAL);
        gauge10.setNeedleColor(Color.WHITE);
        gauge10.setUnitColor(Color.WHITE);
        gauge10.setValueColor(Color.WHITE);
        gauge10.setForegroundBaseColor(Color.WHITE);

        gauge11.setTitle("FP");
        gauge11.setMinValue(0.00);
        gauge11.setMaxValue(1023.00);
        gauge11.setSkin(new LinearSkin(gauge11));
        gauge11.setOrientation(Orientation.VERTICAL);
        gauge11.setNeedleColor(Color.WHITE);
        gauge11.setUnitColor(Color.WHITE);
        gauge11.setValueColor(Color.WHITE);
        gauge11.setForegroundBaseColor(Color.WHITE);

        gauge12.setTitle("BPS");
        gauge12.setMinValue(0.00);
        gauge12.setMaxValue(1023.00);
        gauge12.setSkin(new LinearSkin(gauge12));
        gauge12.setOrientation(Orientation.VERTICAL);
        gauge12.setNeedleColor(Color.WHITE);
        gauge12.setUnitColor(Color.WHITE);
        gauge12.setValueColor(Color.WHITE);
        gauge12.setForegroundBaseColor(Color.WHITE);

        gauge13.setTitle("EXTRA");
        gauge13.setMinValue(0.00);
        gauge13.setMaxValue(1023.00);
        gauge13.setTickLabelsVisible(false);
        gauge13.setSkin(new LinearSkin(gauge13));
        gauge13.setOrientation(Orientation.VERTICAL);
        gauge13.setNeedleColor(Color.WHITE);
        gauge13.setUnitColor(Color.WHITE);
        gauge13.setValueColor(Color.WHITE);
        gauge13.setForegroundBaseColor(Color.WHITE);

        gauge14.setTitle("EXTRA");
        gauge14.setMinValue(0.00);
        gauge14.setMaxValue(1023.00);
        gauge14.setTickLabelsVisible(false);
        gauge14.setNeedleColor(Color.WHITE);
        gauge14.setUnitColor(Color.WHITE);
        gauge14.setValueColor(Color.WHITE);
        gauge14.setForegroundBaseColor(Color.WHITE);

        GridPane gridpane1 = new GridPane();
        gridpane1.setBackground(new Background(new BackgroundFill(Color.valueOf("#1c1d1f"), CornerRadii.EMPTY, Insets.EMPTY)));

        GridPane gridpane2 = new GridPane();
        gridpane2.setPrefHeight(bounds.getHeight()/3);
        gridpane2.setBackground(new Background(new BackgroundFill(Color.valueOf("#1c1d1f"), CornerRadii.EMPTY, Insets.EMPTY)));

        ColumnConstraints column0 = new ColumnConstraints(bounds.getWidth()/10);
        column0.setHgrow(Priority.ALWAYS);
        ColumnConstraints column1 = new ColumnConstraints(bounds.getWidth()/10);
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints(bounds.getWidth()/10);
        column2.setHgrow(Priority.ALWAYS);
        ColumnConstraints column3 = new ColumnConstraints(bounds.getWidth()/10);
        column3.setHgrow(Priority.ALWAYS);
        ColumnConstraints column4 = new ColumnConstraints(bounds.getWidth()/10);
        column4.setHgrow(Priority.ALWAYS);
        ColumnConstraints column5 = new ColumnConstraints(bounds.getWidth()/10);
        column5.setHgrow(Priority.ALWAYS);
        ColumnConstraints column6 = new ColumnConstraints(bounds.getWidth()/10);
        column6.setHgrow(Priority.ALWAYS);
        ColumnConstraints column7 = new ColumnConstraints(bounds.getWidth()/10);
        column7.setHgrow(Priority.ALWAYS);
        ColumnConstraints column8 = new ColumnConstraints(bounds.getWidth()/10);
        column8.setHgrow(Priority.ALWAYS);
        ColumnConstraints column9 = new ColumnConstraints(bounds.getWidth()/10);
        column9.setHgrow(Priority.ALWAYS);

        RowConstraints row0 = new RowConstraints(bounds.getHeight()/3);
        row0.setVgrow(Priority.ALWAYS);
        RowConstraints row1 = new RowConstraints(bounds.getHeight()/3);
        row1.setVgrow(Priority.ALWAYS);
        RowConstraints row2 = new RowConstraints(bounds.getHeight()/3);
        row2.setVgrow(Priority.ALWAYS);

        gridpane1.getColumnConstraints().addAll(column0, column1, column2, column3, column4, column5, column6, column7, column8, column9);
        gridpane1.add(comboBoxPorts, 0, 0);
        gridpane1.add(gauge9, 0, 1);
        gridpane1.add(gauge12, 1, 1);
        gridpane1.add(gauge5, 2, 1, 2, 1);
        gridpane1.add(gauge6, 4, 1, 2, 1);
        gridpane1.add(gauge7, 6, 1, 2, 1);
        gridpane1.add(gauge8, 8, 1, 2, 1);
        gridpane1.add(gauge3, 0, 2);
        gridpane1.add(gauge4, 1, 2);
        gridpane1.add(gauge13, 2, 2);
        gridpane1.add(gauge11, 3, 2);
        gridpane1.add(gauge2, 4, 2);
        gridpane1.add(gauge10, 5, 2);
        gridpane1.add(gauge1, 6, 2, 2, 1);
        gridpane1.add(gauge14, 8, 2, 2, 1);

        RowConstraints row00 = new RowConstraints(bounds.getHeight());
        row00.setVgrow(Priority.ALWAYS);
        ColumnConstraints column00 = new ColumnConstraints(bounds.getWidth()/2);
        column00.setHgrow(Priority.ALWAYS);
        ColumnConstraints column1i = new ColumnConstraints(bounds.getWidth()/2);
        column1i.setHgrow(Priority.ALWAYS);

        gridpane2.getColumnConstraints().addAll(column00, column1i);
        gridpane2.add(lineChart1, 0, 0);
        gridpane2.add(lineChart2, 1, 0);

        FileInputStream imageStream = new FileInputStream("/Users/MahicShah/Library/CloudStorage/OneDrive-ManipalAcademyofHigherEducation/Formula Manipal/Software/JavaFX_Arduino_Live/Logo.png");
        Image image = new Image(imageStream);
        ImageView imageView = new ImageView(image);
        imageView.setX(170);
        imageView.setY(10);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        gridpane1.add(imageView, 9, 0);
        gridpane1.setHalignment(imageView, HPos.RIGHT);

        FileInputStream imageStream1 = new FileInputStream("/Users/MahicShah/Library/CloudStorage/OneDrive-ManipalAcademyofHigherEducation/Formula Manipal/Software/JavaFX_Arduino_Live/Transperant logo.png");
        Image image1 = new Image(imageStream1);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(File, Tools);

        layout.setTop(menuBar);
        layout.setCenter(gridpane1);
        layout.setBottom(gridpane2);

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

    public void shiftSeriesData1(float newValue)
    {
        for(int i=0; i<NUM_OF_POINT-1; i++){
            XYChart.Data<String, Number> ShiftDataUp = (XYChart.Data<String, Number>) series1.getData().get(i + 1);
            Number shiftValue = ShiftDataUp.getYValue();
            XYChart.Data<String, Number> ShiftDataDn = (XYChart.Data<String, Number>)series1.getData().get(i);
            ShiftDataDn.setYValue(shiftValue);
        }
        XYChart.Data<String, Number> lastData = (XYChart.Data<String, Number>)series1.getData().get(NUM_OF_POINT-1);
        lastData.setYValue(newValue);
    }

    public void shiftSeriesData2(float newValue2)
    {
        for(int i=0; i<NUM_OF_POINT-1; i++){
            XYChart.Data<String, Number> ShiftDataUp = (XYChart.Data<String, Number>)series2.getData().get(i+1);
            Number shiftValue = ShiftDataUp.getYValue();
            XYChart.Data<String, Number> ShiftDataDn = (XYChart.Data<String, Number>)series2.getData().get(i);
            ShiftDataDn.setYValue(shiftValue);
        }
        XYChart.Data<String, Number> lastData = (XYChart.Data<String, Number>)series2.getData().get(NUM_OF_POINT-1);
        lastData.setYValue(newValue2);
    }

    public void connectArduino(String port){
        System.out.println("Arduino is connected.");
        SerialPort serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if (serialPortEvent.isRXCHAR()) {
                    try {

                            byte[] l;

                            while (serialPort.isOpened()) {
                                l = serialPort.readBytes(11);
                                int x = l[0] & 0xFF;
                                int o = l[1] & 0xFF;
                                if (x == 5 & o == 240 || x == 5 & o == 241 || x == 5 & o == 242) {
                                    break;
                                }
                            }

                            byte[] b = serialPort.readBytes(11);

                            int y = b[1] & 0xFF;
                            int id = ((5 * 256) + y);

                            if (id == 1520) {
                                int rpm_1 = b[4] & 0xFF;
                                int rpm_2 = b[3] & 0xFF;
                                int rpm_total = ((rpm_1 * 256) + rpm_2);
                                Platform.runLater(() -> gauge1.setValue(rpm_total));

                                int ops_1 = b[6] & 0xFF;
                                int ops_2 = b[5] & 0xFF;
                                int ops_total = ((ops_1 * 256) + ops_2);
                                Platform.runLater(() -> gauge2.setValue(ops_total));

                                int ect1_1 = b[8] & 0xFF;
                                int ect1_2 = b[7] & 0xFF;
                                int ect1_total = ((ect1_1 * 256) + ect1_2);
                                Platform.runLater(() -> gauge3.setValue(ect1_total));

                                int ect2_1 = b[10] & 0xFF;
                                int ect2_2 = b[9] & 0xFF;
                                int ect2_total = ((ect2_1 * 256) + ect2_2);
                                Platform.runLater(() -> gauge4.setValue(ect2_total));

                                Platform.runLater(() -> {
                                    shiftSeriesData1((float) rpm_total);
                                    shiftSeriesData2((float) ops_total);
                                });
                            }

                            if (id == 1521) {
                                int wsfr_1 = b[4] & 0xFF;
                                int wsfr_2 = b[3] & 0xFF;
                                int wsfr_total = ((wsfr_1 * 256) + wsfr_2);
                                Platform.runLater(() -> gauge5.setValue(wsfr_total));

                                int wsfl_1 = b[6] & 0xFF;
                                int wsfl_2 = b[5] & 0xFF;
                                int wsfl_total = ((wsfl_1 * 256) + wsfl_2);
                                Platform.runLater(() -> gauge6.setValue(wsfl_total));

                                int wsrr_1 = b[8] & 0xFF;
                                int wsrr_2 = b[7] & 0xFF;
                                int wsrr_total = ((wsrr_1 * 256) + wsrr_2);
                                Platform.runLater(() -> gauge7.setValue(wsrr_total));

                                int wsrl_1 = b[10] & 0xFF;
                                int wsrl_2 = b[9] & 0xFF;
                                int wsrl_total = ((wsrl_1 * 256) + wsrl_2);
                                Platform.runLater(() -> gauge8.setValue(wsrl_total));
                            }

                            if (id == 1522) {
                                int tps_1 = b[4] & 0xFF;
                                int tps_2 = b[3] & 0xFF;
                                int tps_total = ((tps_1 * 256) + tps_2);
                                Platform.runLater(() -> gauge9.setValue(tps_total));

                                int vol_1 = b[6] & 0xFF;
                                int vol_2 = b[5] & 0xFF;
                                int vol_total = ((vol_1 * 256) + vol_2);
                                Platform.runLater(() -> gauge10.setValue(vol_total));

                                int fp_1 = b[8] & 0xFF;
                                int fp_2 = b[7] & 0xFF;
                                int fp_total = ((fp_1 * 256) + fp_2);
                                Platform.runLater(() -> gauge11.setValue(fp_total));
                            }

                            System.out.println("b[0] = " + (b[0] & 0xFF));
                            System.out.println("b[1] = " + (b[1] & 0xFF));
                            System.out.println("b[2] = " + (b[2] & 0xFF));
                            System.out.println("b[3] = " + (b[3] & 0xFF));
                            System.out.println("b[4] = " + (b[4] & 0xFF));
                            System.out.println("b[5] = " + (b[5] & 0xFF));
                            System.out.println("b[6] = " + (b[6] & 0xFF));
                            System.out.println("b[7] = " + (b[7] & 0xFF));
                            System.out.println("b[8] = " + (b[8] & 0xFF));
                            System.out.println("b[9] = " + (b[9] & 0xFF));
                            System.out.println();

                            MenuItem Sub_Ports = new MenuItem(serialPort.getPortName());
                            Ports.getItems().add(Sub_Ports);


                        } catch(SerialPortException ex){
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
            });

            arduinoPort = serialPort;
        } catch (SerialPortException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SerialPortException: " + ex);
        }
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
                Logger.getLogger(Controller.class.getName())
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
