package orders.OrderObjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {

    @Test
    public void testLocationRecord() {
        // Test if the Location record can be created with the correct values
        Location location = new Location("USA", "New York", "NY", "10001", "Northeast");
        assertEquals("USA", location.country());
        assertEquals("New York", location.city());
        assertEquals("NY", location.state());
        assertEquals("10001", location.postCode());
        assertEquals("Northeast", location.region());
    }

    @Test
    public void testLocationRecordWithNullValues() {
        // Test if the Location record can be created with null values
        Location location = new Location(null, null, null, null, null);
        assertNull(location.country());
        assertNull(location.city());
        assertNull(location.state());
        assertNull(location.postCode());
        assertNull(location.region());
    }

    @Test
    public void testLocationRecordWithEmptyValues() {
        // Test if the Location record can be created with empty values
        Location location = new Location("", "", "", "", "");
        assertEquals("", location.country());
        assertEquals("", location.city());
        assertEquals("", location.state());
        assertEquals("", location.postCode());
        assertEquals("", location.region());
    }

    @Test
    public void testHashCode() {
        // Test if the hashCode method returns the same value for two equal objects
        Location location1 = new Location("USA", "New York", "NY", "10001", "Northeast");
        Location location2 = new Location("USA", "New York", "NY", "10001", "Northeast");
        assertEquals(location1.hashCode(), location2.hashCode());
    }

    @Test
    public void testEquals() {
        // Test if the equals method returns true for two equal objects
        Location location1 = new Location("USA", "New York", "NY", "10001", "Northeast");
        Location location2 = new Location("USA", "New York", "NY", "10001", "Northeast");
        assertTrue(location1.equals(location2));
    }

    @Test
    public void testToString() {
        // Test if the toString method returns the correct string representation
        Location location = new Location("USA", "New York", "NY", "10001", "Northeast");
        assertEquals("Location[country=USA, city=New York, state=NY, postCode=10001, region=Northeast]", location.toString());
    }
}