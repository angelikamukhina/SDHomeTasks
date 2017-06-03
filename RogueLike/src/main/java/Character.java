/**
 * Base class for characters classes.
 */
abstract class Character {

    final String name;
    private final boolean isBot;
    boolean isAlive;
    private final String display;
    protected int power = 50;
    protected int safety = 50;
    protected int health;

    public Character(String name, int health, boolean isBot, String display) {
        this.name = name;
        this.health = health;
        this.isBot = isBot;
        this.isAlive = true;
        this.display = display;
    }

    /**
     * Is this character alive or dead.
     * @return true if character is alive
     */
    public boolean getIsAlive() {
        return this.isAlive;
    }

    /**
     * To get name of the character.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * If character is Shovel or Dog the method returns true, if Mole returns false.
     * @return look above
     */
    public boolean isBot() {
        return isBot;
    }

    /**
     * What symbol corresponds to the character.
     * @return symbol
     */
    public String getDisplay() {
        return display;
    }

    /**
     * for logging
     * @return string representation of character: it's name, state.
     */
    public String toString() {
        return "Creature(Name:" + this.name + " Health:" + this.health + ")";
    }

    /**
     * The method for attack, it's called when attacker tries to go to the location when this object is located.
     * @param attacker character that tries to go to this' location
     * @return
     */
    public void attack(Character attacker) {
        takeDamage(attacker);
        if (this.health <= 0) {
            this.isAlive = false;
            System.out.printf("%s(%s) was killed by %s(%s)\n", this.name, this.display, attacker.name, attacker.display);
        }

        if (attacker.health <= 0) {
            attacker.isAlive = false;
            System.out.printf("%s(%s) was killed by %s(%s)\n", attacker.name, attacker.display, this.name, this.display);
        }
    }

    /**
     * by default returns random direction for the character
     * @return instance of Movement enum
     */
    public Movement movement() {
        return Movement.values()[(int) (Math.random() * Movement.values().length)];
    }

    protected int takeDamage(Character attacker) {
        int damageTaken = 20 * attacker.power / safety;
        this.health -= damageTaken;

        if (this.health <= 0) {
            System.out.printf("(%s) took (%d) damage and died.\n", this.name, damageTaken);
            this.isAlive = false;
        } else {
            System.out.printf("(%s) took (%d) damage.\n", this.name, damageTaken);
        }
        return damageTaken;
    }

    public enum Movement {UP, DOWN, LEFT, RIGHT, HOLD}
}
