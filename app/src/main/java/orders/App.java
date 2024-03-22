package orders;

import com.opencsv.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//TO DO
////Add Threading for Buttons
////Selector for States/Customers + Search bars
////View summary of orders of customer
////Customers per State                                 (done!)
////Average Sales amounts of the orders                 (done?)
////Customer with most sales                            (done!)
////Count segments
////total sales per year
////total sales per region
////****Testing****
////JavaDocs
////Make Pretty

public class App extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        ArrayList<Order> orders = readCSVData("data/SuperStoreOrders.csv");
        Set<Location> uniqueLocations = extractLocations(orders);


        //--------------------------Customers per State---------------------
        Set<String> uniqueStates = new HashSet<>();
        for (Location location : uniqueLocations) {
            uniqueStates.add(location.state());
        }

        Map<String, Long> customersPerState = new HashMap<>();
        int i = 0;
        for (String state : uniqueStates) {
            long customersInState = orders.stream()
                    .filter(order -> state.equals(order.location().state()))
                    .map(Order::customer)
                    .map(Customer::customerId)
                    .distinct()
                    .count();
            customersPerState.put(state, customersInState);
            //System.out.println(++i + ": State: " + state + ": count: " + customersInState);
        }

        //-------------------------------Search by state for total Orders--------------------
        Label stateLabel=new Label("State:"); 
        TextField stateInput=new TextField();
        Button stateSearchBtn = new Button("Search");
        stateSearchBtn.getStyleClass().add(".button");
        Label stateOutput = new Label();

        stateSearchBtn.setOnAction(e-> {
            String state = stateInput.getText();
            if (state == null || state.isEmpty()) {
                stateOutput.setText("Empty search bar.");
            }else {
                boolean stateExists = uniqueLocations.stream()
                .anyMatch(location -> state.equalsIgnoreCase(location.state()));
                if (stateExists) {
                    int ordersPerState = (int) orders.stream()
                    .filter(order -> state.equalsIgnoreCase(order.location().state()))
                    .count();
                    System.out.println("Orders in " + toProperCase(state) + ": " + ordersPerState);
                    stateOutput.setText("Orders in " + toProperCase(state) + ": " + ordersPerState);
                } else {
                    System.out.println("State not found.");
                    stateOutput.setText("State not found.");
                }
            }
        });

        //--------------------Find Customer with most sales----------------
        Map<String, Integer> salesPerCustomer = new HashMap<>();
        for (Order order : orders) {
            String customerId = order.customer().customerId();
            int sales = order.sales();
            salesPerCustomer.put(customerId, salesPerCustomer.getOrDefault(customerId, 0) + sales);
        }
        
        String customerWithMostSales = "";
        int mostSales = 0;

        for (Map.Entry<String, Integer> entry : salesPerCustomer.entrySet()) {
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
        Label customerLabel=new Label("Customer Count:"); 
        TextField customerInput=new TextField();
        Button customerSearchBtn = new Button("Search");
        customerSearchBtn.getStyleClass().add(".button");
        Label customerOutput = new Label();

        customerSearchBtn.setOnAction(e-> {
            String customerName = toProperCase(customerInput.getText());
            if (customerName == null || customerName.isEmpty()) {
                customerOutput.setText("Unknown Customer");
            }else {
                if (salesPerCustomer.containsKey(customerName)) {
                    long totalSales = salesPerCustomer.get(customerName);
                    System.out.println(customerName + " had " + totalSales + " total sales.");
                    customerOutput.setText("Customer found: " + toProperCase(customerName));
                } else {
                    customerOutput.setText("Customer not found.");
                }
            }
        });

        //--------------------------Total Average Sales-------------------------
        Button salesBtn = new Button("Average Sales");
        salesBtn.getStyleClass().add(".button");

        salesBtn.setOnAction(e -> {
            long totalSales = orders.stream().mapToLong(Order::sales).sum();
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
        root.add(stateInput, 1, 0);
        root.add(stateSearchBtn, 2, 0);
        root.add(stateOutput, 1, 1);
        root.add(salesBtn, 1, 2);
        root.add(customerLabel, 0, 3);
        root.add(customerInput, 1, 3);
        root.add(customerSearchBtn, 2, 3);
        root.add(customerOutput, 1, 4);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("SuperStore Data");
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
        //System.out.println(new App().getGreeting());
        //List<Order> orders = readCSVData("data/SuperStoreOrders.csv");
    }

    private static ArrayList<Order> readCSVData(String file) {
        ArrayList<Order> allOrders = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(1)
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            csvReader.iterator().forEachRemaining(order -> {
                try {
                    int rowID = Integer.parseInt(order[0]);
                                                            //orderId, orderDate, shipDate, Ship Mode
                    ShippingOrder shipOrder = new ShippingOrder(order[1],order[2],order[3], order[4]);
                                                    //customerId, Name, Segment
                    Customer customer = new Customer(order[5], order[6], order[7]);

                                                    //Country, state,     State,    Post Code,  Region
                    Location location = new Location(order[8], order[9], order[10], order[11], order[12]);
                                                //productId, category, sub-cat,      name
                    Product product = new Product(order[13], order[14], order[15], order[16]);
                    //Commas were randomly interpolated in the data that weren't shown in Excel,
                    //so replace with blanks
                    int sales = replaceCommaAndParse(order[17], rowID);
                    int quantity = replaceCommaAndParse(order[18], rowID);
                    int discount = replaceCommaAndParse(order[19], rowID);
                    int profit = replaceCommaAndParse(order[20], rowID);
                    Order fullOrder = new Order(shipOrder, customer, location,
                            product, sales, quantity, discount, profit);
                    allOrders.add(fullOrder);
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse data in Row ID: " + order[0]);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allOrders;
    }

    private static int replaceCommaAndParse(String orderItem, int rowID){
        try{
            return Integer.parseInt(orderItem.replace(",",""));
        }catch (NumberFormatException e){
            System.err.println("Failed to parse string: " + orderItem + ", in row with id: " + rowID);
            return 0; ///////////////////////////////////////////////////////////////////////////Change to Optional?
        }
    }
    private static Set<Location> extractLocations(ArrayList<Order> allOrders) {
        Set<Location> uniqueLocations = new HashSet<>();
        for (Order order : allOrders) {
            uniqueLocations.add(order.location());
        }
        return uniqueLocations;
    }
    

    //Maybe dont need this if I toLowerCase for search and then output the country/customer from set...
    private static String toProperCase(String input) {
        StringBuilder properCase = new StringBuilder();
        boolean makeUpperCase = true;
        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                properCase.append(c);
                makeUpperCase = true;
            } else if (Character.isLetter(c)) {
                if (makeUpperCase) {
                    properCase.append(Character.toUpperCase(c));
                    makeUpperCase = false;
                } else {
                    properCase.append(Character.toLowerCase(c));
                }
            }
        }
        return properCase.toString();
    }
    public String getGreeting() {
        return "Hello World!";
    }
}  