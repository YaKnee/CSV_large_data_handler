package orders.CSVHandler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import orders.OrderObjects.Order;


public class CSVDataReaderTest {
    @Test
    public void testCreateOrders() {
        String file = "data/SuperStoreOrders.csv";
        ArrayList<Order> orders = CSVDataReader.createOrders(file);
        assertNotNull(orders);
        assertEquals(9994, orders.size());
        assertNotEquals(0, orders.size());
    }

    @Test
    public void testReplaceCommaAndParse() {
        assertEquals(1234, CSVDataReader.replaceCommaAndParse("1,234", 1));
        assertEquals(-69, CSVDataReader.replaceCommaAndParse("aaaaaaaaaaa", 1));
    }
}
