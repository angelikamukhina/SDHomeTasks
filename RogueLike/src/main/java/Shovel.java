/**
 * class for shovel objects, they move randomly inside map rooms
 */
class Shovel extends Character {

    public Shovel(String name, int health, boolean isBot, String display) {
        super(name, health, isBot, display);
    }

    /**
     * get one of Movement enum instances
     * @return Movement instance
     */
    @Override
    public Movement movement() {
        return Movement.values()[(int) (Math.random() * Movement.values().length)];
    }
}
