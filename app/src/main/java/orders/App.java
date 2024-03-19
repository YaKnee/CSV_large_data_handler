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
import java.io.InputStream;
import java.util.ArrayList;


public class App extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        ArrayList<Order> orders = readCSVData("data/SuperStoreOrders.csv");

        ///////////////////////////////////////////////////////////mess around with other layouts


        Label cityLabel=new Label("City:"); 
        TextField cityInput=new TextField();
        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add(".button");
        Label cityOutput = new Label();
        searchBtn.setOnAction(e-> {
            System.out.println("You entered City: " + cityInput.getText());
            cityOutput.setText(toProperCase(cityInput.getText()));
        });

        Button salesBtn = new Button("Average Sales");
        salesBtn.getStyleClass().add(".button");
        salesBtn.setOnAction(e -> {
            long totalSales = orders.stream().mapToLong(Order::sales).sum();
            double averageSales = (double) totalSales / orders.size();
            System.out.println("Total Average Sales: " + averageSales);
        });

        ////////////////////////////////////////////////////////////Change to a search/select
        Button customerPerStateBtn = new Button("Customers in Kentucky");
        customerPerStateBtn.getStyleClass().add(".button");
        customerPerStateBtn.setOnAction(e -> {
            int kentuckyCount = (int) orders.stream()
                    .map(Order::location)
                    .filter(location -> "Kentucky".equals(location.state()))
                    .count();
            System.out.println("Customers in Kentucky: " + kentuckyCount);
        });


        GridPane root = new GridPane();
        root.setMinSize(400, 200);
        root.setPadding(new Insets(10, 10, 10, 10)); 
        root.setVgap(5); 
        root.setHgap(5);       
        root.setAlignment(Pos.CENTER); 
        root.add(cityLabel, 0, 0);
        root.add(cityInput, 1, 0);
        root.add(cityOutput, 2, 0);
        root.add(searchBtn, 0, 1);
        root.add(salesBtn, 1, 2);
        root.add(customerPerStateBtn, 1, 3);
        cityOutput.setStyle("-fx-background-colour: #fffaf0;");
        root.setStyle("-fx-background-colour: #6495ed");
        // root.addRow(0,cityLabel,cityInput,searchBtn,cityOutput);
        // root.addRow(1, salesBtn);
        // root.addRow(2, customerPerStateBtn);
        // root.getChildren().addAll(cityLabel,cityInput,searchBtn, cityOutput,salesBtn, customerPerStateBtn);
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
                                                    //customerId, Name, Segment
                    Customer customer = new Customer(order[5], order[6], order[7]);
                    String orderDate = order[2];
                    String shippingMode = order[4];
                                                    //City,     State,      Region
                    Location location = new Location(order[9], order[10], order[12]);
                                                //productId, category, sub-cat,      name
                    Product product = new Product(order[13], order[14], order[15], order[16]);
                    //Commas were randomly interpolated in the data that weren't shown in Excel,
                    //so replace with blanks
                    int sales = replaceCommaAndParse(order[17], order[0]);
                    int quantity = replaceCommaAndParse(order[18], order[0]);
                    int discount = replaceCommaAndParse(order[19], order[0]);
                    int profit = replaceCommaAndParse(order[20], order[0]);
                    Order fullOrder = new Order(customer, orderDate, shippingMode, location,
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
    private static int replaceCommaAndParse(String orderItem, String rowID){
        try{
            return Integer.parseInt(orderItem.replace(",",""));
        }catch (NumberFormatException e){
            System.err.println("Failed to parse string: " + orderItem + ", in row with id: " + rowID);
            return 0; ///////////////////////////////////////////////////////////////////////////Change to Optional?
        }
    }

    public static String toProperCase(String input) { 
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }
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
            } else {
                throw new IllegalArgumentException("Input contains special characters/numbers: " + c);
            }
        }
        return properCase.toString();
    }
    public String getGreeting() {
        return "Hello World!";
    }
}  