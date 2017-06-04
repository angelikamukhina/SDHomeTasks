package ru.spbau.mit;

import net.slashie.libjcsi.CSIColor;
import net.slashie.libjcsi.CharKey;
import net.slashie.libjcsi.ConsoleSystemInterface;
import net.slashie.libjcsi.wswing.WSwingConsoleInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class UI {
    private final int leftMapSide;
    private final int topMapSide;
    private final int SCREEN_WIDTH = 80;
    private final int SCREEN_HEIGHT = 25;
    private ConsoleSystemInterface csi;

    UI(int mapHeight) {
        this.leftMapSide = (int) (0.25 * SCREEN_WIDTH);
        this.topMapSide = (SCREEN_HEIGHT - mapHeight) / 2;

        Properties text = new Properties();
        text.setProperty("fontSize", "11");
        text.setProperty("font", "Ariel");
        try {
            csi = new WSwingConsoleInterface("Wormhole", text);
        } catch (ExceptionInInitializerError e) {
            System.out.println("*** Error: Swing Console Box cannot be initialized. Exiting...");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public Character.Movement getUserActions(Mole mole, List<Cell> cells) {
        CharKey key = csi.inkey();
        if (key.code == CharKey.SPACE) {
            itemsManager(mole);
        } else {
            return getMovement(key);
        }
        displayMoleState(mole);
        displayMap(cells);
        return getMovement(key);
    }

    private void itemsManager(Mole mole) {
        Cell.Item itemToPutOn = null;
        CharKey key = null;
        do {
            String line;
            line = "(press Enter to choose)";
            csi.print(SCREEN_WIDTH / 2, 0, line, CSIColor.PINK);
            for (Cell.Item item : Cell.Item.values()) {
                itemToPutOn = item;
                if (item == Cell.Item.NOTHING) {
                    line = "Do you want to take off item?   ";
                    csi.print(SCREEN_WIDTH / 2, 1, line, CSIColor.PINK);
                    csi.print(SCREEN_WIDTH / 2, 2, "           ", CSIColor.PINK_ORANGE);
                } else {
                    line = "What do you want to get out?:    ";
                    csi.print(SCREEN_WIDTH / 2, 1, line, CSIColor.PINK);
                    csi.print(SCREEN_WIDTH / 2, 2, itemToPutOn.toString() + "  ", CSIColor.PINK_ORANGE);
                }
                csi.refresh();
                key = csi.inkey();
                if (key.isLeftArrow()) {
                    continue;
                } else if (key.code == CharKey.ENTER) {
                    break;
                }
            }
        } while (key.code != CharKey.ENTER);
        csi.cls();
        mole.takeOffItem();
        mole.takeItemFromBackpack(itemToPutOn);
    }

    /**
     * the method for getting users direction from keyboard
     *
     * @return instance of enum Movement
     */
    public Character.Movement getMovement(CharKey dir) {
        Character.Movement cm;
        if (dir.isUpArrow()) {
            cm = Character.Movement.UP;
        } else if (dir.isDownArrow()) {
            cm = Character.Movement.DOWN;
        } else if (dir.isLeftArrow()) {
            cm = Character.Movement.LEFT;
        } else if (dir.isRightArrow()) {
            cm = Character.Movement.RIGHT;
        } else if (dir.code == CharKey.H) {
            cm = Character.Movement.HOLD;
        } else {
            cm = getMovement(csi.inkey());
        }
        return cm;
    }

    /**
     * highlight dead player
     *
     * @param loc           location of dead player
     * @param deadCharacter dead character
     */
    public void displayDeadCharacter(Location loc, Character deadCharacter) {
        csi.print(loc.x + leftMapSide, loc.y + topMapSide, deadCharacter.getDisplay(), CSIColor.RED);
        csi.refresh();
    }

    /**
     * the method for updating map
     *
     * @param cells cells of map
     */
    public void displayMap(List<Cell> cells) {
        for (Cell cellElement : cells) {
            if (!cellElement.isEmpty()) {
                if (cellElement.isWall()) {
                    displayCell(cellElement, CSIColor.BROWN);
                    continue;
                }
                if (cellElement.isItem()) {
                    displayCell(cellElement, CSIColor.ALICE_BLUE);
                    continue;
                }
                if (cellElement.getCharacter().getIsAlive()) {
                    displayCell(cellElement, CSIColor.PINK);
                } else {
                    displayCell(cellElement, CSIColor.GRAY);
                }
            } else {
                displayCell(cellElement, CSIColor.GRAY);
            }
            csi.refresh();
        }
        displayRules();
    }

    private void displayCell(Cell cell, CSIColor color) {
        Location location = cell.getLocation();
        csi.print(location.x + leftMapSide, location.y + topMapSide, cell.getDisplay(), color);
    }

    /**
     * gets name of mole from user
     *
     * @return name
     */
    public String getMoleName() {
        String qLine = "What is your Mole name ? ";
        csi.print(SCREEN_WIDTH / 2 - qLine.length() / 2, SCREEN_HEIGHT / 2, qLine, CSIColor.LAVENDER_PINK);
        csi.refresh();
        String name = askQuestionStr(csi);
        csi.cls();
        return name;
    }

    /**
     * shows state of mole: name, health, safety, power
     *
     * @param mole Cell object of mole
     */
    public void displayMoleState(Mole mole) {
        String[] state = {"Mole: ",
                "name: " + mole.getName(),
                "health: " + mole.health,
                "safety: " + mole.safety,
                "power: " + mole.power};
        int position = 0;
        for (String line : state) {
            csi.print(2, position, line, CSIColor.GRAY);
            position++;
        }
        csi.print(2, position, "item: " + mole.currItem, CSIColor.GRAY);

        position = 0;
        csi.print(2, SCREEN_HEIGHT / 2 + position++, "backpack: ");
        ArrayList<String> backpackState = new ArrayList<>();
        for (Cell.Item item : Cell.Item.values()) {
            if (item != Cell.Item.NOTHING)
                backpackState.add(item + " " + mole.backpack.get(item));
        }
        for (String line : backpackState) {
            csi.print(2, SCREEN_HEIGHT / 2 + position, line, CSIColor.GRAY);
            position++;
        }
        csi.refresh();
    }

    /**
     * shows string s at the center of screen
     *
     * @param s string to show
     */
    public void displayString(String s) {
        csi.print((SCREEN_WIDTH - s.length()) / 2, 0, s, CSIColor.DARK_RED);
    }

    /**
     * at the end of game shows winner
     *
     * @param name name of winner
     */
    public void displayWinner(String name) {
        String line1 = "Winner";
        String line2 = name;
        csi.print((SCREEN_WIDTH - line1.length()) / 2, 0, "Winner", CSIColor.DARK_RED);
        csi.print((SCREEN_WIDTH - line2.length()) / 2, 1, name, CSIColor.WHITE);
        csi.refresh();
    }

    private String askQuestionStr(ConsoleSystemInterface csi) {
        csi.locateCaret(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2 + 2);
        String answer = csi.input();
        csi.refresh();
        return answer;
    }

    public void displayRules() {
        int vertPosition = (int) (6.3 / 8. * SCREEN_HEIGHT) + 1;
        String line = "Press SPACE to take item from backpack.";
        csi.print(12, vertPosition, line, CSIColor.GRAY);
        line = "Press LeftArrow, UpArrow, DownArrow, RightArrow to move.";
        csi.print(12, ++vertPosition, line, CSIColor.GRAY);
        line = "* is a stone, it increase your power, but decrease safety;";
        csi.print(12, ++vertPosition, line, CSIColor.GRAY);
        line = "^ is a helmet, it increase your safety, but decrease power;";
        csi.print(12, ++vertPosition, line, CSIColor.GRAY);
        line = "w is a worm, eat it, and your health will increase.";
        csi.print(12, ++vertPosition, line, CSIColor.GRAY);
        csi.refresh();
    }
}
