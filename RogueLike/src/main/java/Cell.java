/**
 * Object of Cell class holds information about what is in this map cell:
 * is it wall, character, item or just empty cell
 */
class Cell implements Comparable<Cell> {

    Kind kind;
    Item item;
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
                this.item = (Cell.Item)object;
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

    Character getCharacter() {
        if (kind == Kind.CHARACTER) return character;
        else {
            System.out.println("This cell is not a character");
            return null;
        }
    }

    String getDisplay() {
        return display;
    }

    int getX() {
        return location.x;
    }

    void setX(int x) {
        location.x = x;
    }

    int getY() {
        return location.y;
    }

    void setY(int y) {
        location.y = y;
    }

    Location getLocation() {
        return location;
    }

    @Override
    public int compareTo(Cell cell) {
        if (cell == null) {
            return -1;
        }
        if (this == cell) {
            return 0;
        }

        if (this.location.x != cell.getX()) {
            return this.getX() - cell.getX();
        } else {
            return this.getY() - cell.getY();
        }
    }

    public Location attemptMove() {
        Location xy = new Location(this.location);
        if (this.character == null) {
            System.out.println("This is not character");
        } else if (this.character.getIsAlive()) {
            Character.Movement m = this.character.movement();
            switch (m) {
                case UP:
                    xy.y = this.getY() - 1;
                    break;
                case DOWN:
                    xy.y = this.getY() + 1;
                    break;
                case LEFT:
                    xy.x = this.getX() - 1;
                    break;
                case RIGHT:
                    xy.x = this.getX() + 1;
                    break;
                case HOLD:
                    break;
            }
        }
        return xy;
    }

    public Location attemptManualMove(Character.Movement m) {
        Location xy = new Location(this.location);
        if (this.character == null) {
            System.out.println("This is not character");
        } else if (this.character.getIsAlive()) {
            switch (m) {
                case UP:
                    xy.y = this.getY() - 1;
                    break;
                case DOWN:
                    xy.y = this.getY() + 1;
                    break;
                case LEFT:
                    xy.x = this.getX() - 1;
                    break;
                case RIGHT:
                    xy.x = this.getX() + 1;
                    break;
                case HOLD:
                    break;
            }
        }
        return xy;
    }

    public enum Item {
        HELMET, STONE, WARM
    }

    public enum Kind {
        EMPTY, WALL, ITEM, CHARACTER
    }
}
