package orders.OrderObjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductRecord() {
        // Test if the Product record can be created with the correct values
        Product product = new Product("P001", "Electronics", "Smartphones", "iPhone 13");
        assertEquals("P001", product.id());
        assertEquals("Electronics", product.category());
        assertEquals("Smartphones", product.subCategory());
        assertEquals("iPhone 13", product.productName());
    }

    @Test
    public void testProductRecordWithNullValues() {
        // Test if the Product record can be created with null values
        Product product = new Product(null, null, null, null);
        assertNull(product.id());
        assertNull(product.category());
        assertNull(product.subCategory());
        assertNull(product.productName());
    }

    @Test
    public void testProductRecordWithEmptyValues() {
        // Test if the Product record can be created with empty values
        Product product = new Product("", "", "", "");
        assertEquals("", product.id());
        assertEquals("", product.category());
        assertEquals("", product.subCategory());
        assertEquals("", product.productName());
    }

    @Test
    public void testHashCode() {
        // Test if the hashCode method returns the same value for two equal objects
        Product product1 = new Product("P001", "Electronics", "Smartphones", "iPhone 13");
        Product product2 = new Product("P001", "Electronics", "Smartphones", "iPhone 13");
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    public void testEquals() {
        // Test if the equals method returns true for two equal objects
        Product product1 = new Product("P001", "Electronics", "Smartphones", "iPhone 13");
        Product product2 = new Product("P001", "Electronics", "Smartphones", "iPhone 13");
        Product product3 = new Product("P001", "Electronics", "TV", "Samsung 4K UHD TV");
        assertTrue(product1.equals(product2));
        assertFalse(product1.equals(product3));
        assertTrue(product1.category().equals(product3.category()));
    }

    @Test
    public void testToString() {
        // Test if the toString method returns the correct string representation
        Product product = new Product("P001", "Electronics", "Smartphones", "iPhone 13");
        assertEquals("Product[id=P001, category=Electronics, subCategory=Smartphones, productName=iPhone 13]", product.toString());
    }
}