package orders.OrderObjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {
    private Location location1 = new Location("USA", "New York", "NY", "10001", "Northeast");
    private Location location2 = new Location("USA", "New York", "NY", "10001", "Northeast");
    private Location location3 = new Location("USA", "New York City", "NY", "11111", "Northeast");

    @Test
    public void testLocationRecord() {
        assertEquals("USA", location1.country());
        assertEquals("New York", location1.city());
        assertEquals("NY", location1.state());
        assertEquals("10001", location1.postCode());
        assertEquals("Northeast", location1.region());
    }

    @Test
    public void testLocationRecordWithNullValues() {
        Location location = new Location(null, null, null, null, null);
        assertNull(location.country());
        assertNull(location.city());
        assertNull(location.state());
        assertNull(location.postCode());
        assertNull(location.region());
    }

    @Test
    public void testLocationRecordWithEmptyValues() {
        Location location = new Location("", "", "", "", "");
        assertEquals("", location.country());
        assertEquals("", location.city());
        assertEquals("", location.state());
        assertEquals("", location.postCode());
        assertEquals("", location.region());
    }

    @Test
    public void testHashCode() {
        assertEquals(location1.hashCode(), location2.hashCode());
        assertNotEquals(location1, location3);
    }

    @Test
    public void testEquals() {
        assertTrue(location1.equals(location2));
        assertFalse(location1.equals(location3));
    }

    @Test
    public void testToString() {
        assertEquals("Location[country=USA, city=New York, state=NY, postCode=10001, region=Northeast]", location1.toString());
    }
}