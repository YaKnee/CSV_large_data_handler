package orders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
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
import orders.OrderObjects.*;




//TO DO
////Add Threading for Buttons
////Selector for States/Customers + Search bars         (done!)
////View summary of orders of customer                  (done!)
////Customers per State                                 (done!)
////Average Sales amounts of the orders                 (done?)
////Customer with most sales                            (done!)
////Count segments                                      (done!)
////total sales per year
////total sales per region                              (done!)
////****Testing****
////JavaDocs
////Make Pretty

public class App extends Application{


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


        //--------------------------------Customer with Most Sales-------------------------------
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
       
        //--------------------Find Customer------------------------------
        TableView<Order> table = new TableView<>();
        table.setVisible(false);
        Label tableHeading = new Label();
        Label customerLabel=new Label("Customer Name:"); 
        ComboBox<String> nameComboBox = new ComboBox<String>();
        autoFillCombBox(nameComboBox, customerNameAndID.keySet());
        Button customerSearchBtn = new Button("Search");
        customerSearchBtn.getStyleClass().add(".button");
        Label customerOutput = new Label();
        customerSearchBtn.setOnAction(e-> {
            String name = toProperCase(nameComboBox.getEditor().getText());
            if (name == null || name.isEmpty()) {
                tableHeading.setText("Empty search bar.");
                table.setVisible(false);
            }else {
                boolean nameExists = false;
                for (String key : customerNameAndID.keySet()) {
                    if (key.equalsIgnoreCase(name)) {
                        nameExists = true;
                        break;
                    }
                }
                if (nameExists) {
                    tableHeading.setText(toProperCase(name));
                    ObservableList<Order> data = FXCollections.observableArrayList(customerOrders.get(customerNameAndID.get(toProperCase(name))));
                    populateTable(table, data);
                } else {
                    tableHeading.setText("Customer not found.");
                    table.setVisible(false);
                }
            }
        });


        //Iterate through segments to find values or keep as hardcode?
        String[] segments = {"Consumer", "Corporate", "Home Office"};
        int consCustomers = 0;
        int corpCustomers = 0;
        int offcCustomers = 0;
        for (Order order : orders) {
            String segment = order.customer().segment();
            if (segment.equals(segments[0])) {
                consCustomers++;
            } else if (segment.equals(segments[1])) {
                corpCustomers++;
            } else if (segment.equals(segments[2])) {
                offcCustomers++;
            }
        }
        System.out.println("cons: " + consCustomers);
        System.out.println("corp: " + corpCustomers);
        System.out.println("home: " + offcCustomers);



        //Just hard code the 4 years???
        Set<String> uniqueYears = new HashSet<>();
        for (Order order : orders) {
            uniqueYears.add(order.shipOrder().orderDate().substring(order.shipOrder().orderDate().length() - 4));
        }


        Label yearLabel = new Label("Orders in Year: ");
        ComboBox<String> yearComboBox = new ComboBox<String>();
        autoFillCombBox(yearComboBox, uniqueYears);
        Button yearSearchBtn = new Button("Search");
        yearSearchBtn.getStyleClass().add(".button");
        Label yearOutput = new Label();
        yearSearchBtn.setOnAction(e-> {
            String year = yearComboBox.getEditor().getText();
            if (year == null || year.isEmpty()) {
                yearOutput.setText("Empty search bar.");
            }else {
                boolean yearExists = uniqueYears.stream()
                .anyMatch(date -> year.equals(date.substring(date.length() - 4)));
                if (yearExists) {
                    int ordersInYear = (int) orders.stream()
                    .filter(order -> year.equals(order.shipOrder().orderDate().substring(year.length())))
                    .count();
                    yearOutput.setText("Orders in " + year + ": " + ordersInYear);
                    System.out.println("Orders in " + year + ": " + ordersInYear);
                } else {
                    System.out.println("Year not found.");
                    yearOutput.setText("Year not found.");
                }
            }
        });

        //-------------------------------Search by state for total Orders and customers--------------------

        Set<String> uniqueStates = new HashSet<>();
        for (Order order : orders) {
            uniqueStates.add(order.location().state());
        }

        Label stateLabel=new Label("State:"); 
        ComboBox<String> stateComboBox = new ComboBox<String>();
        autoFillCombBox(stateComboBox, uniqueStates);
        Button stateSearchBtn = new Button("Search");
        stateSearchBtn.getStyleClass().add(".button");
        Label stateOutput = new Label();

        stateSearchBtn.setOnAction(e-> {
            String state = stateComboBox.getEditor().getText();
            if (state == null || state.isEmpty()) {
                stateOutput.setText("Empty search bar.");
            }else {
                boolean stateExists = uniqueStates.stream()
                .anyMatch(location -> state.equalsIgnoreCase(location));
                if (stateExists) {
                    int ordersPerState = (int) orders.stream()
                    .filter(order -> state.equalsIgnoreCase(order.location().state()))
                    .count();
                    long customersInState = orders.stream()
                    .filter(order -> state.equals(order.location().state()))
                    .map(Order::customer)
                    .map(Customer::customerId)
                    .distinct()
                    .count();
                    stateOutput.setText(toProperCase(state) + "{Orders=" + ordersPerState + ", Customers=" + customersInState + "}");
                    System.out.println(toProperCase(state) + "{Orders=" + ordersPerState + ", Customers=" + customersInState + "}");
                } else {
                    System.out.println("State not found.");
                    stateOutput.setText("State not found.");
                }
            }
        });

        //--------------------------Sales per Region-------------------
        /////////////////////////////////////////////////////////////////////Use Collectors for other methods
        Map<String, Long> salesPerRegion = orders.stream()
        .collect(Collectors.groupingBy(order -> order.location().region(), Collectors.summingLong(Order::sales)));
        salesPerRegion.forEach((region, sales) -> System.out.println(region + " Sales: " + sales));

        //--------------------------Total Average Sales-------------------------
        Button salesBtn = new Button("Average Sales");
        salesBtn.getStyleClass().add(".button");

        salesBtn.setOnAction(e -> {
            long totalSales = orders.stream().mapToLong(Order::sales).sum();
            //System.out.println("Total Sales: " + totalSales);
            double averageSales = (double) totalSales / orders.size();
            System.out.println("Total Average Sales: " + averageSales);
        });

        ///////////////////////////////////////////////////////////mess around with other layouts
        GridPane root = new GridPane();
        root.setMinSize(400, 200);
        root.setPadding(new Insets(10, 10, 10, 10)); 
        root.setVgap(5); 
        root.setHgap(5);       
        root.setAlignment(Pos.CENTER); 

        root.add(stateLabel, 0, 0);
        root.add(stateComboBox, 1, 0);
        root.add(stateSearchBtn, 2, 0);
        root.add(stateOutput, 1, 1);
        root.add(salesBtn, 1, 2);
        root.add(customerLabel, 0, 3);
        root.add(nameComboBox, 1, 3);
        root.add(customerSearchBtn, 2, 3);
        root.add(customerOutput, 1, 4);
        root.add(yearLabel, 0, 5);
        root.add(yearComboBox, 1, 5);
        root.add(yearSearchBtn, 2, 5);
        root.add(yearOutput, 1, 6);
        root.add(tableHeading, 1, 7);
        root.add(table, 0, 8, 3, 10);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("SuperStore Data");
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }


    public void autoFillCombBox(ComboBox<String> cb, Collection<String> list) {
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

        table.setItems(data);

        dateCol.getColumns().addAll(orderIDCol,orderDateCol,shipDateCol,shippingModeCol);
        dateCol.setResizable(false);
        customerCol.getColumns().addAll(custNameCol,segmentCol);
        customerCol.setResizable(false);
        locationCol.getColumns().addAll(cityCol, stateCol, regionCol);
        locationCol.setResizable(false);
        table.getColumns().addAll(countCol,dateCol, customerCol, locationCol);
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
        return column;
    }



    //Maybe dont need this if I toLowerCase for search and then output the country/customer from set...
    static String toProperCase(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        String[] words = input.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)))
              .append(word.substring(1).toLowerCase())
              .append(" ");
        }
        return sb.toString().trim();
    }
}  