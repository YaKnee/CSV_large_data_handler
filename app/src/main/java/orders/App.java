package orders;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.application.Application;
// import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
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
    
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * The main entry point for the JavaFX application. This method is called when the application is launched.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception If an error occurs during application start-up, an exception is thrown.
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
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


            //  Parent root = FXMLLoader.load(getClass().getResource("/welcome.fxml"));
            // scene.getStylesheets().add("stylesheet.css");
            Scenes scenes = new Scenes(stage);
            Scene welcomeScene = scenes.createWelcomeScene();
            Image icon = new Image("icon.png");
            stage.getIcons().add(icon);
            stage.setScene(welcomeScene);
            stage.setTitle("SuperStore Data");
            stage.setOnCloseRequest(event -> System.exit(0));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a map of sales data grouped by a specific function.
     * 
     * @param orders list of orders to generate sales data from
     * @param groupingFunction function to group orders by
     * @return map of sales data
     */
    public static Map<String, Long> generateSalesMap(ArrayList<Order> orders, Function<Order, String> groupingFunction) {
        return orders.stream().collect(Collectors.groupingBy(
                            groupingFunction, 
                            Collectors.summingLong(Order::sales)));
    }

    /**
     * Generates a map of customers and their count.
     * 
     * @param orders list of orders to generate customer count from
     * @param groupingFunction function to group orders by
     * @return map of customers and their count
     */
    public static Map<String, Integer> generateCustomersMap(ArrayList<Order> orders, Function<Order, String> groupingFunction) {
        return orders.stream()
        .collect(Collectors.groupingBy(groupingFunction,
            Collectors.mapping(order -> 
                order.customer().name(), /////////////////////////////change back to customerID?
                // Collectors.collectingAndThen(
                //     Collectors.counting(),
                //     Long::intValue
                Collectors.summingInt(order -> 1)
                )));
    }
}
