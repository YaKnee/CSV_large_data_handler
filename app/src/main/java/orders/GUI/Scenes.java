package orders.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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
        root.add(git, 10, 0);
        root.add(welcome, 1, 2);
        root.add(imgView, 3, 2);
        root.add(select, 1, 5);
        root.add(fileBox, 3, 5);
        root.add(errorLbl, 3, 6);
        root.add(continueBtn, 9, 10);
        root.add(exitBtn, 10, 10);
        root.getStylesheets().add("stylesheet.css");
        welcomeScene = new Scene(root);
        return welcomeScene;
    }

    public void createMenuScene(ArrayList<Order> orders) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> stage.setScene(welcomeScene));

        Hyperlink git = Components.createGitLink();

        Button custBtn = new Button("Performances");
        Text custText = new Text("View the performance of customers.");
        custBtn.setOnAction(e -> createPerformanceScene(orders));

        Button avgBtn = new Button("Averages");
        avgBtn.setOnAction(e -> createAveragesScene(orders));
        Text avgText = new Text("Find the averages of sales or customers on a property with graph for visualisation.");

        Button ordersBtn = new Button("Orders Summary");
        ordersBtn.setOnAction(e -> createCustomerSummaryScene(orders));
        Text orderText = new Text("View the summary of orders of a selected customer in table format.");

        GridPane root = new GridPane();
        root.add(backBtn, 0, 0);
        root.add(git, 10, 0);
        root.add(custText, 1, 3);
        root.add(custBtn, 2, 3);
        root.add(avgText, 1, 4);
        root.add(avgBtn, 2, 4);
        root.add(orderText, 1, 5);
        root.add(ordersBtn, 2, 5);
        root.getStylesheets().add("stylesheet.css");
        stage.setScene(new Scene(root));
        // return new Scene(root);
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public void createPerformanceScene(ArrayList<Order> orders) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> createMenuScene(orders));

        Hyperlink git = Components.createGitLink();

        Text descText = new Text("Click a column header to sort the table"
        + " respective to that property.\nClick again to reverse the order.");

        Map<String, Set<Order>> customerOrders = new HashMap<>();

        for (Order order : orders) {
            String customerId = order.customer().customerId();
            Set<Order> ordersForCustomer = customerOrders.computeIfAbsent(customerId, newCustomerId -> new HashSet<>());
            ordersForCustomer.add(order);
        }

        TableView<CustomerPerformance> performanceTable = new TableView<>();
        performanceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<CustomerPerformance, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<CustomerPerformance, String>("name"));
        TableColumn<CustomerPerformance, String> idCol =  new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<CustomerPerformance, String>("id"));
        TableColumn<CustomerPerformance, String> ordersCol = new TableColumn<>("Orders");
        ordersCol.setCellValueFactory(new PropertyValueFactory<CustomerPerformance, String>("orders"));
        TableColumn<CustomerPerformance, String> salesCol =  new TableColumn<>("Sales");
        salesCol.setCellValueFactory(new PropertyValueFactory<CustomerPerformance, String>("sales"));
        TableColumn<CustomerPerformance, String> profitsCol = new TableColumn<>("Profits");
        profitsCol.setCellValueFactory(new PropertyValueFactory<CustomerPerformance, String>("profits"));


        performanceTable.getColumns().addAll(nameCol,idCol,ordersCol,salesCol,profitsCol);

        ObservableList<CustomerPerformance> performanceList = FXCollections.observableArrayList();
        for (Map.Entry<String, Set<Order>> entry : customerOrders.entrySet()) {
            String customerId = entry.getKey();
            Set<Order> ordersForCustomer = entry.getValue();
            String name = "";
            for (Order order : ordersForCustomer) {
                name = order.customer().name();
                break; 
            }
            int orderSum = entry.getValue().size();
            long totalSales = ordersForCustomer.stream().mapToLong(Order::sales).sum();
            long totalProfits = ordersForCustomer.stream().mapToLong(Order::profit).sum();

            CustomerPerformance cp = new CustomerPerformance(name, customerId, orderSum, totalSales, totalProfits);
            performanceList.add(cp);
        }
        for (TableColumn<CustomerPerformance, ?> column : performanceTable.getColumns()) {
            column.setComparator((s1, s2) -> {
                try {
                    Integer n1 = Integer.parseInt((String) s1);
                    Integer n2 = Integer.parseInt((String) s2);
                    return n1.compareTo(n2);
                } catch (NumberFormatException e) {
                        return ((String) s1).compareTo((String) s2);

                }
            });
        }

        performanceTable.setItems(performanceList);
     
        GridPane root = new GridPane();
        root.add(backBtn, 0, 0);
        root.add(git, 10, 0);
        root.add(descText, 1, 3);
        root.add(performanceTable, 1, 5);
        root.getStylesheets().add("stylesheet.css");
        stage.setScene(new Scene(root));
    }

    public void createAveragesScene(ArrayList<Order> orders) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> createMenuScene(orders));

        Hyperlink git = Components.createGitLink();

        Text pText = new Text("Select the category for searching: ");
        String[] parentTxt = {"Customers", "Sales"};
        ComboBox<String> parentChoice = new ComboBox<>(FXCollections.observableArrayList(parentTxt));
        parentChoice.getSelectionModel().selectFirst();
        parentChoice.setPrefWidth(100);

        Text cText = new Text("Select the sub-category for searching: ");
        String[] childTxt = {"City", "State", "Region", "Segment", "Year"};
        ComboBox<String> childChoice = new ComboBox<>(FXCollections.observableArrayList(childTxt));
        childChoice.getSelectionModel().select(1);
        childChoice.setPrefWidth(100);

        chart = EventController.createChartForCategory("Customers", "State", orders);

        GridPane root = new GridPane();
        root.add(backBtn, 0, 0);
        root.add(git, 5, 0);
        root.add(pText, 1, 2);
        root.add(parentChoice, 3, 2);
        root.add(cText, 1, 3);
        root.add(childChoice, 3, 3);
        root.add(chart, 1, 4, 10, 10);

        parentChoice.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            String selectedParent = newValue;
            String selectedChild = childChoice.getValue();
            root.getChildren().remove(chart);
            chart = EventController.createChartForCategory(selectedParent, selectedChild, orders);
            root.add(chart, 1, 4, 10, 10);
         }); 
        childChoice.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            String selectedChild = newValue;
            String selectedParent = parentChoice.getValue();
            root.getChildren().remove(chart);
            chart = EventController.createChartForCategory(selectedParent, selectedChild, orders);
            root.add(chart, 1, 4, 10, 10);
         }); 
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
        stage.setScene(new Scene(root));
    }
}
