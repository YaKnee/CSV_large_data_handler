package orders.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

import orders.CustomerPerformance;
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
    
    public Scene createWelcomeScene() {
        Hyperlink git = Components.createGitLink();
        HBox topBox = new HBox(git);
        topBox.setAlignment(Pos.TOP_RIGHT);

        Text welcome = new Text("Welcome!");
        welcome.setStyle("-fx-font: 24 arial;");
        Image image = new Image("files2.png");
        ImageView imgView = new ImageView(image);
        imgView.setFitWidth(100);
        imgView.setFitHeight(100);
        Text select = new Text("Please select the data file that you wish to view.");
        String[] files = {"SuperStoreOrders.csv"};
        ComboBox<String> fileBox = new ComboBox<>(FXCollections.observableArrayList(files));
        Label errorLbl = new Label("Please select a file.");
        errorLbl.setStyle("-fx-text-fill: red");
        errorLbl.setVisible(false);
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.add(welcome, 1, 2);
        grid.add(imgView, 3, 2);
        grid.add(select, 1, 5);
        grid.add(fileBox, 3, 5);
        grid.add(errorLbl, 3, 6);
        grid.getStyleClass().add("grid");

        Button selectFileButton = new Button("Select");
        selectFileButton.setOnAction(e->{
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
        HBox bottomBox = new HBox(selectFileButton, exitBtn);
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBox.setSpacing(10);

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(grid);
        root.setBottom(bottomBox);
        root.getStylesheets().add("stylesheet.css");

        welcomeScene = new Scene(root);
        return welcomeScene;
    }

    public void createMenuScene(ArrayList<Order> orders) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> stage.setScene(welcomeScene));
        Hyperlink git = Components.createGitLink();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBox = new HBox(backBtn, spacer, git); 
        topBox.setAlignment(Pos.CENTER);
        topBox.setSpacing(10);

        Text custText = new Text("Overall performance of customers");
        custText.getStyleClass().add("menu-text");
        Button custBtn = new Button("Performances");
        custBtn.getStyleClass().add("menu-button");
        custBtn.setOnAction(e -> createPerformanceScene(orders));
        VBox custBox = new VBox(custText, custBtn);
        custBox.setAlignment(Pos.CENTER);

        Text totalText = new Text("Total sales or customers per property");
        totalText.getStyleClass().add("menu-text");
        Button totalBtn = new Button("Totals");
        totalBtn.getStyleClass().add("menu-button");
        totalBtn.setOnAction(e -> createAveragesScene(orders));
        VBox totalBox = new VBox(totalText, totalBtn);
        totalBox.setAlignment(Pos.CENTER);
        

        Text orderText = new Text("Orders summary for specific customer");
        orderText.getStyleClass().add("menu-text");
        Button orderBtn = new Button("Summary");
        orderBtn.getStyleClass().add("menu-button");
        orderBtn.setOnAction(e -> createCustomerSummaryScene(orders));
        VBox orderBox = new VBox(orderText, orderBtn);
        orderBox.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid");
        grid.add(custBox, 0, 0);
        grid.add(totalBox, 0, 1);
        grid.add(orderBox, 0, 2);
        grid.setVgap(20);


        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(grid);
        
        root.getStylesheets().add("stylesheet.css");
        stage.setScene(new Scene(root));
        // return new Scene(root);
    }

    public void createPerformanceScene(ArrayList<Order> orders) {

        Text descText = new Text("Click a column header to sort the table"
        + " respective to that property.\nClick again to reverse the order.");
        TableView<CustomerPerformance> performanceTable =
                                        Components.performanceTable(orders);

        GridPane grid = new GridPane();
        grid.add(descText, 1, 3);
        grid.add(performanceTable, 1, 5);
        grid.getStyleClass().add("grid");

        BorderPane root = new BorderPane();
        backAndGit(orders, root);
        root.setCenter(grid);

        root.getStylesheets().add("stylesheet.css");
        stage.setScene(new Scene(root));
    }

    public void createAveragesScene(ArrayList<Order> orders) {
        Text pText = new Text("Select the category for searching: ");
        String[] parentTxt = {"Customers", "Sales"};
        ComboBox<String> parentChoice =
                new ComboBox<>(FXCollections.observableArrayList(parentTxt));
        parentChoice.getSelectionModel().selectFirst();
        parentChoice.setPrefWidth(100);

        Text cText = new Text("Select the sub-category for searching: ");
        String[] childTxt = {"City", "State", "Region", "Segment", "Year"};
        ComboBox<String> childChoice =
                new ComboBox<>(FXCollections.observableArrayList(childTxt));
        childChoice.getSelectionModel().select(1);
        childChoice.setPrefWidth(100);

        chart = EventController.createChartForCategory("Customers", "State", orders);

        GridPane grid = new GridPane();
        grid.add(pText, 1, 2);
        grid.add(parentChoice, 3, 2);
        grid.add(cText, 1, 3);
        grid.add(childChoice, 3, 3);
        grid.add(chart, 1, 4, 10, 10);
        grid.getStyleClass().add("grid");

        BorderPane root = new BorderPane();
        backAndGit(orders, root);
        root.setCenter(grid);

        parentChoice.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            String selectedParent = newValue;
            String selectedChild = childChoice.getValue();
            grid.getChildren().remove(chart);
            chart = EventController.createChartForCategory(selectedParent, selectedChild, orders);
            grid.add(chart, 1, 4, 10, 10);
         }); 
        childChoice.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            String selectedChild = newValue;
            String selectedParent = parentChoice.getValue();
            grid.getChildren().remove(chart);
            chart = EventController.createChartForCategory(selectedParent, selectedChild, orders);
            grid.add(chart, 1, 4, 10, 10);
         }); 
         root.getStylesheets().add("stylesheet.css");

         stage.setScene(new Scene(root));
    }

    public void createCustomerSummaryScene(ArrayList<Order> orders) {
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

        GridPane grid = new GridPane();
        grid.add(customerLabel, 0, 1);
        grid.add(nameComboBox, 1, 1);
        grid.add(customerSearchBtn, 2, 1);
        grid.add(tableHeading, 1, 2);
        grid.add(table, 0, 3, 10, 10);
        grid.getStyleClass().add("grid");

        BorderPane root = new BorderPane();
        backAndGit(orders, root);
        root.setCenter(grid);
        root.getStylesheets().add("stylesheet.css");
        stage.setScene(new Scene(root));
    }

    private void backAndGit(ArrayList<Order> orders, BorderPane bp) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> createMenuScene(orders));
        Hyperlink git = Components.createGitLink();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBox = new HBox(backBtn, spacer, git); 
        topBox.setAlignment(Pos.CENTER);
        topBox.setSpacing(10);
        bp.setTop(topBox);
    }
}
