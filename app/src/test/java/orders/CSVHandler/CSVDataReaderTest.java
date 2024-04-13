package orders.CSVHandler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.control.Label;
import java.util.ArrayList;
import orders.OrderObjects.Order;


public class CSVDataReaderTest {
    private Label error;

    @Test
    public void testParseOrderObjects() {
        // Create a test order array
        String[] order = new String[] {
            "1", "orderId", "orderDate", "shipDate", "shipMode", 
            "customerId", "customerName", "segment", 
            "country", "city", "state", "postCode", "region", 
            "productId", "category", "subCategory", "productName", 
            "100", "2", "10", "50"
        };
        ArrayList<Order> list = new ArrayList<>();
        CSVDataReader.parseOrderObjects(list, order);
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    private void testCreateOrders() {
        String file = "data/SuperStoreOrders.csv";

        ArrayList<Order> orders = CSVDataReader.createOrders(file, error);
        assertNotNull(orders);
        assertEquals(9994, orders.size());
        assertNotEquals(0, orders.size());
    }

    @Test
    public void testCreateCSVReader() {
        assertThrows(RuntimeException.class, () -> {
            CSVDataReader.createCSVReader("OoooGaBooGa", error);
        });
    }

    @Test
    private void testReplaceCommaAndParse() {
        assertEquals(1234, CSVDataReader.replaceCommaAndParse("1,234", 1));
        try {
            CSVDataReader.replaceCommaAndParse("aaaa", 1);
        } catch (NumberFormatException e) {
            assertEquals("Failed to parse string: aaaa, in row with id: 1", e.getMessage());
        }
    }
}
