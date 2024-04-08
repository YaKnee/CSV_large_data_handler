package orders.OrderObjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    final private ShippingOrder shippingOrder = new ShippingOrder("O001", "2022-01-01", "2022-01-05", "Express");
    final private Customer customer = new Customer("C001", "John Doe", "Retail");
    final private Location location = new Location("USA", "New York", "NY", "10001", "Northeast");
    final private Product product = new Product("P001", "Electronics", "TV", "Samsung 4K UHD TV");
    final private Order order1 = new Order(1, shippingOrder, customer, location, product, 1000, 2, 10, 200);
    final private Order order2 = new Order(1, shippingOrder, customer, location, product, 1000, 2, 10, 200);
    @Test
    public void testOrderRecord() {

        assertEquals(1, order1.rowID());
        assertEquals(shippingOrder, order1.shipOrder());
        assertEquals(customer, order1.customer());
        assertEquals(location, order1.location());
        assertEquals(product, order1.product());
        assertEquals(1000, order1.sales());
        assertEquals(2, order1.quantity());
        assertEquals(10, order1.discount());
        assertEquals(200, order1.profit());
    }

    @Test
    public void testHashCode() {
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(order1.equals(order2));
    }

    @Test
    public void testToString() {
        assertEquals("Order[rowID=1, " 
        + "shipOrder=ShippingOrder[orderId=O001, orderDate=2022-01-01, shipDate=2022-01-05, shipMode=Express], "
        + "customer=Customer[customerId=C001, name=John Doe, segment=Retail], " 
        + "location=Location[country=USA, city=New York, state=NY, postCode=10001, region=Northeast], " 
        + "product=Product[id=P001, category=Electronics, subCategory=TV, productName=Samsung 4K UHD TV], "
        + "sales=1000, quantity=2, discount=10, profit=200]", order1.toString());
    }
}