package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationTest {
    @Test
    public void locationToList() throws Exception {
        Location location = new Location(1, 2);
        assertEquals(7, location.locationToList(3));
    }

    @Test
    public void staticLocationToList() throws Exception {
        Location location = new Location(1, 2);
        assertEquals(7, Location.locationToList(location.x, location.y, 3));
    }

    @Test
    public void equals() throws Exception {
        Location location = new Location(1, 2);
        Location newLocation = new Location(location);
        assertEquals(newLocation, location);
    }
}