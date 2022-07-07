package fm.telemetrysoftware;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.hansolo.medusa.FGauge;
import eu.hansolo.medusa.skins.*; //Import the jar file of Medusa for gauges.
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jssc.*;
import static jssc.SerialPort.MASK_RXCHAR;
import eu.hansolo.medusa.Gauge;
import java.io.*;

public class Controller extends Application {

    BorderPane layout = new BorderPane(); //Creating a BorderPane as the base pane, on top of which different panes have been placed.

    Label label = new Label();

    Button button1 = new Button("Gauges");
    Button button2 = new Button("Line Charts");
    Button button3 = new Button();
    Button button4 = new Button();

    final int NUM_OF_POINT = 75; //Set the limit of points visible in the line chart at once.
    XYChart.Series series1; //Construct the 1st Series for the 1st line chart.
    XYChart.Series series2; //Construct the 2nd Series for the 2nd line chart.
    XYChart.Series series3;
    XYChart.Series series4;

    Gauge gauge1 = new Gauge(); //Initialize all the gauges.
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

    Label gear = new Label();
    Label gear_text = new Label();

    Menu File = new Menu("File"); //Creating menu bar ports.
    Menu Save = new Menu("Save");
    Menu Tools = new Menu("Tools");
    Menu Ports = new Menu("Ports");

    SerialPort XBeePort = null; //Initializing the serial port of XBee, and setting it null.
    ObservableList<String> portList; //Initialing the port list.

    private void detectPort() { //Method to detect all serial ports connected.
        portList = FXCollections.observableArrayList();
        String[] serialPortNames = SerialPortList.getPortNames();
        for (String name : serialPortNames) {
            System.out.println(name);
            portList.add(name);
        }
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException { //Main method.

        detectPort();
        final ComboBox comboBoxPorts = new ComboBox(portList); //Creating a new ComboBox to display the serial ports.
        comboBoxPorts.setPromptText("Available Ports"); //Adding a prompt text to the ComboBox.
        comboBoxPorts.valueProperty().addListener( //Code to check for active serial ports.
                (ChangeListener<String>) (observable, oldValue, newValue) -> {
                    System.out.println(newValue);
                    disconnectXBee();
                    connectXBee(newValue);
                }
        );

        final NumberAxis xAxis1 = new NumberAxis(); //Initializing the x axis of the first line chart.
        final NumberAxis yAxis1 = new NumberAxis(); //Initializing the y axis of the first line chart.
        final NumberAxis xAxis2 = new NumberAxis(); //Initializing the x axis of the second line chart.
        final NumberAxis yAxis2 = new NumberAxis(); //Initializing the y axis of the second line chart.
        final NumberAxis xAxis3 = new NumberAxis();
        final NumberAxis yAxis3 = new NumberAxis();
        final NumberAxis xAxis4 = new NumberAxis();
        final NumberAxis yAxis4 = new NumberAxis();
        xAxis1.setLabel("Time"); //Setting the name of the x axis of the fist line chart.
        yAxis1.setLabel("Data"); //Setting the name of the y axis of the fist line chart.
        xAxis2.setLabel("Time"); //Setting the name of the x axis of the second line chart.
        yAxis2.setLabel("OPS"); //Setting the name of the y axis of the second line chart.
        xAxis3.setLabel("Time");
        yAxis3.setLabel("TPS");
        xAxis4.setLabel("Time");
        yAxis4.setLabel("Extra");
//        xAxis1.setTickLabelFill(Color.WHITE);
//        xAxis2.setTickLabelFill(Color.WHITE);
//        xAxis3.setTickLabelFill(Color.WHITE);
//        xAxis4.setTickLabelFill(Color.WHITE);
//        yAxis1.setTickLabelFill(Color.WHITE);
//        yAxis2.setTickLabelFill(Color.WHITE);
//        yAxis3.setTickLabelFill(Color.WHITE);
//        yAxis4.setTickLabelFill(Color.WHITE);
        final LineChart<Number,Number> lineChart1 = new LineChart<>(xAxis1,yAxis1); //Initializing the first line chart.
//        BackgroundFill background_fill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);
//        Background background = new Background(background_fill);
//        lineChart1.setBackground(background);
        final LineChart<Number,Number> lineChart2 = new LineChart<>(xAxis2,yAxis2); //Initializing the second line chart.
        final LineChart<Number,Number> lineChart3 = new LineChart<>(xAxis3,yAxis3);
        final LineChart<Number,Number> lineChart4 = new LineChart<>(xAxis4,yAxis4);
        series1 = new XYChart.Series(); //Initialing series for first line chart.
        series1.setName("TPS"); //Naming the series for the first line chart as RPM.

//        Platform.runLater(() -> {
//            Set<Node> nodes = lineChart1.lookupAll(".series" + 0);
//            for (Node n : nodes) {
//                n.setStyle("-fx-background-color: black, white;\n" + "-fx-background-insets: 0, 2;\n" + "-fx-background-radius: 5px;\n" + "-fx-padding: 5px;");
//            }
//            series1.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: black;");
//        });

        series2 = new XYChart.Series(); //Initialing series for second line chart.
        series2.setName("BPS"); //Naming the series for the second line chart as OPS.
        series3 = new XYChart.Series();
        series3.setName("RPM");
        series4 = new XYChart.Series();
        series4.setName("Battery Voltage");
        lineChart1.getData().addAll(series1, series2); //Adding series1 to the first line chart.
        lineChart2.getData().add(series2); //Adding series2 to the second line chart.
        lineChart3.getData().add(series3);
        lineChart4.getData().add(series4);
        lineChart1.setAnimated(false); //Disabling animation for the first line chart.
        lineChart2.setAnimated(false); //Disabling animation for the second line chart.
        lineChart3.setAnimated(false);
        lineChart4.setAnimated(false);
        lineChart1.setCreateSymbols(false); //Hiding data point symbols in the first line chart.
        lineChart2.setCreateSymbols(false); //Hiding data point symbols in the second line chart.
        lineChart3.setCreateSymbols(false);
        lineChart4.setCreateSymbols(false);


        for(int i=0; i<NUM_OF_POINT; i++) {
            series1.getData().add(new XYChart.Data(i, 0)); //Preload the first line chart with 0.
            series2.getData().add(new XYChart.Data(i, 0)); //Preload the second line chart with 0.
            series3.getData().add(new XYChart.Data(i, 0));
            series4.getData().add(new XYChart.Data(i, 0));
        }

        File.getItems().add(Save); //Adding 'Save' under 'File' in the menu bar.
        Tools.getItems().add(Ports); //Adding 'Ports' under 'Tools' in the menu bar.

        Screen screen = Screen.getPrimary(); //Creating a screen.
        Rectangle2D bounds = screen.getVisualBounds(); //Getting dimensions of the user's screen.

//        GridPane opsgp = new GridPane();
//        GridPane ect1gp = new GridPane();
//        GridPane ect2gp = new GridPane();
//        GridPane speedgp = new GridPane();
//        GridPane tpsgp = new GridPane();
//        GridPane bpsgp = new GridPane();
//        GridPane volgp = new GridPane();
//        GridPane fpsgp = new GridPane();
//        GridPane geargp = new GridPane();


        gauge1.setTitle("RPM"); //Gauge 1 settings
        gauge1.setMajorTickSpace(1000);
        gauge1.setMinorTickSpace(100);
        gauge1.setMinValue(0.00);
        gauge1.setMaxValue(1023.00);

        gauge2.setTitle("OPS (psi)"); //Gauge 2 settings
        gauge2.setMinValue(0.00);
        gauge2.setMaxValue(1023.00);
        gauge2.setSkin(new LinearSkin(gauge2));
        gauge2.setOrientation(Orientation.VERTICAL);
//        gauge2.setNeedleColor(Color.WHITE);
//        gauge2.setUnitColor(Color.WHITE);
//        gauge2.setValueColor(Color.WHITE);
//        gauge2.setForegroundBaseColor(Color.WHITE);

        gauge3.setTitle("ECT 1 (°C)"); //Gauge 3 settings
        gauge3.setMinValue(0.00);
        gauge3.setMaxValue(1023.00);
        gauge3.setSkin(new LinearSkin(gauge3));
        gauge3.setOrientation(Orientation.VERTICAL);
//        gauge3.setNeedleColor(Color.WHITE);
//        gauge3.setUnitColor(Color.WHITE);
//        gauge3.setValueColor(Color.WHITE);
//        gauge3.setForegroundBaseColor(Color.WHITE);

        gauge4.setTitle("ECT 2 (°C)"); //Gauge 4 settings
        gauge4.setMinValue(0.00);
        gauge4.setMaxValue(1023.00);
        gauge4.setSkin(new LinearSkin(gauge4));
        gauge4.setOrientation(Orientation.VERTICAL);
//        gauge4.setNeedleColor(Color.WHITE);
//        gauge4.setUnitColor(Color.WHITE);
//        gauge4.setValueColor(Color.WHITE);
//        gauge4.setForegroundBaseColor(Color.WHITE);

        gauge5.setTitle("WS FR"); //Gauge 5 settings
        gauge5.setMinValue(0.00);
        gauge5.setMaxValue(1023.00);
        gauge5.setTickLabelsVisible(false);
//        gauge5.setNeedleColor(Color.WHITE);
//        gauge5.setUnitColor(Color.WHITE);
//        gauge5.setValueColor(Color.WHITE);
//        gauge5.setForegroundBaseColor(Color.WHITE);

        gauge6.setTitle("WS FL"); //Gauge 6 settings
        gauge6.setMinValue(0.00);
        gauge6.setMaxValue(1023.00);
        gauge6.setTickLabelsVisible(false);
//        gauge6.setNeedleColor(Color.WHITE);
//        gauge6.setUnitColor(Color.WHITE);
//        gauge6.setValueColor(Color.WHITE);
//        gauge6.setForegroundBaseColor(Color.WHITE);

        gauge7.setTitle("WS RR"); //Gauge 7 settings
        gauge7.setMinValue(0.00);
        gauge7.setMaxValue(1023.00);
        gauge7.setTickLabelsVisible(false);
//        gauge7.setNeedleColor(Color.WHITE);
//        gauge7.setUnitColor(Color.WHITE);
//        gauge7.setValueColor(Color.WHITE);
//        gauge7.setForegroundBaseColor(Color.WHITE);

        gauge8.setTitle("WS RL"); //Gauge 8 settings
        gauge8.setMinValue(0.00);
        gauge8.setMaxValue(1023.00);
        gauge8.setTickLabelsVisible(false);
//        gauge8.setNeedleColor(Color.WHITE);
//        gauge8.setUnitColor(Color.WHITE);
//        gauge8.setValueColor(Color.WHITE);
//        gauge8.setForegroundBaseColor(Color.WHITE);

        gauge9.setTitle("TPS (%)"); //Gauge 9 settings
        gauge9.setMinValue(0.00);
        gauge9.setMaxValue(1023.00);
        gauge9.setSkin(new LinearSkin(gauge9));
        gauge9.setOrientation(Orientation.HORIZONTAL);
//        gauge9.setNeedleColor(Color.WHITE);
//        gauge9.setUnitColor(Color.WHITE);
//        gauge9.setValueColor(Color.WHITE);
//        gauge9.setForegroundBaseColor(Color.WHITE);

        gauge10.setTitle("BATTERY (V)"); //Gauge 10 settings
        gauge10.setMinValue(0.00);
        gauge10.setMaxValue(1023.00);
        gauge10.setSkin(new LinearSkin(gauge10));
        gauge10.setOrientation(Orientation.VERTICAL);
        gauge10.setTickLabelsVisible(true);
//        gauge10.setNeedleColor(Color.WHITE);
//        gauge10.setUnitColor(Color.WHITE);
//        gauge10.setValueColor(Color.WHITE);
//        gauge10.setForegroundBaseColor(Color.WHITE);

        gauge11.setTitle("FPS (psi)"); //Gauge 11 settings
        gauge11.setMinValue(0.00);
        gauge11.setMaxValue(1023.00);
        gauge11.setSkin(new LinearSkin(gauge11));
        gauge11.setOrientation(Orientation.VERTICAL);
//        gauge11.setNeedleColor(Color.WHITE);
//        gauge11.setUnitColor(Color.WHITE);
//        gauge11.setValueColor(Color.WHITE);
//        gauge11.setForegroundBaseColor(Color.WHITE);

        gauge12.setTitle("BPS (bar)"); //Gauge 12 settings
        gauge12.setMinValue(0.00);
        gauge12.setMaxValue(1023.00); //83 is max pressure acc to VD.
        gauge12.setSkin(new LinearSkin(gauge12));
        gauge12.setOrientation(Orientation.HORIZONTAL);
//        gauge12.setNeedleColor(Color.WHITE);
//        gauge12.setUnitColor(Color.WHITE);
//        gauge12.setValueColor(Color.WHITE);
//        gauge12.setForegroundBaseColor(Color.WHITE);

        gauge13.setTitle("SPEED (km/h)"); //Gauge 13 settings
        gauge13.setMinValue(0.00);
        gauge13.setMaxValue(1023.00);
        gauge13.setMajorTickSpace(100);
        gauge13.setMinorTickSpace(10);
//        gauge13.setNeedleColor(Color.WHITE);
//        gauge13.setUnitColor(Color.WHITE);
//        gauge13.setValueColor(Color.WHITE);
//        gauge13.setForegroundBaseColor(Color.WHITE);

        gauge14.setTitle("GEAR"); //Gauge 14 settings
        gauge14.setSkin(new NasaSkin(gauge14));
        gauge14.setMinValue(0.00);
        gauge14.setMaxValue(1023.00);
        gauge14.setTickLabelsVisible(false);
//        gauge14.setNeedleColor(Color.WHITE);
//        gauge14.setUnitColor(Color.WHITE);
//        gauge14.setValueColor(Color.WHITE);
//        gauge14.setForegroundBaseColor(Color.WHITE);

        gear.setFont(new Font("Arial", 150));
        gear.setText("0");

        gear_text.setFont(new Font("Arial", 75));
        gear_text.setText("Gear");

        GridPane gridpane1 = new GridPane(); //Creating a GridPane for the gauges.
        gridpane1.setPrefHeight(bounds.getHeight());
        //gridpane1.setGridLinesVisible(true);
        //gridpane1.setBackground(new Background(new BackgroundFill(Color.valueOf("#1c1d1f"), CornerRadii.EMPTY, Insets.EMPTY))); //Set the background colour as dark grey.

        GridPane gridpane2 = new GridPane(); //Creating a GridPane for the line charts.
        //gridpane2.setGridLinesVisible(true);
        //gridpane2.setBackground(new Background(new BackgroundFill(Color.valueOf("#1c1d1f"), CornerRadii.EMPTY, Insets.EMPTY))); //Set the background colour as dark grey.

        GridPane buttonsbar = new GridPane();
        buttonsbar.setPrefHeight(bounds.getHeight()/30);
        //buttonsbar.setBackground(new Background(new BackgroundFill(Color.valueOf("#1c1d1f"), CornerRadii.EMPTY, Insets.EMPTY)));

        GridPane popup_gridpane = new GridPane();

//        GridPane gridpane3 = new GridPane();
//        buttonsbar.setBackground(new Background(new BackgroundFill(Color.valueOf("#1c1d1f"), CornerRadii.EMPTY, Insets.EMPTY)));

//        Text RPM = new Text("RPM");
//        Text OPS = new Text("OPS");
//        Text ECT_1 = new Text("ECT 1");
//        Text ECT_2 = new Text("ECT 2");
//        Text WS_FR = new Text("WS FR");
//        Text WS_FL = new Text("WS FL");
//        Text WS_RR = new Text("WS RR");
//        Text WS_RL = new Text("WS RL");
//        Text TPS = new Text("TPS");
//        Text VOLTAGE = new Text("VOLTAGE");
//        Text FP = new Text("FP");
//        Text BPS = new Text("BPS");
//        Text EXTRA_1 = new Text("EXTRA");
//        Text EXTRA_2 = new Text("EXTRA");
//
//        ListView listView = new ListView();
//        listView.getItems().add(RPM);
//        listView.getItems().add(OPS);
//        listView.getItems().add(ECT_1);
//        listView.getItems().add(ECT_2);
//        listView.getItems().add(WS_FR);
//        listView.getItems().add(WS_FL);
//        listView.getItems().add(WS_RR);
//        listView.getItems().add(WS_RL);
//        listView.getItems().add(TPS);
//        listView.getItems().add(VOLTAGE);
//        listView.getItems().add(FP);
//        listView.getItems().add(BPS);
//        listView.getItems().add(EXTRA_1);
//        listView.getItems().add(EXTRA_2);

//        GridPane dummy = new GridPane();
//        dummy.setPrefHeight(0);
//        dummy.setBackground(new Background(new BackgroundFill(Color.valueOf("#1c1d1f"), CornerRadii.EMPTY, Insets.EMPTY)));

        ColumnConstraints column0 = new ColumnConstraints(bounds.getWidth()/8);
        column0.setHgrow(Priority.ALWAYS);
        ColumnConstraints column1 = new ColumnConstraints(bounds.getWidth()/8);
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints(bounds.getWidth()/8);
        column2.setHgrow(Priority.ALWAYS);
        ColumnConstraints column3 = new ColumnConstraints(bounds.getWidth()/8);
        column3.setHgrow(Priority.ALWAYS);
        ColumnConstraints column4 = new ColumnConstraints(bounds.getWidth()/8);
        column4.setHgrow(Priority.ALWAYS);
        ColumnConstraints column5 = new ColumnConstraints(bounds.getWidth()/8);
        column5.setHgrow(Priority.ALWAYS);
        ColumnConstraints column6 = new ColumnConstraints(bounds.getWidth()/8);
        column6.setHgrow(Priority.ALWAYS);
        ColumnConstraints column7 = new ColumnConstraints(bounds.getWidth()/8);
        column7.setHgrow(Priority.ALWAYS);

        RowConstraints row0 = new RowConstraints(bounds.getHeight()*0.225);
        row0.setVgrow(Priority.ALWAYS);
        RowConstraints row1 = new RowConstraints(bounds.getHeight()*0.225);
        row1.setVgrow(Priority.ALWAYS);
        RowConstraints row2 = new RowConstraints(bounds.getHeight()*0.45);
        row2.setVgrow(Priority.ALWAYS);

        gridpane1.getRowConstraints().addAll(row0, row1, row2);
        gridpane1.getColumnConstraints().addAll(column0, column1, column2, column3, column4, column5, column6, column7);
        gridpane1.add(gauge13, 0, 0, 2, 2);
        gridpane1.add(gauge1, 2, 0, 2, 2);
        gridpane1.add(gauge9, 4, 0, 2, 1);
        gridpane1.add(gauge12, 4, 1, 2, 1);
        gridpane1.add(gear_text, 6, 0, 2, 1);
        gridpane1.add(gear, 6, 1, 2, 1);
        gridpane1.add(gauge3, 0, 2, 1, 1);
        gridpane1.add(gauge4, 1, 2, 1, 1);
        gridpane1.add(gauge11, 2, 2, 1, 1);
        gridpane1.add(gauge2, 3, 2, 1, 1);
        gridpane1.add(gauge10, 4, 2, 1, 1);
        gridpane1.add(lineChart1, 5, 2, 3, 1);
//        gridpane1.setGridLinesVisible(true);
        gridpane1.setHalignment(gear, HPos.CENTER);
        gridpane1.setHalignment(gear_text, HPos.CENTER);

        RowConstraints row00 = new RowConstraints(bounds.getHeight()*0.22); //Rows for the line chart GridPane.
        row00.setVgrow(Priority.ALWAYS);
        RowConstraints row1i = new RowConstraints(bounds.getHeight()*0.22);
        row1i.setVgrow(Priority.ALWAYS);
        RowConstraints row2i = new RowConstraints(bounds.getHeight()*0.22);
        row2i.setVgrow(Priority.ALWAYS);
        RowConstraints row3i = new RowConstraints(bounds.getHeight()*0.22);
        row3i.setVgrow(Priority.ALWAYS);
        ColumnConstraints column00 = new ColumnConstraints(bounds.getWidth()); //Columns for the line chart GridPane.
        column00.setHgrow(Priority.ALWAYS);

        ColumnConstraints columnb0 = new ColumnConstraints(bounds.getWidth()/20); //Columns for the buttonsbar GridPane.
        columnb0.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb1 = new ColumnConstraints(bounds.getWidth()/20);
        columnb1.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb2 = new ColumnConstraints(bounds.getWidth()/20);
        columnb2.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb3 = new ColumnConstraints(bounds.getWidth()/20);
        columnb3.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb4 = new ColumnConstraints(bounds.getWidth()/20);
        columnb4.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb5 = new ColumnConstraints(bounds.getWidth()/20);
        columnb5.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb6 = new ColumnConstraints(bounds.getWidth()/20);
        columnb6.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb7 = new ColumnConstraints(bounds.getWidth()/20);
        columnb7.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb8 = new ColumnConstraints(bounds.getWidth()/20);
        columnb8.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb9 = new ColumnConstraints(bounds.getWidth()/20);
        columnb9.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb10 = new ColumnConstraints(bounds.getWidth()/20); //Columns for the buttonsbar GridPane.
        columnb10.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb11 = new ColumnConstraints(bounds.getWidth()/20);
        columnb11.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb12 = new ColumnConstraints(bounds.getWidth()/20);
        columnb12.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb13 = new ColumnConstraints(bounds.getWidth()/20);
        columnb13.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb14 = new ColumnConstraints(bounds.getWidth()/20);
        columnb14.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb15 = new ColumnConstraints(bounds.getWidth()/20);
        columnb15.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb16 = new ColumnConstraints(bounds.getWidth()/20);
        columnb16.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb17 = new ColumnConstraints(bounds.getWidth()/20);
        columnb17.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb18 = new ColumnConstraints(bounds.getWidth()/20);
        columnb18.setHgrow(Priority.ALWAYS);
        ColumnConstraints columnb19 = new ColumnConstraints(bounds.getWidth()/20);
        columnb19.setHgrow(Priority.ALWAYS);

        gridpane2.getColumnConstraints().addAll(column00);
        gridpane2.getRowConstraints().addAll(row00, row1i, row2i, row3i);
        gridpane2.add(lineChart4, 0, 0);
        gridpane2.add(lineChart2, 0, 1);
        gridpane2.add(lineChart3, 0, 2);
//        gridpane2.add(lineChart4, 0, 3);

        PopupControl popup = new PopupControl();
        popup.getScene().setRoot(gridpane1);

        buttonsbar.getColumnConstraints().addAll(columnb0, columnb1, columnb2, columnb3, columnb4, columnb5, columnb6, columnb7, columnb8, columnb9, columnb10, columnb11, columnb12, columnb13, columnb14, columnb15, columnb16, columnb17, columnb18, columnb19);
        buttonsbar.add(comboBoxPorts,0,0);
        buttonsbar.add(button3,2,0);
        buttonsbar.add(button4,3,0);
        buttonsbar.add(label, 6,0, 1, 3);
        buttonsbar.add(button1,9,0);
        buttonsbar.add(button2,10,0, 1, 2);
        buttonsbar.setHalignment(comboBoxPorts, HPos.RIGHT);
        buttonsbar.setHalignment(button1, HPos.CENTER);
        buttonsbar.setHalignment(button2, HPos.CENTER);
        buttonsbar.setHalignment(button3, HPos.RIGHT);
        buttonsbar.setHalignment(button4, HPos.CENTER);

        FileInputStream imageStream = new FileInputStream("/Users/MahicShah/Downloads/FM_Software/telemetry-software/Images/FM_Black.png");
        Image image = new Image(imageStream);
        ImageView imageView = new ImageView(image);
        imageView.setX(100);
        imageView.setY(5);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        buttonsbar.add(imageView, 19, 0);
        buttonsbar.setHalignment(imageView, HPos.RIGHT);

        FileInputStream imageStream1 = new FileInputStream("/Users/MahicShah/Downloads/FM_Software/telemetry-software/Images/FM_Logo.png");
        Image image1 = new Image(imageStream1);

        FileInputStream imageStream4 = new FileInputStream("/Users/MahicShah/Downloads/FM_Software/telemetry-software/Images/light-mode-black.png");
        Image image4 = new Image(imageStream4);
        ImageView imageView4 = new ImageView(image4);
        imageView4.setFitWidth(20);
        imageView4.setPreserveRatio(true);
        button3.setGraphic(imageView4);

        FileInputStream imageStream5 = new FileInputStream("/Users/MahicShah/Downloads/FM_Software/telemetry-software/Images/dark-mode-black.png");
        Image image5 = new Image(imageStream5);
        ImageView imageView5 = new ImageView(image5);
        imageView5.setFitWidth(20);
        imageView5.setPreserveRatio(true);
        button4.setGraphic(imageView5);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(File, Tools);

        layout.setTop(buttonsbar);
        layout.setCenter(gridpane1);
        //layout.setBottom(dummy);

        // Create ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Log Sensor Data");
        item1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                label.setText("Menu Item 1 Selected");
            }
        });
        MenuItem item2 = new MenuItem("Menu Item 2");
        item2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                label.setText("Menu Item 2 Selected");
            }
        });

        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(item1, item2);

        // When user right-click on Circle
        gauge9.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

            @Override
            public void handle(ContextMenuEvent event) {

                contextMenu.show(gauge9, event.getScreenX(), event.getScreenY());
            }
        });

        Scene scene = new Scene(layout);

        button1.setOnAction(e -> layout.setCenter(gridpane1));
        button2.setOnAction(e -> layout.setCenter(gridpane2));

        button3.setOnAction(e -> {

            gauge1.setNeedleColor(Color.RED);
            gauge1.setUnitColor(Color.BLACK);
            gauge1.setValueColor(Color.BLACK);
            gauge1.setForegroundBaseColor(Color.BLACK);

            gauge2.setNeedleColor(Color.RED);
            gauge2.setUnitColor(Color.BLACK);
            gauge2.setValueColor(Color.BLACK);
            gauge2.setForegroundBaseColor(Color.BLACK);

            gauge3.setNeedleColor(Color.RED);
            gauge3.setUnitColor(Color.BLACK);
            gauge3.setValueColor(Color.BLACK);
            gauge3.setForegroundBaseColor(Color.BLACK);

            gauge4.setNeedleColor(Color.RED);
            gauge4.setUnitColor(Color.BLACK);
            gauge4.setValueColor(Color.BLACK);
            gauge4.setForegroundBaseColor(Color.BLACK);

            gauge5.setNeedleColor(Color.RED);
            gauge5.setUnitColor(Color.BLACK);
            gauge5.setValueColor(Color.BLACK);
            gauge5.setForegroundBaseColor(Color.BLACK);

            gauge6.setNeedleColor(Color.RED);
            gauge6.setUnitColor(Color.BLACK);
            gauge6.setValueColor(Color.BLACK);
            gauge6.setForegroundBaseColor(Color.BLACK);

            gauge7.setNeedleColor(Color.RED);
            gauge7.setUnitColor(Color.BLACK);
            gauge7.setValueColor(Color.BLACK);
            gauge7.setForegroundBaseColor(Color.BLACK);

            gauge8.setNeedleColor(Color.RED);
            gauge8.setUnitColor(Color.BLACK);
            gauge8.setValueColor(Color.BLACK);
            gauge8.setForegroundBaseColor(Color.BLACK);

            gauge9.setNeedleColor(Color.RED);
            gauge9.setUnitColor(Color.BLACK);
            gauge9.setValueColor(Color.BLACK);
            gauge9.setForegroundBaseColor(Color.BLACK);

            gauge10.setNeedleColor(Color.RED);
            gauge10.setUnitColor(Color.BLACK);
            gauge10.setValueColor(Color.BLACK);
            gauge10.setForegroundBaseColor(Color.BLACK);

            gauge11.setNeedleColor(Color.RED);
            gauge11.setUnitColor(Color.BLACK);
            gauge11.setValueColor(Color.BLACK);
            gauge11.setForegroundBaseColor(Color.BLACK);

            gauge12.setNeedleColor(Color.RED);
            gauge12.setUnitColor(Color.BLACK);
            gauge12.setValueColor(Color.BLACK);
            gauge12.setForegroundBaseColor(Color.BLACK);

            gauge13.setNeedleColor(Color.RED);
            gauge13.setUnitColor(Color.BLACK);
            gauge13.setValueColor(Color.BLACK);
            gauge13.setForegroundBaseColor(Color.BLACK);

            gauge14.setNeedleColor(Color.RED);
            gauge14.setUnitColor(Color.BLACK);
            gauge14.setValueColor(Color.BLACK);
            gauge14.setForegroundBaseColor(Color.BLACK);

            gear.setTextFill(Color.BLACK);
            gear_text.setTextFill(Color.BLACK);


            gridpane1.setBackground(new Background(new BackgroundFill(Color.valueOf("#f4f4f4"), CornerRadii.EMPTY, Insets.EMPTY)));
            gridpane2.setBackground(new Background(new BackgroundFill(Color.valueOf("#f4f4f4"), CornerRadii.EMPTY, Insets.EMPTY)));
            buttonsbar.setBackground(new Background(new BackgroundFill(Color.valueOf("#f4f4f4"), CornerRadii.EMPTY, Insets.EMPTY)));

            try {
                FileInputStream imageStream2 = new FileInputStream("/Users/MahicShah/Downloads/FM_Software/telemetry-software/Images/FM_Black.png");
                Image image2 = new Image(imageStream2);
                ImageView imageView2 = new ImageView(image2);
                imageView2.setX(100);
                imageView2.setY(5);
                imageView2.setFitWidth(150);
                imageView2.setPreserveRatio(true);
                buttonsbar.add(imageView2, 19, 0);
                buttonsbar.setHalignment(imageView2, HPos.RIGHT);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        button4.setOnAction(e -> {

            gauge1.setNeedleColor(Color.RED);
            gauge1.setUnitColor(Color.WHITE);
            gauge1.setValueColor(Color.WHITE);
            gauge1.setForegroundBaseColor(Color.WHITE);

            gauge2.setNeedleColor(Color.RED);
            gauge2.setUnitColor(Color.WHITE);
            gauge2.setValueColor(Color.WHITE);
            gauge2.setForegroundBaseColor(Color.WHITE);

            gauge3.setNeedleColor(Color.RED);
            gauge3.setUnitColor(Color.WHITE);
            gauge3.setValueColor(Color.WHITE);
            gauge3.setForegroundBaseColor(Color.WHITE);

            gauge4.setNeedleColor(Color.RED);
            gauge4.setUnitColor(Color.WHITE);
            gauge4.setValueColor(Color.WHITE);
            gauge4.setForegroundBaseColor(Color.WHITE);

            gauge5.setNeedleColor(Color.RED);
            gauge5.setUnitColor(Color.WHITE);
            gauge5.setValueColor(Color.WHITE);
            gauge5.setForegroundBaseColor(Color.WHITE);

            gauge6.setNeedleColor(Color.RED);
            gauge6.setUnitColor(Color.WHITE);
            gauge6.setValueColor(Color.WHITE);
            gauge6.setForegroundBaseColor(Color.WHITE);

            gauge7.setNeedleColor(Color.RED);
            gauge7.setUnitColor(Color.WHITE);
            gauge7.setValueColor(Color.WHITE);
            gauge7.setForegroundBaseColor(Color.WHITE);

            gauge8.setNeedleColor(Color.RED);
            gauge8.setUnitColor(Color.WHITE);
            gauge8.setValueColor(Color.WHITE);
            gauge8.setForegroundBaseColor(Color.WHITE);

            gauge9.setNeedleColor(Color.RED);
            gauge9.setUnitColor(Color.WHITE);
            gauge9.setValueColor(Color.WHITE);
            gauge9.setForegroundBaseColor(Color.WHITE);

            gauge10.setNeedleColor(Color.RED);
            gauge10.setUnitColor(Color.WHITE);
            gauge10.setValueColor(Color.WHITE);
            gauge10.setForegroundBaseColor(Color.WHITE);

            gauge11.setNeedleColor(Color.RED);
            gauge11.setUnitColor(Color.WHITE);
            gauge11.setValueColor(Color.WHITE);
            gauge11.setForegroundBaseColor(Color.WHITE);

            gauge12.setNeedleColor(Color.RED);
            gauge12.setUnitColor(Color.WHITE);
            gauge12.setValueColor(Color.WHITE);
            gauge12.setForegroundBaseColor(Color.WHITE);

            gauge13.setNeedleColor(Color.RED);
            gauge13.setUnitColor(Color.WHITE);
            gauge13.setValueColor(Color.WHITE);
            gauge13.setForegroundBaseColor(Color.WHITE);

            gauge14.setNeedleColor(Color.RED);
            gauge14.setUnitColor(Color.WHITE);
            gauge14.setValueColor(Color.WHITE);
            gauge14.setForegroundBaseColor(Color.WHITE);

            gear.setTextFill(Color.WHITE);
            gear_text.setTextFill(Color.WHITE);


            gridpane1.setBackground(new Background(new BackgroundFill(Color.valueOf("#1c1d1f"), CornerRadii.EMPTY, Insets.EMPTY)));
            gridpane2.setBackground(new Background(new BackgroundFill(Color.valueOf("#1c1d1f"), CornerRadii.EMPTY, Insets.EMPTY)));
            buttonsbar.setBackground(new Background(new BackgroundFill(Color.valueOf("#1c1d1f"), CornerRadii.EMPTY, Insets.EMPTY)));
            popup_gridpane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

            try {
                FileInputStream imageStream3 = new FileInputStream("/Users/MahicShah/Downloads/FM_Software/telemetry-software/Images/FM_White.png");
                Image image3 = new Image(imageStream3);
                ImageView imageView3 = new ImageView(image3);
                imageView3.setX(100);
                imageView3.setY(5);
                imageView3.setFitWidth(150);
                imageView3.setPreserveRatio(true);
                buttonsbar.add(imageView3, 19, 0);
                buttonsbar.setHalignment(imageView3, HPos.RIGHT);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        imageView.setOnMouseClicked((MouseEvent e) -> {
//            popup_gridpane.setPrefHeight(bounds.getHeight()/4);
//            popup_gridpane.setPrefWidth(bounds.getWidth()/4);
//            popup.show(popup_gridpane.getScene().getWindow());

            final Stage dialog1 = new Stage();
            dialog1.initModality(Modality.APPLICATION_MODAL);
            dialog1.initOwner(primaryStage);
            VBox dialogVbox1 = new VBox(30);
            dialogVbox1.getChildren().add(new Text("Created by Mahic Shah, ECS"));
            Scene dialogScene1 = new Scene(dialogVbox1, 300, 100);
            dialog1.setScene(dialogScene1);
            dialog1.show();
        });

        gauge1.setOnMouseClicked((MouseEvent e1) -> {
            final Stage dialog2 = new Stage();
            dialog2.initModality(Modality.APPLICATION_MODAL);
            dialog2.initOwner(primaryStage);
            VBox dialogVbox2 = new VBox(30);
            dialogVbox2.getChildren().add(new Text("Created by Mahic Shah, ECS"));
            Scene dialogScene1 = new Scene(dialogVbox2, 300, 100);
            dialog2.setScene(dialogScene1);
            dialog2.show();
        });

        primaryStage.setTitle("Genesis");
        primaryStage.getIcons().add(image1);
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.setResizable(true);
        primaryStage.show();
    }



    public void shiftSeriesData1(float newValue) {
        for(int i=0; i<NUM_OF_POINT-1; i++){
            XYChart.Data<String, Number> ShiftDataUp = (XYChart.Data<String, Number>) series1.getData().get(i + 1);
            Number shiftValue = ShiftDataUp.getYValue();
            XYChart.Data<String, Number> ShiftDataDn = (XYChart.Data<String, Number>)series1.getData().get(i);
            ShiftDataDn.setYValue(shiftValue);
        }
        XYChart.Data<String, Number> lastData = (XYChart.Data<String, Number>)series1.getData().get(NUM_OF_POINT-1);
        lastData.setYValue(newValue);
    }

    public void shiftSeriesData2(float newValue2) {
        for(int i=0; i<NUM_OF_POINT-1; i++){
            XYChart.Data<String, Number> ShiftDataUp = (XYChart.Data<String, Number>)series2.getData().get(i+1);
            Number shiftValue = ShiftDataUp.getYValue();
            XYChart.Data<String, Number> ShiftDataDn = (XYChart.Data<String, Number>)series2.getData().get(i);
            ShiftDataDn.setYValue(shiftValue);
        }
        XYChart.Data<String, Number> lastData = (XYChart.Data<String, Number>)series2.getData().get(NUM_OF_POINT-1);
        lastData.setYValue(newValue2);
    }

    public void shiftSeriesData3(float newValue3) {
        for(int i=0; i<NUM_OF_POINT-1; i++){
            XYChart.Data<String, Number> ShiftDataUp = (XYChart.Data<String, Number>)series3.getData().get(i+1);
            Number shiftValue = ShiftDataUp.getYValue();
            XYChart.Data<String, Number> ShiftDataDn = (XYChart.Data<String, Number>)series3.getData().get(i);
            ShiftDataDn.setYValue(shiftValue);
        }
        XYChart.Data<String, Number> lastData = (XYChart.Data<String, Number>)series3.getData().get(NUM_OF_POINT-1);
        lastData.setYValue(newValue3);
    }
    public void shiftSeriesData4(float newValue4) {
        for(int i=0; i<NUM_OF_POINT-1; i++){
            XYChart.Data<String, Number> ShiftDataUp = (XYChart.Data<String, Number>)series4.getData().get(i+1);
            Number shiftValue = ShiftDataUp.getYValue();
            XYChart.Data<String, Number> ShiftDataDn = (XYChart.Data<String, Number>)series4.getData().get(i);
            ShiftDataDn.setYValue(shiftValue);
        }
        XYChart.Data<String, Number> lastData = (XYChart.Data<String, Number>)series4.getData().get(NUM_OF_POINT-1);
        lastData.setYValue(newValue4);
    }

    public void connectXBee(String port){
        System.out.println("XBee is connected.");
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
                            l = serialPort.readBytes(3);
                            int x = l[0];
                            if (x == 50) {
                                break;
                            }
                        }

                        byte[] b = serialPort.readBytes(3);

                        int id = b[0] & 0xFF;

                        if (id == 2) {
                            int rpm_1 = b[2] & 0xFF;
                            int rpm_2 = b[1] & 0xFF;
                            int rpm_total = ((rpm_1 * 256) + rpm_2);
                            Platform.runLater(() -> gauge1.setValue(rpm_total));
                        }

                        if (id == 3) {
                            int ops_1 = b[2] & 0xFF;
                            int ops_2 = b[1] & 0xFF;
                            int ops_total = ((ops_1 * 256) + ops_2);
                            Platform.runLater(() -> gauge1.setValue(ops_total));
                        }

                        if (id == 4) {
                            int ect1_1 = b[2] & 0xFF;
                            int ect1_2 = b[1] & 0xFF;
                            int ect1_total = ((ect1_1 * 256) + ect1_2);
                            Platform.runLater(() -> gauge3.setValue(ect1_total));
                        }

                        if (id == 5) {
                            int ect2_1 = b[2] & 0xFF;
                            int ect2_2 = b[1] & 0xFF;
                            int ect2_total = ((ect2_1 * 256) + ect2_2);
                            Platform.runLater(() -> gauge4.setValue(ect2_total));
                        }

                        if (id == 6) {
                            int tps_1 = b[2] & 0xFF;
                            int tps_2 = b[1] & 0xFF;
                            int tps_total = ((tps_1 * 256) + tps_2);
                            Platform.runLater(() -> gauge9.setValue(tps_total));
                        }

                        if (id == 7) {
                            int vol_1 = b[2] & 0xFF;
                            int vol_2 = b[1] & 0xFF;
                            int vol_total = ((vol_1 * 256) + vol_2);
                            Platform.runLater(() -> gauge10.setValue(vol_total));
                        }

                        System.out.println("b[0] = " + (b[0] & 0xFF));
//                            System.out.println("b[1] = " + (b[1] & 0xFF));
//                            System.out.println("b[2] = " + (b[2] & 0xFF));
//                            System.out.println("b[3] = " + (b[3] & 0xFF));
//                            System.out.println("b[4] = " + (b[4] & 0xFF));
//                            System.out.println("b[5] = " + (b[5] & 0xFF));
//                            System.out.println("b[6] = " + (b[6] & 0xFF));
//                            System.out.println("b[7] = " + (b[7] & 0xFF));
//                            System.out.println("b[8] = " + (b[8] & 0xFF));
                        System.out.println();

                        MenuItem Sub_Ports = new MenuItem(serialPort.getPortName());
                        Ports.getItems().add(Sub_Ports);

                    } catch(SerialPortException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            XBeePort = serialPort;
        } catch (SerialPortException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SerialPortException: " + ex);
        }
    }


    public void disconnectXBee() {
        System.out.println("XBee has been disconnected.");
        if (XBeePort != null) {
            try {
                XBeePort.removeEventListener();
                if (XBeePort.isOpened()) {
                    XBeePort.closePort();
                }
            } catch (SerialPortException ex) {
                Logger.getLogger(Controller.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void stop() throws Exception {
        disconnectXBee();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
