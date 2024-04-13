package orders.OrderObjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    private Product product1 = new Product("P001", "Electronics", "Smartphones", "iPhone");
    private Product product2 = new Product("P001", "Electronics", "Smartphones", "iPhone");
    private Product product3 = new Product("P001", "Electronics", "Smartphones", "iPhone 14");
    @Test
    public void testProductRecord() {
        assertEquals("P001", product1.id());
        assertEquals("Electronics", product1.category());
        assertEquals("Smartphones", product1.subCategory());
        assertEquals("iPhone", product1.productName());
    }

    @Test
    public void testProductRecordWithNullValues() {
        Product product = new Product(null, null, null, null);
        assertNull(product.id());
        assertNull(product.category());
        assertNull(product.subCategory());
        assertNull(product.productName());
    }

    @Test
    public void testProductRecordWithEmptyValues() {
        Product product = new Product("", "", "", "");
        assertEquals("", product.id());
        assertEquals("", product.category());
        assertEquals("", product.subCategory());
        assertEquals("", product.productName());
    }

    @Test
    public void testHashCode() {
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(product1.equals(product2));
        assertFalse(product1.equals(product3));
        assertTrue(product1.category().equals(product3.category()));
        assertTrue(product1.subCategory().equals(product3.subCategory()));
        assertFalse(product1.productName().equals(product3.productName()));
    }

    @Test
    public void testToString() {
        assertEquals("Product[id=P001, category=Electronics, subCategory=Smartphones, productName=iPhone]", product1.toString());
        assertEquals("Product[id=P001, category=Electronics, subCategory=Smartphones, productName=iPhone 14]", product3.toString());
    }
}