package orders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.text.NumberFormat;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import orders.CSVHandler.CSVDataReader;



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

public class App extends Application{

    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

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
            Set<Order> ordersForCustomer = customerOrders.computeIfAbsent(customerId, orderDetails -> new HashSet<>());
            ordersForCustomer.add(order);
            customerNameAndID.put(customerName, customerId);
            int totalSales = order.sales();
            customerTotalSales.put(customerId, customerTotalSales.getOrDefault(customerId, 0) + totalSales);
        }



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
        //---------------------------FIND CUSTOMER AND DISPLAY ORDERS-------------------------
        //====================================================================================
        TableView<Order> table = new TableView<>();
        table.setVisible(false);
        Label tableHeading = new Label();
        Label customerLabel=new Label("Customer Name:"); 
        ComboBox<String> nameComboBox = new ComboBox<String>();
        autoFillComboBox(nameComboBox, customerNameAndID.keySet());
        Button customerSearchBtn = new Button("Search");
        customerSearchBtn.getStyleClass().add(".button");
        Label customerOutput = new Label();
        customerSearchBtn.setOnAction(e-> {
            String name = nameComboBox.getEditor().getText();
            if (name == null || name.isEmpty()) {
                tableHeading.setText("Empty search bar.");
                table.setVisible(false);
                return;
            }
            for (String key : customerNameAndID.keySet()) {
                if (key.equalsIgnoreCase(name)) {
                    tableHeading.setText(key);
                    ObservableList<Order> data = FXCollections.observableArrayList(customerOrders.get(customerNameAndID.get(key)));
                    populateTable(table, data);
                    return;
                }
            }
            tableHeading.setText("Customer not found.");
            table.setVisible(false);
        });

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
        ComboBox<String> segmentComboBox = new ComboBox<String>();
        autoFillComboBox(segmentComboBox, totalCustomersPerSegment.keySet());
        Button segmentSearchBtn = new Button("Search");
        segmentSearchBtn.getStyleClass().add(".button");
        Label segmentOutput = new Label();
        handleButtonClick(totalCustomersPerSegment, segmentSearchBtn, segmentComboBox, segmentOutput, "Customers");


        //====================================================================================
        //-----------------------------------TOTAL SALES PER YEAR-----------------------------
        //====================================================================================
        Map<String, Long> salesPerYear = orders.stream()
        .collect(Collectors.groupingBy(order ->
            order.shipOrder().orderDate().substring(order.shipOrder().orderDate().length() - 4),
            Collectors.summingLong(Order::sales))
            );

        Label yearLabel = new Label("Year: ");
        ComboBox<String> yearComboBox = new ComboBox<String>();
        autoFillComboBox(yearComboBox, salesPerYear.keySet());
        Button yearSearchBtn = new Button("Search");
        yearSearchBtn.getStyleClass().add(".button");
        Label yearOutput = new Label();
        handleButtonClick(salesPerYear, yearSearchBtn, yearComboBox, yearOutput, "Sales");

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
        ComboBox<String> stateComboBox = new ComboBox<String>();
        autoFillComboBox(stateComboBox, customersPerState.keySet());
        Button stateSearchBtn = new Button("Search");
        stateSearchBtn.getStyleClass().add(".button");
        Label stateOutput = new Label();
        handleButtonClick(customersPerState, stateSearchBtn, stateComboBox, stateOutput, "Customers");

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
        ComboBox<String> regionComboBox = new ComboBox<String>();
        autoFillComboBox(regionComboBox, salesPerRegion.keySet());
        Button regionSearchBtn = new Button("Search");
        regionSearchBtn.getStyleClass().add(".button");
        Label regionOutput = new Label();
        handleButtonClick(salesPerRegion, regionSearchBtn, regionComboBox, regionOutput, "Sales");


        //====================================================================================
        //----------------------------------TOTAL AVERAGE SALES-------------------------------
        //====================================================================================
        Button salesBtn = new Button("Avg. Sales");
        salesBtn.getStyleClass().add(".button");
        Label salesLabel = new Label();

        salesBtn.setOnAction(e -> {
            long totalSales = orders.stream().mapToLong(Order::sales).sum();
            double averageSales = (double) totalSales / orders.size();
            System.out.println("Total Average Sales: " + averageSales);
            salesLabel.setText(numberFormat.format(averageSales));
        });

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
        root.add(customerOutput, 3, 5);

        root.add(tableHeading, 1, 6);
        root.add(table, 0, 7, 4, 10);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("SuperStore Data");
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }


    public void autoFillComboBox(ComboBox<String> cb, Collection<String> list) {
        cb.setEditable(true);
        ObservableList<String> items = FXCollections.observableArrayList(list);
                                                                            //visible 
        FilteredList<String> filteredItems = new FilteredList<String>(items, v -> true);

        cb.getEditor().textProperty().addListener((obs, originalVal, updatedVal) -> {
            final TextField search = cb.getEditor();
            final String selected = cb.getSelectionModel().getSelectedItem();

            Platform.runLater(() -> {
                if (selected == null || !selected.equals(search.getText())) {
                    filteredItems.setPredicate(item -> {
                        if (item.toUpperCase().startsWith(updatedVal.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
        });
        cb.setItems(filteredItems);
    }


    public <T extends Number> void handleButtonClick(Map<String, T> map, Button button, ComboBox<String> cb, Label outputLabel, String item) {
        button.setOnAction(e-> {
            String input = cb.getEditor().getText();
            if (input == null || input.isEmpty()) {
                outputLabel.setText("Empty search bar.");
            }   else if(map.containsKey(input)){
                outputLabel.setText(item + " in " + input + ": " + numberFormat.format(map.get(input)));
                System.out.println(item + " in " + input + ": " + numberFormat.format(map.get(input)));
            } else {
                System.out.println(item + " not found in \"" + input + "\".");
                outputLabel.setText(item + " not found in \"" + input + "\".");
            }
            /////////////////////////////////////////////////////////////CHECK IF INPUT IS WRONG TYPE
        });
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void populateTable(TableView<Order> table, ObservableList<Order> data){
        //use ___Col.setVisible(false/true) for checkboxs
        table.getItems().clear();
        table.getColumns().clear();
        TableColumn<Order, String> countCol = createColumn("Row ID", Order::rowID);

        TableColumn<Order, String> dateCol = new TableColumn("Order & Shipping");
        TableColumn<Order, String> orderIDCol = createColumn("Order ID", order -> order.shipOrder().orderId());
        TableColumn<Order, String> orderDateCol = createColumn("Order Date",  order -> order.shipOrder().orderDate());
        TableColumn<Order, String> shipDateCol = createColumn("Shipping Date",  order -> order.shipOrder().shipDate());
        TableColumn<Order, String> shippingModeCol = createColumn("Shipping Mode",  order -> order.shipOrder().shipMode());


        TableColumn<Order, String> customerCol = new TableColumn("Customer");
        TableColumn<Order, String> custNameCol = createColumn("Name",  order -> order.customer().name());
        TableColumn<Order, String> segmentCol = createColumn("Segment", order -> order.customer().segment());

        TableColumn<Order, String> locationCol = new TableColumn("Location");
        TableColumn<Order, String> cityCol = createColumn("City", order -> order.location().city());
        TableColumn<Order, String> stateCol = createColumn("State", order -> order.location().state());
        TableColumn<Order, String> regionCol = createColumn("Region",order -> order.location().region());

        TableColumn<Order, String> productsCol = new TableColumn("Products");
        TableColumn<Order, String> productIDCol = createColumn("ID", order -> order.product().id());
        TableColumn<Order, String> productSubCatCol = createColumn("Sub-Category", order -> order.product().subCategory());

        TableColumn<Order, String> logisticsCol = new TableColumn("Logistics");
        TableColumn<Order, String> salesCol = createColumn("Sales", Order::sales);
        TableColumn<Order, String> quantityCol = createColumn("Quantity", Order::quantity);
        TableColumn<Order, String> discountCol = createColumn("discount", Order::discount);
        TableColumn<Order, String> profitCol = createColumn("Profit", Order::profit);

        table.setItems(data);

        dateCol.getColumns().addAll(orderIDCol,orderDateCol,shipDateCol,shippingModeCol);
        customerCol.getColumns().addAll(custNameCol,segmentCol);
        locationCol.getColumns().addAll(cityCol, stateCol, regionCol);
        productsCol.getColumns().addAll(productIDCol, productSubCatCol);
        logisticsCol.getColumns().addAll(salesCol,quantityCol,discountCol,profitCol);

        table.getColumns().addAll(countCol,dateCol, customerCol, locationCol, productsCol, logisticsCol);
        for(TableColumn col : table.getColumns()){
            col.setResizable(false);
        }
        table.setEditable(false);
        table.setVisible(true);
    }

    private <S, T> TableColumn<S, String> createColumn(String title, Function<S, T> property) {
        TableColumn<S, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> {
            T value = property.apply(cellData.getValue());
            if (value != null) {
                return new SimpleStringProperty(value.toString());
            } else {
                return new SimpleStringProperty("");
            }
        });
        column.setResizable(false);
        return column;
    }



    //Maybe dont need this if I toLowerCase for search and then output the country/customer from set...
    // static String toProperCase(String input) {
    //     if (input == null || input.isEmpty()) {
    //         return null;
    //     }
    //     String[] words = input.trim().split("\\s+");
    //     StringBuilder sb = new StringBuilder();
    //     for (String word : words) {
    //         sb.append(Character.toUpperCase(word.charAt(0)))
    //           .append(word.substring(1).toLowerCase())
    //           .append(" ");
    //     }
    //     return sb.toString().trim();
    // }
}  