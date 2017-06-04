package ru.spbau.mit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     *
     * @param args
     */
    public static void main(String[] args) {
        UI ui = new UI(MAP_HEIGHT);
        Logger LOG = LogManager.getLogger(Game.class);
        LOG.info("UI object was created");
        Map map = new Map(MAP_WIDTH, MAP_HEIGHT, NUMBER_OF_ROOMS);
        LOG.info("Map object was created");
        List<Cell> cells = initGame(map);
        LOG.info("Map cells were created and initialized");
        Cell moleCell = getMole(cells, map, ui);
        LOG.info("Mole object was created");
        ui.displayRules();
        Map.Room theFirstRoom = map.rooms.get(0);
        generateItems(AMOUNT_OF_ITEMS_IN_ROOM, cells, theFirstRoom);
        generateEnemies(AMOUNT_OF_ENEMIES_IN_ROOM, cells, theFirstRoom, moleCell);

        LOG.info("All initial game objects were created");

        Set<Map.Room> visitedRooms = new HashSet<>();
        List<Cell> characterCells = new ArrayList<>();
        ui.displayMoleState((Mole) (moleCell.getCharacter()));

        LOG.info("Game started");
        do {
            ui.displayMap(cells);
            getCharactersCells(cells, characterCells);
            turn(LOG, ui, cells, moleCell, moleCell);

            LOG.info("Mole has made turn");

            ui.displayMap(cells);

            for (Cell cellElement : characterCells) {
                turn(LOG, ui, cells, cellElement, moleCell);
                LOG.info(cellElement.getCharacter().getName() + " has made turn");
                ui.displayMap(cells);
            }
            ui.displayMoleState((Mole) moleCell.getCharacter());
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
     *
     * @param ui          object of class implementing ConsoleInterface
     * @param cells       map cells
     * @param currentCell cell that goes
     */
    private static void turn(Logger LOG, UI ui, List<Cell> cells, Cell currentCell, Cell moleCell) {
        Character currCharacter = currentCell.getCharacter();

        Location xy;
        int numberOfAttempts = 0;
        do {
            Character.Movement cm = null;
            if (!currentCell.getCharacter().isBot()) {
                cm = ui.getUserActions((Mole) moleCell.getCharacter(), cells);
            }
            xy = currCharacter.attemptMove(currentCell.getLocation(), cm);
            numberOfAttempts++;
        } while (isWall(xy, cells) && numberOfAttempts <= 20);
        if (numberOfAttempts == 20) return;

        int newLocationInList = Location.locationToList(xy.x, xy.y, MAP_WIDTH);
        Location prevLocation = currentCell.getLocation();
        int oldLocationInList = Location.locationToList(prevLocation.x, prevLocation.y, MAP_WIDTH);
        Cell cellOnNewLocation = cells.get(newLocationInList);

        if (cellOnNewLocation.isCharacter()) {
            meetCharacter(LOG, ui, currentCell, xy, cellOnNewLocation);
        } else {

            if (cellOnNewLocation.isItem() && !currCharacter.isBot()) {
                meetItem(LOG, (Mole) currCharacter, cellOnNewLocation);
            }

            meetEmptyCell(cells, currentCell, xy, newLocationInList, oldLocationInList);
        }
    }

    private static void meetEmptyCell(List<Cell> cells, Cell currentCell,
                                      Location xy, int newLocationInList, int oldLocationInList) {
        Location prevLocation = new Location(currentCell.getLocation());
        cells.set(oldLocationInList, new Cell(prevLocation, Cell.Kind.EMPTY));
        currentCell.getLocation().x = xy.x;
        currentCell.getLocation().y = xy.y;
        cells.set(newLocationInList, currentCell);
    }

    private static void meetItem(Logger LOG, Mole mole, Cell cellOnNewLocation) {
        Cell.Item item = cellOnNewLocation.getItem();
        mole.putItemToBackpack(item);
        switch (item) {
            case HELMET:
                LOG.info("Mole put helmet to backpack");
                break;
            case STONE:
                LOG.info("Mole put stone to backpack");
                break;
            case WARM:
                LOG.info("Mole put warm to backpack");
                break;
        }
    }

    private static void meetCharacter(Logger LOG, UI ui, Cell currentCell, Location xy, Cell cellOnNewLocation) {
        Character currCharacter = currentCell.getCharacter();
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
                LOG.info(currCharacter.getName() + " is dead");
            } else {
                ui.displayDeadCharacter(xy, characterOnNewLocation);
                LOG.info(characterOnNewLocation + " is dead");
            }
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
                occupiedLocations.add(new Location(cell.getLocation()));
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
            newEnemy = new Dog("Dog" + enemies, true, display, moleCell, currLocation);
        }
        cells.set(Location.locationToList(x, y, MAP_WIDTH),
                new Cell(currLocation, Cell.Kind.CHARACTER, newEnemy));
    }

    private static Cell getMole(List<Cell> cells, Map map, UI ui) {
        Map.Room theFirstRoom = map.rooms.get(0);
        Location moleLocation = new Location(theFirstRoom.x, theFirstRoom.y);
        Cell moleCell = new Cell(moleLocation, Cell.Kind.CHARACTER, new Mole(ui.getMoleName()));
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
