package orders.OrderObjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShippingOrderTest {

    @Test
    public void testShippingOrderRecord() {
        // Test if the ShippingOrder record can be created with the correct values
        ShippingOrder shippingOrder = new ShippingOrder("O001", "2022-01-01", "2022-01-05", "Express");
        assertEquals("O001", shippingOrder.orderId());
        assertEquals("2022-01-01", shippingOrder.orderDate());
        assertEquals("2022-01-05", shippingOrder.shipDate());
        assertEquals("Express", shippingOrder.shipMode());
    }

    @Test
    public void testShippingOrderRecordWithNullValues() {
        // Test if the ShippingOrder record can be created with null values
        ShippingOrder shippingOrder = new ShippingOrder(null, null, null, null);
        assertNull(shippingOrder.orderId());
        assertNull(shippingOrder.orderDate());
        assertNull(shippingOrder.shipDate());
        assertNull(shippingOrder.shipMode());
    }

    @Test
    public void testShippingOrderRecordWithEmptyValues() {
        // Test if the ShippingOrder record can be created with empty values
        ShippingOrder shippingOrder = new ShippingOrder("", "", "", "");
        assertEquals("", shippingOrder.orderId());
        assertEquals("", shippingOrder.orderDate());
        assertEquals("", shippingOrder.shipDate());
        assertEquals("", shippingOrder.shipMode());
    }

    @Test
    public void testHashCode() {
        // Test if the hashCode method returns the same value for two equal objects
        ShippingOrder shippingOrder1 = new ShippingOrder("O001", "2022-01-01", "2022-01-05", "Express");
        ShippingOrder shippingOrder2 = new ShippingOrder("O001", "2022-01-01", "2022-01-05", "Express");
        assertEquals(shippingOrder1.hashCode(), shippingOrder2.hashCode());
    }

    @Test
    public void testEquals() {
        // Test if the equals method returns true for two equal objects
        ShippingOrder shippingOrder1 = new ShippingOrder("O001", "2022-01-01", "2022-01-05", "Express");
        ShippingOrder shippingOrder2 = new ShippingOrder("O001", "2022-01-01", "2022-01-05", "Express");
        assertTrue(shippingOrder1.equals(shippingOrder2));
    }

    @Test
    public void testToString() {
        // Test if the toString method returns the correct string representation
        ShippingOrder shippingOrder = new ShippingOrder("O001", "2022-01-01", "2022-01-05", "Express");
        assertEquals("ShippingOrder[orderId=O001, orderDate=2022-01-01, shipDate=2022-01-05, shipMode=Express]", shippingOrder.toString());
    }
}