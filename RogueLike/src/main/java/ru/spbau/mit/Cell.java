package ru.spbau.mit;

/**
 * Object of Cell class holds information about what is in this map cell:
 * is it wall, character, item or just empty cell
 */
class Cell {

    private Kind kind;
    private Item item;
    private Location location;
    private Character character;
    private String display;


    public Cell(Location location, Cell.Kind kind) {
        this.kind = kind;
        this.location = location;
        switch (kind) {
            case EMPTY:
                display = " ";
                break;
            case WALL:
                display = "#";
                break;
        }
    }

    public Cell(Location location, Cell.Kind kind, Object object) {
        this.location = location;
        this.kind = kind;
        switch (kind) {
            case ITEM:
                this.item = (Cell.Item) object;
                switch (item) {
                    case HELMET:
                        display = "^";
                        break;
                    case STONE:
                        display = "*";
                        break;
                    case WARM:
                        display = "w";
                        break;
                }
                break;
            case CHARACTER:
                character = (Character) object;
                display = character.getDisplay();
                break;
            default:
                System.out.println("There is no such kind of cell");
        }
    }

    boolean isEmpty() {
        return kind == Kind.EMPTY;
    }

    boolean isItem() {
        return kind == Kind.ITEM;
    }

    boolean isWall() {
        return kind == Kind.WALL;
    }

    boolean isCharacter() {
        return kind == Kind.CHARACTER;
    }

    public Cell.Item getItem() {
        return item;
    }

    public Character getCharacter() {
        if (kind == Kind.CHARACTER) return character;
        else {
            System.out.println("This cell is not a character");
            return null;
        }
    }

    String getDisplay() {
        return display;
    }

    Location getLocation() {
        return location;
    }

    public enum Item {
        NOTHING, HELMET, STONE, WARM
    }

    public enum Kind {
        EMPTY, WALL, ITEM, CHARACTER
    }
}
