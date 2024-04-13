package orders;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerPerformanceTest {

    @Test
    public void testCustomerPerformance() {
        CustomerPerformance customer = new CustomerPerformance("John Doe", 
            "123", "Segment A", 5, 1000, 200);

        assertEquals("John Doe", customer.getName());
        assertEquals("123", customer.getId());
        assertEquals("Segment A", customer.getSegment());
        assertEquals("5", customer.getOrders());
        assertEquals("1000", customer.getSales());
        assertEquals("200", customer.getProfits());
    }

    @Test
    public void testNulls() {
        CustomerPerformance customer = new CustomerPerformance(null, "123",
    null, 5, 1000, 200);

        assertNull(customer.getName());
        assertEquals("123", customer.getId());
        assertNull(customer.getSegment());
        assertEquals("5", customer.getOrders());
        assertEquals("1000", customer.getSales());
        assertEquals("200", customer.getProfits());
    }
}