package orders.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import orders.CSVHandler.CSVDataReader;
import orders.OrderObjects.Order;

public class Scenes {
    private Stage stage;
    private Scene welcomeScene;
    private LineChart<String, Number> chart;

    public Scenes(final Stage stage) {
        this.stage = stage;
        this.welcomeScene = createWelcomeScene();
    }
    
    public Scene createWelcomeScene(){
        Text welcome = new Text("Welcome!");
        welcome.setStyle("-fx-font: 24 arial;");
        Image image = new Image("files2.png");
        ImageView imgView = new ImageView(image);
        imgView.setFitWidth(200);
        imgView.setFitHeight(200);
        Text select = new Text("Please select the data file that you wish to view.");
        String[] files = {"SuperStoreOrders.csv"};
        ComboBox<String> fileBox = new ComboBox<>(FXCollections.observableArrayList(files));
        Label errorLbl = new Label("Please select a file.");
        errorLbl.setStyle("-fx-text-fill: red");
        errorLbl.setVisible(false);
        Button continueBtn = new Button("Select");
        continueBtn.setOnAction(e->{
            if (fileBox.getSelectionModel().isEmpty()) {
                errorLbl.setVisible(true);
            }else if (fileBox.getValue().toString().equals("SuperStoreOrders.csv")) {
                errorLbl.setVisible(false);
                ArrayList<Order> orders = CSVDataReader.createOrders("data/" + fileBox.getValue().toString());
                createMenuScene(orders);
            } else {
                errorLbl.setText("Uuuuuummmm, yeah we ain't done that yet.");
                errorLbl.setVisible(true);
            }
        });
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(e -> System.exit(0));
        GridPane root = new GridPane();
        root.add(welcome, 0, 0);
        root.add(imgView, 3, 0);
        root.add(select, 0, 2);
        root.add(fileBox, 3, 2);
        root.add(errorLbl, 3, 3);
        root.add(continueBtn, 4, 5);
        root.add(exitBtn, 5, 5);
        root.getStylesheets().add("stylesheet.css");
        welcomeScene = new Scene(root);
        return welcomeScene;
    }

    public void createMenuScene(ArrayList<Order> orders) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> stage.setScene(welcomeScene));

        Button custBtn = new Button("Totals");
        custBtn.setStyle("-fx-background-color: red");
        Text custText = new Text("View of the total sales or customers on a property with graph for visualisation.");
        Label eLabel = new Label("Scene not yet implemented.");
        eLabel.setStyle("-fx-text-fill: red");
        eLabel.setVisible(false);
        custBtn.setOnAction(e -> eLabel.setVisible(true));

        Button avgBtn = new Button("Averages");
        avgBtn.setOnAction(e -> createAveragesScene(orders));
        Text avgText = new Text("Find the averages of sales or customers on a property with graph for visualisation.");

        Button ordersBtn = new Button("Orders Summary");
        ordersBtn.setOnAction(e -> createCustomerSummaryScene(orders));
        Text orderText = new Text("View the summary of orders of a selected customer in table format.");

        GridPane root = new GridPane();
        root.add(backBtn, 0, 0);
        root.add(custText, 1, 3);
        root.add(custBtn, 2, 3);
        root.add(avgText, 1, 4);
        root.add(avgBtn, 2, 4);
        root.add(orderText, 1, 5);
        root.add(ordersBtn, 2, 5);
        root.add(eLabel, 1, 7);
        root.getStylesheets().add("stylesheet.css");
        root.setMinSize(600, 600);
        stage.setScene(new Scene(root));
        // return new Scene(root);
    }

    public void createAveragesScene(ArrayList<Order> orders) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> createMenuScene(orders));

        Text pText = new Text("Select the category for searching: ");
        String[] parentTxt = {"Customers", "Sales"};
        ComboBox<String> parentChoice = new ComboBox<>(FXCollections.observableArrayList(parentTxt));
        parentChoice.getSelectionModel().selectFirst();

        Text cText = new Text("Select the sub-category for searching: ");
        String[] childTxt = {"City", "State", "Region", "Segment", "Year"};
        ComboBox<String> childChoice = new ComboBox<>(FXCollections.observableArrayList(childTxt));
        childChoice.getSelectionModel().select(1);
        chart = EventController.createChartForCategory("Customers", "State", orders);

        VBox root = new VBox(5);
        // root.setMinWidth(500);
        root.setPadding(new Insets(10,10,10,10));
        root.getChildren().addAll(backBtn,pText, parentChoice, cText, childChoice, chart);
        

        parentChoice.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            String selectedParent = newValue;
            String selectedChild = childChoice.getValue();
            root.getChildren().remove(chart);
            chart = EventController.createChartForCategory(selectedParent, selectedChild, orders);
            root.getChildren().add(chart);
         }); 
        childChoice.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            String selectedChild = newValue;
            String selectedParent = parentChoice.getValue();
            root.getChildren().remove(chart);
            chart = EventController.createChartForCategory(selectedParent, selectedChild, orders);
            root.getChildren().add(chart);
         }); 

         root.setMinSize(600, 600);
         root.getStylesheets().add("stylesheet.css");

         stage.setScene(new Scene(root));
    }

    public void createCustomerSummaryScene(ArrayList<Order> orders) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> createMenuScene(orders));

        Map<String, Set<Order>> customerOrders = new HashMap<>();
        Map<String, String> customerNameAndID = new HashMap<>();
        for (Order order : orders) {
            String customerId = order.customer().customerId();
            String customerName = order.customer().name();
            Set<Order> ordersForCustomer = customerOrders.computeIfAbsent(customerId, newCustomerId -> new HashSet<>());
            ordersForCustomer.add(order);
            customerNameAndID.putIfAbsent(customerName, customerId);
        }

        TableView<Order> table = new TableView<>();
        table.setVisible(false);
        Label tableHeading = new Label();
        Label customerLabel=new Label("Customer Name:"); 
        Button customerSearchBtn = new Button("Search");
        ComboBox<String> nameComboBox = new ComboBox<String>();
        Components.autoFillComboBox(nameComboBox, customerNameAndID.keySet());
        EventController.orderSummaryTableFromName(customerSearchBtn, nameComboBox, tableHeading, table, customerNameAndID, customerOrders);

        GridPane root = new GridPane();
        root.add(backBtn, 0, 0);
        root.add(customerLabel, 0, 1);
        root.add(nameComboBox, 1, 1);
        root.add(customerSearchBtn, 2, 1);
        root.add(tableHeading, 1, 2);
        root.add(table, 0, 3, 10, 10);
        root.getStylesheets().add("stylesheet.css");
        root.setMinSize(600, 600);
        stage.setScene(new Scene(root));
    }
}
