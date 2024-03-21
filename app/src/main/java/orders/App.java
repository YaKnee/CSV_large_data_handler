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
import java.util.HashSet;
import java.util.Set;

//TO DO
////Add Threading for Buttons
////Selector for States/Customers + Search bars
////View summary of orders of customer
////Average Sales amounts of the orders                 (done?)
////Customer with most sales
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
                    int customersInState = (int) orders.stream()
                    .filter(order -> state.equalsIgnoreCase(order.location().state()))
                    .count();
                    System.out.println("Customers in " + toProperCase(state) + ": " + customersInState);
                    stateOutput.setText("Customers in " + toProperCase(state) + ": " + customersInState);
                } else {
                    System.out.println("State not found.");
                    stateOutput.setText("State not found.");
                }
            }
        });

        Label customerLabel=new Label("Customer Count:"); 
        TextField customerInput=new TextField();
        Button customerSearchBtn = new Button("Search");
        customerSearchBtn.getStyleClass().add(".button");
        Label customerOutput = new Label();
        customerSearchBtn.setOnAction(e-> {
            if (customerInput.getText() == null || customerInput.getText().isEmpty()) { ////////////////or not found in search
                customerOutput.setText("Unknown Customer");
            }else {
                
                System.out.println("You entered Customer: " + customerInput.getText());
                customerOutput.setText(toProperCase(customerInput.getText()));
            }
        });

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