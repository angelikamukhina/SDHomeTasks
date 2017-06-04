package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {
    @Test
    public void isEmpty() throws Exception {
        Cell cell = new Cell(new Location(0, 0), Cell.Kind.EMPTY);
        assertTrue(cell.isEmpty());
        Cell cellCharacter = new Cell(new Location(0, 0), Cell.Kind.CHARACTER, new Mole("mole"));
        assertFalse(cellCharacter.isEmpty());
    }

    @Test
    public void isItem() throws Exception {
        Cell cell = new Cell(new Location(0, 0), Cell.Kind.ITEM);
        assertTrue(cell.isItem());
    }

    @Test
    public void isWall() throws Exception {
        Cell cell = new Cell(new Location(0, 0), Cell.Kind.WALL);
        assertTrue(cell.isWall());
    }

    @Test
    public void isCharacter() throws Exception {
        Cell cell = new Cell(new Location(0, 0), Cell.Kind.CHARACTER);
        assertTrue(cell.isCharacter());
    }

    @Test
    public void getItem() throws Exception {
        Cell cell = new Cell(new Location(0, 0), Cell.Kind.ITEM, Cell.Item.HELMET);
        assertEquals(Cell.Item.HELMET, cell.getItem());
    }

    @Test
    public void getCharacter() throws Exception {
        Cell cell = new Cell(new Location(0, 0), Cell.Kind.CHARACTER, new Mole("mole"));
        assertTrue(cell.getCharacter() != null);
    }

    @Test
    public void getDisplay() throws Exception {
        Cell cell = new Cell(new Location(0, 0), Cell.Kind.WALL);
        assertEquals("#", cell.getDisplay());
    }

    @Test
    public void getLocation() throws Exception {
        Cell cell = new Cell(new Location(0, 1), Cell.Kind.WALL);
        assertTrue(cell.getLocation().x == 0 && cell.getLocation().y == 1);
    }
}