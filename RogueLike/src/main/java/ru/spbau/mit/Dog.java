package ru.spbau.mit;

/**
 * One of the enemies in the game, object of Dog class
 * moves toward mole. It search direction that give
 * decrease in distance between mole and this object.
 */
class Dog extends Character {
    Location location = null;
    Cell moleCell = null;

    public Dog(String name, boolean isBot, String display, Cell moleCell, Location location) {
        super(name, isBot, display);
        this.moleCell = moleCell;
        this.location = location;
    }


    /**
     * get instance of Movement enum according to
     * minimum distance between mole and the dog
     *
     * @return instance of Movement enum
     */
    @Override
    public Movement movement() {
        Movement res = Movement.UP;
        int resDist = Integer.MAX_VALUE;
        for (Movement mv : Movement.values()) {
            Location xy = new Location(this.location);
            switch (mv) {
                case UP:
                    xy.y = location.y - 1;
                    break;
                case DOWN:
                    xy.y = location.y + 1;
                    break;
                case LEFT:
                    xy.x = location.x - 1;
                    break;
                case RIGHT:
                    xy.x = location.x + 1;
                    break;
                case HOLD:
                    break;
            }
            int dist = dist(xy);
            if (dist < resDist) {
                res = mv;
                resDist = dist;
            }
        }
        return res;
    }

    private int dist(Location location) {
        Location moleLocation = moleCell.getLocation();
        return (int) (Math.pow((double) (location.x - moleLocation.x), 2) +
                Math.pow((double) (location.y - moleLocation.y), 2)) +
                (int) (Math.random() * 20 - 10);
    }
}

