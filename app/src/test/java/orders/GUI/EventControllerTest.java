// package orders.GUI;

// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;
// import java.util.ArrayList;
// import javafx.scene.chart.LineChart;
// import orders.OrderObjects.*;

// public class EventControllerTest {


//     @Test
//     public void testCreateChartForCategory() {
//         // Create a list of orders
//         ArrayList<Order> orders = new ArrayList<>();
//         Order order1 = new Order(1, new ShippingOrder("O001", "2022-01-01", "2022-01-05", "Express"), 
//             new Customer("C001", "John Doe", "Retail"), 
//             new Location("USA", "New York", "NY", "10001", "Northeast"), 
//             new Product("P001", "Electronics", "TV", "Samsung 4K UHD TV"), 1000, 2, 10, 200);
//         Order order2 = new Order(2, new ShippingOrder("O002", "2022-01-02", "2022-01-06", "Express"), 
//             new Customer("C002", "Jane Doe", "Retail"), 
//             new Location("USA", "Los Angeles", "CA", "90001", "West"), 
//             new Product("P002", "Electronics", "TV", "LG 4K UHD TV"), 1500, 3, 15, 300);
//         orders.add(order1);
//         orders.add(order2);

//         // Test for different parent and child categories
//         LineChart<String, Number> chart1 = EventController.createChartForCategory("Customers", "City", orders);
//         LineChart<String, Number> chart2 = EventController.createChartForCategory("Sales", "Region", orders);

//         // Add assertions to test the charts
//         assertNotNull(chart1);
//         assertNotNull(chart2);
//     }
// }