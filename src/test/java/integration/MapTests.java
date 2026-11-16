package integration;

import game.map.GameMap;
import game.map.cell.*;
import game.map.creation.CellFactory;
import game.map.creation.MapImporter;
import game.position.Position2D;
import game.position.PositionOutOfBoundsException;
import game.utils.FileHandler;
import org.junit.Assert;
import org.junit.Test;

public class MapTests {

    @Test
    public void testMapZeroLoadsCorrectly() {
        testMapLoadsCorrectly("src/test/resources/testMap0.txt", buildExpectedMapZero());
    }

    @Test
    public void testMapOneLoadsCorrectly() {
        testMapLoadsCorrectly("src/test/resources/testMap1.txt", buildExpectedMapOne());
    }

    private Cell[][] buildExpectedMapZero() {
        Cell[][] expected = new Cell[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (x == 0 || y == 0 || x == 7 || y == 7) {
                    expected[y][x] = new Mountain(null);
                } else if (y == 1 || y == 2) {
                    expected[y][x] = new Water(null);
                } else if (y == 3 || y == 4) {
                    expected[y][x] = new Cell(null);
                } else if (x == 1 || x == 2) {
                    expected[y][x] = new Acid(null);
                } else if (x == 3 || x == 4) {
                    expected[y][x] = new Cell(null);
                } else {
                    expected[y][x] = new Building(null);
                }
            }
        }
        return expected;
    }

    private Cell[][] buildExpectedMapOne() {
        return new Cell[][]{
                {new Mountain(null), new Cell(null), new Cell(null), new Mountain(null)},
                {new Mountain(null), new Cell(null), new Cell(null), new Mountain(null)},
                {new Mountain(null), new Cell(null), new Cell(null), new Mountain(null)},
                {new Mountain(null), new Cell(null), new Cell(null), new Mountain(null)}
        };
    }

    private void testMapLoadsCorrectly(String mapPath, Cell[][] expected) {
        FileHandler fileHandler = FileHandler.getInstance();
        CellFactory cellFactory = new CellFactory();
        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory);
        GameMap map = GameMap.getInstance();
        try {
            map.initialize(mapImporter, mapPath);
        } catch (Exception e) {
            Assert.fail("Map failed to load: " + e.getMessage());
        }

        for (int y = 0; y < expected.length; y++) {
            for (int x = 0; x < expected[y].length; x++) {
                try {
                    Cell actual = map.getCellAt(new Position2D(x, y));
                    Assert.assertEquals("Unexpected cell type at " + x + ", " + y + ".", expected[y][x].cellTypeId(), actual.cellTypeId());
                } catch (PositionOutOfBoundsException e) {
                    Assert.fail("Position out of bounds at " + x + ", " + y + ": " + e.getMessage());
                }
            }
        }
    }
}