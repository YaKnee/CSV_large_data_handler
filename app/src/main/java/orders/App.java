package orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.collections.FXCollections;
// import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
// import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import orders.CSVHandler.*;
import orders.GUI.*;
import orders.OrderObjects.Order;



//TO DO
////Add Threading for Buttons
////Selector for States/Customers + Search bars         (done!)
////View summary of orders of customer                  (done!)
////Customers per State                                 (done!)
////Average Sales amounts of the orders                 (done?)
////Customer with most sales                            (done!)
////Count segments                                      (done!)
////total sales per year                                (done!)
////total sales per region                              (done!)
////****Testing****
////JavaDocs
////Make Pretty

/**
 * This class represents a JavaFX application for managing SuperStore orders data.
 */
public class App extends Application{
    
    private Stage stage;
    private Scene avgScene;
    private Scene orderSumScene;
    private Scene menuScene;
    private BarChart<String, ? extends Number> chart;

    /**
     * Entry point for the JavaFX application.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception Throws an exception if an error occurs during application start-up.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            stage = primaryStage;
            ArrayList<Order> orders = CSVDataReader.createOrders("data/SuperStoreOrders.csv");

            //------------------------------------All orders for a customerId----------------------
        //     Map<String, Set<Order>> customerOrders = new HashMap<>();
        //     Map<String, String> customerNameAndID = new HashMap<>();
        //     Map<String, Integer> customerTotalSales = new HashMap<>();
    
        //     for (Order order : orders) {
        //         String customerId = order.customer().customerId();
        //         String customerName = order.customer().name();
        //         Set<Order> ordersForCustomer = customerOrders.computeIfAbsent(customerId, newCustomerId -> new HashSet<>());
        //         ordersForCustomer.add(order);
        //         customerNameAndID.putIfAbsent(customerName, customerId);
        //         customerTotalSales.merge(customerId, order.sales(), Integer::sum);
        //     }
    
        //     //====================================================================================
        //     //---------------------------FIND CUSTOMER AND DISPLAY ORDERS-------------------------
        //     //====================================================================================
        //     TableView<Order> table = new TableView<>();
        //     table.setVisible(false);
        //     Label tableHeading = new Label();
        //     Label customerLabel=new Label("Customer Name:"); 
        //     Button customerSearchBtn = new Button("Search");
        //     ComboBox<String> nameComboBox = new ComboBox<String>();
        //     Components.autoFillComboBox(nameComboBox, customerNameAndID.keySet());
        //     EventController.orderSummaryTableFromName(customerSearchBtn, nameComboBox, tableHeading, table, customerNameAndID, customerOrders);
    
        //     //====================================================================================
        //     //----------------------------------CUSTOMER WITH MOST SALES--------------------------
        //     //====================================================================================
        //     String customerWithMostSales = "";
        //     int mostSales = 0;
    
        //     for (Map.Entry<String, Integer> entry : customerTotalSales.entrySet()) {
        //         if (entry.getValue() > mostSales) {
        //             mostSales = entry.getValue();
        //             customerWithMostSales = entry.getKey();
        //         }
        //     }
    
        //     for (Order order : orders) {
        //         if (order.customer().customerId().equals(customerWithMostSales)) {
        //             System.out.print("Customer ID: " + order.customer().customerId());
        //             System.out.print(", Name: " + order.customer().name());
        //             System.out.print(", Segment: " + order.customer().segment());
        //             System.out.println(", Sales: " + mostSales);
        //             break; 
        //         }
        //     }
    
    
        //     //====================================================================================
        //     //--------------------------TOTAL CUSTOMERS PER SEGMENT-------------------------------
        //     //====================================================================================
        //     Map<String, Integer> totalCustomersPerSegment = orders.stream()
        //     .collect(Collectors.groupingBy(order ->
        //         order.customer().segment(),
        //         Collectors.mapping(order -> 
        //             order.customer().customerId(), 
        //             Collectors.collectingAndThen(
        //                 Collectors.counting(),
        //                 Long::intValue
        //             ))
        //     ));
    
        //     Label segmentLabel = new Label("Segment: ");
        //     Button segmentSearchBtn = new Button("Search");
        //     Label segmentOutput = new Label();
        //     ComboBox<String> segmentComboBox = new ComboBox<String>();
        //     Components.autoFillComboBox(segmentComboBox, totalCustomersPerSegment.keySet());
        //     EventController.searchButton(totalCustomersPerSegment, segmentSearchBtn, segmentComboBox, segmentOutput, "Customers", "segment");
    
    
        //     //====================================================================================
        //     //-----------------------------------TOTAL SALES PER YEAR-----------------------------
        //     //====================================================================================
        //     Map<String, Long> salesPerYear = generateSalesMap(orders, order ->
        //         order.shipOrder().orderDate().substring(order.shipOrder().orderDate().length() - 4));
        //     Label yearLabel = new Label("Year: ");
        //     Button yearSearchBtn = new Button("Search");
        //     Label yearOutput = new Label();
        //     ComboBox<String> yearComboBox = new ComboBox<String>();
        //     Components.autoFillComboBox(yearComboBox, salesPerYear.keySet());
        //     EventController.searchButton(salesPerYear, yearSearchBtn, yearComboBox, yearOutput, "Sales", "year");
    
        //     //====================================================================================
        //     //-------------------------------TOTAL CUSTOMERS PER STATE----------------------------
        //     //====================================================================================
    
        //     // Map<String, Integer> customersPerState = orders.stream()
        //     // .collect(Collectors.groupingBy(order -> 
        //     //     order.location().state(),
        //     //     Collectors.mapping(order -> 
        //     //         order.customer().customerId(), 
        //     //         Collectors.collectingAndThen(
        //     //             Collectors.counting(),
        //     //             Long::intValue)
        //     //         ))
        //     //     );
        //     Map<String, Integer> customersPerState = generateCustomersMap(orders, order -> order.location().state());
    
    
        //     Label stateLabel=new Label("State:"); 
        //     Button stateSearchBtn = new Button("Search");
        //     Label stateOutput = new Label();
        //     ComboBox<String> stateComboBox = new ComboBox<String>();
        //     Components.autoFillComboBox(stateComboBox, customersPerState.keySet());
        //     EventController.searchButton(customersPerState, stateSearchBtn, stateComboBox, stateOutput, "Customers", "state");
    
        //     //====================================================================================
        //     //----------------------------------TOTAL SALES PER REGION----------------------------
        //     //====================================================================================
    
        //     Map<String, Long> salesPerRegion = generateSalesMap(orders, order -> 
        //                                                         order.location().region());
        //     //salesPerRegion.forEach((region, sales) -> System.out.println("Sales in " + region + ": " + sales));
    
        //     Label regionLabel=new Label("Region:"); 
        //     Button regionSearchBtn = new Button("Search");
        //     Label regionOutput = new Label();
        //     ComboBox<String> regionComboBox = new ComboBox<String>();
        //     Components.autoFillComboBox(regionComboBox, salesPerRegion.keySet());
        //     EventController.searchButton(salesPerRegion, regionSearchBtn, regionComboBox, regionOutput, "Sales", "region");
    
    
        //     //====================================================================================
        //     //----------------------------------TOTAL AVERAGE SALES-------------------------------
        //     //====================================================================================
        //     Button salesBtn = new Button("Avg. Sales");
        //     Label salesLabel = new Label();
        //     EventController.averageButton(salesBtn, orders, salesLabel);
    
    
        //     ///////////////////////////////////////////////////////////mess around with other layouts
        // //    Parent root = FXMLLoader.load(getClass().getResource("/welcome.fxml"));
        //     GridPane root = new GridPane();

        //     root.add(segmentLabel, 0, 0);
        //     root.add(segmentComboBox, 1, 0);
        //     root.add(segmentSearchBtn, 2, 0);
        //     root.add(segmentOutput, 3, 0);
    
        //     root.add(stateLabel, 0, 1);
        //     root.add(stateComboBox, 1, 1);
        //     root.add(stateSearchBtn, 2, 1);
        //     root.add(stateOutput, 3, 1);
    
        //     root.add(regionLabel, 0, 2);
        //     root.add(regionComboBox, 1, 2);
        //     root.add(regionSearchBtn, 2, 2);
        //     root.add(regionOutput, 3, 2);
    
        //     root.add(yearLabel, 0, 3);
        //     root.add(yearComboBox, 1, 3);
        //     root.add(yearSearchBtn, 2, 3);
        //     root.add(yearOutput, 3, 3);
    
        //     root.add(salesBtn, 2, 4);
        //     root.add(salesLabel, 3, 4);
            
        //     root.add(customerLabel, 0, 5);
        //     root.add(nameComboBox, 1, 5);
        //     root.add(customerSearchBtn, 2, 5);
    
    
        //     root.add(tableHeading, 1, 6);
        //     root.add(table, 0, 7, 4, 10);

            // Scene scene = new Scene(root);
            // scene.getStylesheets().add("stylesheet.css");
            menuScene = createMenuScene();
            avgScene = createAveragesScene(orders);
            orderSumScene = createCustomerSummaryScene(orders);

            Image icon = new Image("icon.png");
            stage.getIcons().add(icon);
            stage.setScene(menuScene);
            stage.setTitle("SuperStore Data");
            stage.setOnCloseRequest(event -> System.exit(0));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Scene createMenuScene() {
        Button custBtn = new Button("Totals");
        Text custText = new Text("View of the total sales or customers on a property with graph for visualisation.");
        custBtn.setOnAction(e -> changeScene(null));

        Button avgBtn = new Button("Averages");
        avgBtn.setOnAction(e -> changeScene(avgScene));
        Text avgText = new Text("Find the averages of sales or customers on a property with graph for visualisation.");

        Button ordersBtn = new Button("Orders Summary");
        ordersBtn.setOnAction(e -> changeScene(orderSumScene));
        Text orderText = new Text("View the summary of orders of a selected customer in table format.");

        GridPane root = new GridPane();
        root.add(custText, 0, 0);
        root.add(custBtn, 1, 0);
        root.add(avgText, 0, 1);
        root.add(avgBtn, 1, 1);
        root.add(orderText, 0, 2);
        root.add(ordersBtn, 1, 2);
        root.getStylesheets().add("stylesheet.css");
        root.setMinSize(600, 600);
        return new Scene(root);
    }

    private Scene createCustomerSummaryScene(ArrayList<Order> orders) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> changeScene(menuScene));

        //------------------------------------All orders for a customerId----------------------
        Map<String, Set<Order>> customerOrders = new HashMap<>();
        Map<String, String> customerNameAndID = new HashMap<>();
        Map<String, Integer> customerTotalSales = new HashMap<>();
        for (Order order : orders) {
            String customerId = order.customer().customerId();
            String customerName = order.customer().name();
            Set<Order> ordersForCustomer = customerOrders.computeIfAbsent(customerId, newCustomerId -> new HashSet<>());
            ordersForCustomer.add(order);
            customerNameAndID.putIfAbsent(customerName, customerId);
            customerTotalSales.merge(customerId, order.sales(), Integer::sum);
        }
        //====================================================================================
        //---------------------------FIND CUSTOMER AND DISPLAY ORDERS-------------------------
        //====================================================================================
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
        return new Scene(root);
    }

    private Scene createAveragesScene(ArrayList<Order> orders) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> changeScene(menuScene));

        Text pText = new Text("Select the category for searching: ");
        String[] parentTxt = {"Customers", "Sales"};
        ComboBox<String> parentChoice = new ComboBox<>(FXCollections.observableArrayList(parentTxt));
        parentChoice.getSelectionModel().selectFirst();

        Text cText = new Text("Select the sub-category for searching: ");
        String[] childTxt = {"City", "State", "Region", "Segment", "Year"};
        ComboBox<String> childChoice = new ComboBox<>(FXCollections.observableArrayList(childTxt));
        childChoice.getSelectionModel().selectFirst();
        chart = EventController.updateChart("Customers", "City", orders);

        VBox root = new VBox(5);
        // root.setMinWidth(500);
        root.setPadding(new Insets(10,10,10,10));
        root.getChildren().addAll(backBtn,pText, parentChoice, cText, childChoice, chart);
        

        parentChoice.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            String selectedParent = newValue;
            String selectedChild = childChoice.getValue();
            root.getChildren().remove(chart);
            chart = EventController.updateChart(selectedParent, selectedChild, orders);
            root.getChildren().add(chart);
         }); 
        childChoice.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            String selectedChild = newValue;
            String selectedParent = parentChoice.getValue();
            root.getChildren().remove(chart);
            chart = EventController.updateChart(selectedParent, selectedChild, orders);
            root.getChildren().add(chart);
         }); 
         root.setMinSize(600, 600);
         root.getStylesheets().add("stylesheet.css");
        return new Scene(root);
    }

    private void changeScene(Scene scene) {
        stage.setScene(scene);
    }

    public static Map<String, Long> generateSalesMap(ArrayList<Order> orders, Function<Order, String> groupingFunction) {
        return orders.stream().collect(Collectors.groupingBy(
                            groupingFunction, 
                            Collectors.summingLong(Order::sales)));
    }

    public static Map<String, Integer> generateCustomersMap(ArrayList<Order> orders, Function<Order, String> groupingFunction) {
        return orders.stream()
        .collect(Collectors.groupingBy(groupingFunction,
            Collectors.mapping(order -> 
                order.customer().name(), /////////////////////////////change back to customerID?
                Collectors.collectingAndThen(
                    Collectors.counting(),
                    Long::intValue
                ))));
    }

    
}

