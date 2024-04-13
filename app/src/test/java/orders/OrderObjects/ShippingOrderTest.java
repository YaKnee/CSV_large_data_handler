package orders.OrderObjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShippingOrderTest {
    private ShippingOrder shippingOrder1 = new ShippingOrder("O001", "2022-01-01", "2022-01-05", "Express");
    private ShippingOrder shippingOrder2 = new ShippingOrder("O001", "2022-01-01", "2022-01-05", "Express");
    private ShippingOrder shippingOrder3 = new ShippingOrder("P001", "2023-01-01", "2023-01-05", "Express");
    @Test
    public void testShippingOrderRecord() {
        assertEquals("O001", shippingOrder1.orderId());
        assertEquals("2022-01-01", shippingOrder1.orderDate());
        assertEquals("2022-01-05", shippingOrder1.shipDate());
        assertEquals("Express", shippingOrder1.shipMode());
    }

    @Test
    public void testShippingOrderRecordWithNullValues() {
        ShippingOrder shippingOrder = new ShippingOrder(null, null, null, null);
        assertNull(shippingOrder.orderId());
        assertNull(shippingOrder.orderDate());
        assertNull(shippingOrder.shipDate());
        assertNull(shippingOrder.shipMode());
    }

    @Test
    public void testShippingOrderRecordWithEmptyValues() {
        ShippingOrder shippingOrder = new ShippingOrder("", "", "", "");
        assertEquals("", shippingOrder.orderId());
        assertEquals("", shippingOrder.orderDate());
        assertEquals("", shippingOrder.shipDate());
        assertEquals("", shippingOrder.shipMode());
    }

    @Test
    public void testHashCode() {
        assertEquals(shippingOrder1.hashCode(), shippingOrder2.hashCode());
        assertNotEquals(shippingOrder1.hashCode(), shippingOrder3.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(shippingOrder1.equals(shippingOrder2));
        assertFalse(shippingOrder1.equals(shippingOrder3));
        assertTrue(shippingOrder1.shipMode().equals(shippingOrder3.shipMode()));
    }

    @Test
    public void testToString() {
        assertEquals("ShippingOrder[orderId=O001, orderDate=2022-01-01, shipDate=2022-01-05, shipMode=Express]", shippingOrder1.toString());
    }
}