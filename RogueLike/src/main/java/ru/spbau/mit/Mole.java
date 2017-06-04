package ru.spbau.mit;

import java.util.HashMap;
import java.util.Map;

/**
 * There is only one instance of this class: main character
 */
class Mole extends Character {
    public static final String moleSymbol = "@";
    Map<Cell.Item, Integer> backpack = new HashMap<>();
    Cell.Item currItem = Cell.Item.NOTHING;

    Mole(String name) {
        super(name, false, moleSymbol);
        power = 50;
        safety = 50;
        for (Cell.Item item : Cell.Item.values()) {
            backpack.put(item, 0);
        }
    }

    /**
     * gives increase to one of mole characteristics
     *
     * @param item instance of Cell.Item enum
     */
    public void putItemToBackpack(Cell.Item item) {
        backpack.put(item, backpack.get(item) + 1);
    }

    /**
     * user can choose item that he want to put on mole and
     * increase one of it's characteristics, but it cause decrease of
     * some another characteristic
     *
     * @param item to put on
     */
    public void takeItemFromBackpack(Cell.Item item) {
        if (item == Cell.Item.NOTHING) {
            takeOffItem();
            currItem = item;
        }
        if (backpack.get(item) == 0) return;
        switch (item) {
            case HELMET:
                safety += 10;
                power -= 3;
                break;
            case STONE:
                power += 10;
                safety -= 3;
                break;
            case WARM:
                health += 10;
                break;
        }
        if (item != Cell.Item.WARM) {
            if (currItem != null) {
                takeOffItem();
            }
            currItem = item;
        }
        if (item != Cell.Item.NOTHING)
            backpack.put(item, backpack.get(item) - 1);
    }

    /**
     * To take off the item
     */
    public void takeOffItem() {
        if (currItem == null) return;
        switch (currItem) {
            case HELMET:
                safety -= 10;
                power += 3;
                break;
            case STONE:
                power -= 10;
                safety += 3;
                break;
        }
    }

    @Override
    protected int takeDamage(Character attacker) {
        int damageTaken = 10 * attacker.power / safety;
        this.health -= damageTaken;

        if (this.health <= 0) {
            System.out.printf("(%s) took (%d) damage and died.\n", getName(), damageTaken);
            this.isAlive = false;
        } else {
            System.out.printf("(%s) took (%d) damage.\n", getName(), damageTaken);
        }

        return damageTaken;
    }
}

