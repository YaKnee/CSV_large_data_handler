package orders.OrderObjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CustomerTest {

    @Test
    public void testCustomerRecord() {
        // Test if the Customer record can be created with the correct values
        Customer customer = new Customer("C001", "John Doe", "Retail");
        assertEquals("C001", customer.customerId());
        assertEquals("John Doe", customer.name());
        assertEquals("Retail", customer.segment());
    }

    @Test
    public void testCustomerRecordWithNullValues() {
        // Test if the Customer record can be created with null values
        Customer customer = new Customer(null, null, null);
        assertNull(customer.customerId());
        assertNull(customer.name());
        assertNull(customer.segment());
    }

    @Test
    public void testCustomerRecordWithEmptyValues() {
        // Test if the Customer record can be created with empty values
        Customer customer = new Customer("", "", "");
        assertEquals("", customer.customerId());
        assertEquals("", customer.name());
        assertEquals("", customer.segment());
    }
}