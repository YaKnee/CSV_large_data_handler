package orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import orders.CSVHandler.*;
import orders.GUI.*;


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
    
    /**
     * Entry point for the JavaFX application.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception Throws an exception if an error occurs during application start-up.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        ArrayList<Order> orders = CSVDataReader.createOrders("data/SuperStoreOrders.csv");

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
        customerSearchBtn.getStyleClass().add(".button");
        ComboBox<String> nameComboBox = new ComboBox<String>();
        Components.autoFillComboBox(nameComboBox, customerNameAndID.keySet());
        Handlers.orderSummaryTableFromName(customerSearchBtn, nameComboBox, tableHeading, table, customerNameAndID, customerOrders);

        //====================================================================================
        //----------------------------------CUSTOMER WITH MOST SALES--------------------------
        //====================================================================================
        String customerWithMostSales = "";
        int mostSales = 0;

        for (Map.Entry<String, Integer> entry : customerTotalSales.entrySet()) {
            if (entry.getValue() > mostSales) {
                mostSales = entry.getValue();
                customerWithMostSales = entry.getKey();
            }
        }

        for (Order order : orders) {
            if (order.customer().customerId().equals(customerWithMostSales)) {
                System.out.print("Customer ID: " + order.customer().customerId());
                System.out.print(", Name: " + order.customer().name());
                System.out.print(", Segment: " + order.customer().segment());
                System.out.println(", Sales: " + mostSales);
                break; 
            }
        }


        //====================================================================================
        //--------------------------TOTAL CUSTOMERS PER SEGMENT-------------------------------
        //====================================================================================
        Map<String, Integer> totalCustomersPerSegment = orders.stream()
        .collect(Collectors.groupingBy(order ->
            order.customer().segment(),
            Collectors.mapping(order -> 
                order.customer().customerId(), 
                Collectors.collectingAndThen(
                    Collectors.counting(),
                    Long::intValue
                ))
        ));

        Label segmentLabel = new Label("Segment: ");
        Button segmentSearchBtn = new Button("Search");
        segmentSearchBtn.getStyleClass().add(".button");
        Label segmentOutput = new Label();
        ComboBox<String> segmentComboBox = new ComboBox<String>();
        Components.autoFillComboBox(segmentComboBox, totalCustomersPerSegment.keySet());
        Handlers.searchButtonClick(totalCustomersPerSegment, segmentSearchBtn, segmentComboBox, segmentOutput, "Customers");


        //====================================================================================
        //-----------------------------------TOTAL SALES PER YEAR-----------------------------
        //====================================================================================
        Map<String, Long> salesPerYear = orders.stream()
        .collect(Collectors.groupingBy(order ->
            order.shipOrder().orderDate().substring(order.shipOrder().orderDate().length() - 4),
            Collectors.summingLong(Order::sales))
            );

        Label yearLabel = new Label("Year: ");
        Button yearSearchBtn = new Button("Search");
        yearSearchBtn.getStyleClass().add(".button");
        Label yearOutput = new Label();
        ComboBox<String> yearComboBox = new ComboBox<String>();
        Components.autoFillComboBox(yearComboBox, salesPerYear.keySet());
        Handlers.searchButtonClick(salesPerYear, yearSearchBtn, yearComboBox, yearOutput, "Sales");

        //====================================================================================
        //-------------------------------TOTAL CUSTOMERS PER STATE----------------------------
        //====================================================================================

        Map<String, Integer> customersPerState = orders.stream()
        .collect(Collectors.groupingBy(order -> 
            order.location().state(),
            Collectors.mapping(order -> 
                order.customer().customerId(), 
                Collectors.collectingAndThen(
                    Collectors.counting(),
                    Long::intValue)
                ))
            );


        Label stateLabel=new Label("State:"); 
        Button stateSearchBtn = new Button("Search");
        stateSearchBtn.getStyleClass().add(".button");
        Label stateOutput = new Label();
        ComboBox<String> stateComboBox = new ComboBox<String>();
        Components.autoFillComboBox(stateComboBox, customersPerState.keySet());
        Handlers.searchButtonClick(customersPerState, stateSearchBtn, stateComboBox, stateOutput, "Customers");

        //====================================================================================
        //----------------------------------TOTAL SALES PER REGION----------------------------
        //====================================================================================

        Map<String, Long> salesPerRegion = orders.stream()
        .collect(Collectors.groupingBy(order -> 
            order.location().region(), 
            Collectors.summingLong(Order::sales))
            );
        //salesPerRegion.forEach((region, sales) -> System.out.println("Sales in " + region + ": " + sales));

        Label regionLabel=new Label("Region:"); 
        Button regionSearchBtn = new Button("Search");
        regionSearchBtn.getStyleClass().add(".button");
        Label regionOutput = new Label();
        ComboBox<String> regionComboBox = new ComboBox<String>();
        Components.autoFillComboBox(regionComboBox, salesPerRegion.keySet());
        Handlers.searchButtonClick(salesPerRegion, regionSearchBtn, regionComboBox, regionOutput, "Sales");


        //====================================================================================
        //----------------------------------TOTAL AVERAGE SALES-------------------------------
        //====================================================================================
        Button salesBtn = new Button("Avg. Sales");
        salesBtn.getStyleClass().add(".button");
        Label salesLabel = new Label();
        Handlers.averageButtonClick(salesBtn, orders, salesLabel);


        ///////////////////////////////////////////////////////////mess around with other layouts
        GridPane root = new GridPane();
        root.setMinSize(500, 200);
        root.setPadding(new Insets(10, 10, 10, 10)); 
        root.setVgap(5); 
        root.setHgap(5);       
        root.setAlignment(Pos.CENTER); 

        root.add(segmentLabel, 0, 0);
        root.add(segmentComboBox, 1, 0);
        root.add(segmentSearchBtn, 2, 0);
        root.add(segmentOutput, 3, 0);

        root.add(stateLabel, 0, 1);
        root.add(stateComboBox, 1, 1);
        root.add(stateSearchBtn, 2, 1);
        root.add(stateOutput, 3, 1);

        root.add(regionLabel, 0, 2);
        root.add(regionComboBox, 1, 2);
        root.add(regionSearchBtn, 2, 2);
        root.add(regionOutput, 3, 2);

        root.add(yearLabel, 0, 3);
        root.add(yearComboBox, 1, 3);
        root.add(yearSearchBtn, 2, 3);
        root.add(yearOutput, 3, 3);

        root.add(salesBtn, 2, 4);
        root.add(salesLabel, 3, 4);
        
        root.add(customerLabel, 0, 5);
        root.add(nameComboBox, 1, 5);
        root.add(customerSearchBtn, 2, 5);


        root.add(tableHeading, 1, 6);
        root.add(table, 0, 7, 4, 10);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("SuperStore Data");
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }
}
