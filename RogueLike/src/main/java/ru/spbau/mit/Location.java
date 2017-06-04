package ru.spbau.mit;

/**
 * class for aggregating coordinates of point inside map
 */
class Location {
    int x;
    int y;

    Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Location(Location other) {
        this.x = other.x;
        this.y = other.y;
    }

    static int locationToList(int x, int y, int mapWidth) {
        return y * mapWidth + x;
    }

    int locationToList(int mapWidth) {
        return y * mapWidth + x;
    }

    /**
     * We override method for search in occupied by characters and walls locations
     *
     * @param o
     * @return true if both coordinates are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Location) {
            Location that = (Location) o;
            return that.x == x && that.y == y;
        } else return false;
    }
}
