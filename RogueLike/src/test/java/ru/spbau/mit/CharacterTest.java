package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterTest {
    @Test
    public void getIsAlive() throws Exception {
        Character mole = new Mole("mole");
        assertTrue(mole.getIsAlive());
    }

    @Test
    public void getName() throws Exception {
        Character mole = new Mole("mole");
        assertEquals("mole", mole.getName());
    }

    @Test
    public void isBot() throws Exception {
        Character mole = new Mole("mole");
        assertFalse(mole.isBot());
        Cell moleCell = new Cell(new Location(0, 0), Cell.Kind.CHARACTER, mole);
        Character dog = new Dog("dog", true, "D", moleCell, new Location(2, 4));
        assertTrue(dog.isBot());
    }

    @Test
    public void getDisplay() throws Exception {
        Character mole = new Mole("mole");
        assertEquals("@", mole.getDisplay());
    }

    @Test
    public void attemptMove() throws Exception {
        Character mole = new Mole("mole");
        Cell moleCell = new Cell(new Location(2, 3), Cell.Kind.CHARACTER, mole);
        Location newLocation = mole.attemptMove(moleCell.getLocation(), Character.Movement.LEFT);
        assertTrue(newLocation.x == 1 && newLocation.y == 3);
    }
}