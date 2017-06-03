import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The main class of the game.
 */
public class Game {

    private static final int MAP_WIDTH = 60;
    private static final int MAP_HEIGHT = 20;
    private static final int NUMBER_OF_ROOMS = 5;
    private static final int AMOUNT_OF_ITEMS_IN_ROOM = 6;
    private static final int AMOUNT_OF_ENEMIES_IN_ROOM = 4;
    /**
     * In the main method UI and Map objects, list of map cells and
     * cell corresponding to main character  are created.
     * At the start of game items and enemies are generated randomly in room
     * where the main character is.
     * When the mole come to new room, new enemies and items are created.
     * @param args
     */
    public static void main(String[] args) {
        UI ui = new UI(MAP_HEIGHT);
        Map map = new Map(MAP_WIDTH, MAP_HEIGHT, NUMBER_OF_ROOMS);
        List<Cell> cells = initGame(map);
        Cell moleCell = getMole(cells, map, ui);
        ui.displayRules();

        Map.Room theFirstRoom = map.rooms.get(0);
        generateItems(AMOUNT_OF_ITEMS_IN_ROOM, cells, theFirstRoom);
        generateEnemies(AMOUNT_OF_ENEMIES_IN_ROOM, cells, theFirstRoom, moleCell);


        Set<Map.Room> visitedRooms = new HashSet<>();
        List<Cell> characterCells = new ArrayList<>();
        ui.displayMoleState((Mole)(moleCell.getCharacter()));

        do {
            ui.displayMap(cells);
            getCharactersCells(cells, characterCells);
            turn(ui, cells, moleCell, moleCell);
            ui.displayMap(cells);

            for (Cell cellElement : characterCells) {
                turn(ui, cells, cellElement, moleCell);
                ui.displayMap(cells);
            }
            ui.displayMoleState((Mole)moleCell.getCharacter());
            genItems(moleCell, map.rooms, visitedRooms, cells);
            genEnemiesIfNewRoom(moleCell, map.rooms, visitedRooms, cells);
        } while (characterCells.size() > 0 &&
                moleCell.getCharacter().getIsAlive());

        if (!moleCell.getCharacter().getIsAlive()) {
            String line = "You have died";
            ui.displayString(line);
        } else if (characterCells.size() == 0) {
            ui.displayWinner("You won!");
        }
    }

    private static List<Cell> initGame(Map map) {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < MAP_WIDTH; i++) {
            for (int j = 0; j < MAP_HEIGHT; j++) {
                cells.add(new Cell(new Location(i, j), Cell.Kind.EMPTY));
            }
        }
        for (int i = 0; i < map.getData().size(); i++) {
            Location location = new Location(i % map.getWidth(), i / map.getWidth());
            int value = map.getData().get(i);
            if (value == 2) {
                cells.set(i, new Cell(location, Cell.Kind.WALL));
            }
            if (value == 1 || value == 0) {
                cells.set(i, new Cell(location, Cell.Kind.EMPTY));
            }
        }
        return cells;
    }

    /**
     * Turn of the game. All players step by 1 cell up, down, left or right.
     * @param ui object of class implementing ConsoleInterface
     * @param cells map cells
     * @param currentCell cell that goes
     */
    private static void turn(UI ui, List<Cell> cells, Cell currentCell, Cell moleCell) {
        Location xy;
        int numberOfAttempts = 0;
        do {
            if (!currentCell.getCharacter().isBot()) {
                Character.Movement cm = ui.getUserActions((Mole)moleCell.getCharacter());
                xy = currentCell.attemptManualMove(cm);
            } else {
                xy = currentCell.attemptMove();
            }
            numberOfAttempts++;
        } while (isWall(xy, cells) && numberOfAttempts <= 20);
        if (numberOfAttempts == 20) return;

        int newLocationInList = Location.locationToList(xy.x, xy.y, MAP_WIDTH);
        int oldLocationInList = Location.locationToList(currentCell.getX(), currentCell.getY(), MAP_WIDTH);
        Character currCharacter = currentCell.getCharacter();
        Cell cellOnNewLocation = cells.get(newLocationInList);
        if (cellOnNewLocation.isCharacter()) {
            Character characterOnNewLocation = cellOnNewLocation.getCharacter();
            if (characterOnNewLocation == currCharacter) {
                return;
            }

            if (characterOnNewLocation.getIsAlive()) {
                while (currCharacter.getIsAlive() && characterOnNewLocation.getIsAlive()) {
                    currCharacter.attack(characterOnNewLocation);
                    characterOnNewLocation.attack(currCharacter);
                }
                if (!currCharacter.getIsAlive()) {
                    ui.displayDeadCharacter(currentCell.getLocation(), currCharacter);
                } else {
                    ui.displayDeadCharacter(xy, characterOnNewLocation);
                }
            }
        } else {
            if (cellOnNewLocation.isItem() && !currCharacter.isBot()) {
                Cell.Item item = cellOnNewLocation.item;
                ((Mole) currCharacter).putItemToBackpack(item);
                switch (item) {
                    case HELMET:
                        System.out.println("Mole put helmet to backpack");
                        break;
                    case STONE:
                        System.out.println("Mole put stone to backpack");
                        break;
                    case WARM:
                        System.out.println("Mole put warm to backpack");
                        break;
                }
            }

            cells.set(oldLocationInList, new Cell(new Location(currentCell.getLocation()), Cell.Kind.EMPTY));
            currentCell.setX(xy.x);
            currentCell.setY(xy.y);
            cells.set(newLocationInList, currentCell);
        }
    }

    private static Map.Room genEnemiesIfNewRoom(Cell moleCell, List<Map.Room> rooms,
                                                Set<Map.Room> visitedRooms, List<Cell> cells) {
        for (Map.Room room : rooms) {
            if (isInRoom(moleCell.getLocation(), room)) {
                if (visitedRooms.add(room)) generateEnemies(5, cells, room, moleCell);
                return room;
            }
        }
        return null;
    }

    private static Map.Room genItems(Cell moleCell, List<Map.Room> rooms,
                                     Set<Map.Room> visitedRooms, List<Cell> cells) {
        for (Map.Room room : rooms) {
            if (isInRoom(moleCell.getLocation(), room)) {
                if (!visitedRooms.contains(room)) generateItems(5, cells, room);
                return room;
            }
        }
        return null;
    }

    private static void generateItems(int amountOfItems, List<Cell> cells, Map.Room room) {
        Set<Location> occupiedLocations = new HashSet<>();
        for (Cell cell : cells) {
            if (!cell.isEmpty()) {
                occupiedLocations.add(new Location(cell.getX(), cell.getY()));
            }
        }
        for (int enemies = 0; enemies < amountOfItems / 3; enemies++) {
            getNewItem(cells, occupiedLocations, room, Cell.Item.HELMET);
            getNewItem(cells, occupiedLocations, room, Cell.Item.STONE);
            getNewItem(cells, occupiedLocations, room, Cell.Item.WARM);
        }
    }

    private static void getNewItem(List<Cell> cells, Set<Location> occupiedLocations, Map.Room room, Cell.Item item) {
        Location currLocation;
        int x;
        int y;
        do {
            x = (int) (room.x + Math.random() * room.w);
            y = (int) (room.y + Math.random() * room.h);
            currLocation = new Location(x, y);
        } while (!isInRoom(currLocation, room) || occupiedLocations.contains(currLocation));
        occupiedLocations.add(currLocation);
        cells.set(Location.locationToList(x, y, MAP_WIDTH),
                new Cell(currLocation, Cell.Kind.ITEM, item));
    }


    private static void getCharactersCells(List<Cell> cells, List<Cell> characterCells) {
        characterCells.clear();
        for (Cell cellElement : cells) {
            if (cellElement.isCharacter() && cellElement.getCharacter().isBot()) {
                if (cellElement.getCharacter().getIsAlive()) {
                    characterCells.add(cellElement);
                }
            }
        }
    }

    private static void generateEnemies(int amountOfEnemies, List<Cell> cells, Map.Room room, Cell moleCell) {
        Set<Location> occupiedLocations = new HashSet<>();
        for (Cell cell : cells) {
            if (!cell.isEmpty()) {
                occupiedLocations.add(cell.getLocation());
            }
        }
        for (int enemies = 0; enemies < amountOfEnemies / 2; enemies++) {
            getNewEnemy(cells, occupiedLocations, enemies, "D", room, moleCell);
            getNewEnemy(cells, occupiedLocations, enemies, "S", room, moleCell);
        }
    }

    private static void getNewEnemy(List<Cell> cells, Set<Location> occupiedLocations,
                                    int enemies, String display, Map.Room room, Cell moleCell) {
        Location currLocation;
        int x;
        int y;
        do {
            x = (int) (room.x + Math.random() * room.w);
            y = (int) (room.y + Math.random() * room.h);
            currLocation = new Location(x, y);
        } while (!isInRoom(currLocation, room) || occupiedLocations.contains(currLocation));
        occupiedLocations.add(currLocation);
        Character newEnemy;
        if (display.equals("S")) {
            newEnemy = new Shovel("Shovel" + enemies, 100, true, display);
        } else {
            newEnemy = new Dog("Dog" + enemies, 100, true, display, moleCell, currLocation);
        }
        cells.set(Location.locationToList(x, y, MAP_WIDTH),
                new Cell(currLocation, Cell.Kind.CHARACTER, newEnemy));
    }

    private static Cell getMole(List<Cell> cells, Map map, UI ui) {
        Map.Room theFirstRoom = map.rooms.get(0);
        Location moleLocation = new Location(theFirstRoom.x, theFirstRoom.y);
        Cell moleCell = new Cell(moleLocation, Cell.Kind.CHARACTER, new Mole(ui.getMoleName(), 100, false));
        cells.set(Location.locationToList(theFirstRoom.x, theFirstRoom.y, MAP_WIDTH), moleCell);
        return moleCell;
    }

    private static boolean isWall(Location location, List<Cell> cells) {
        return cells.get(location.locationToList(MAP_WIDTH)).isWall();
    }

    private static boolean isInRoom(Location location, Map.Room room) {
        return location.x >= room.x && location.y >= room.y &&
                location.x < room.x + room.w && location.y < room.y + room.h;
    }
}
