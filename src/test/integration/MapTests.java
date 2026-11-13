package integration;

import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.map.cell.*;
import game.map.creation.CellFactory;
import game.map.creation.MapImporter;
import game.utils.FileHandler;
import org.junit.Assert;
import org.junit.Test;

public class MapTests {

    @SuppressWarnings("ConstantValue")
    @Test
    public void testMapZeroLoadsCorrectly() {
        FileHandler fileHandler = FileHandler.getInstance();
        CellFactory cellFactory = new CellFactory();
        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory);
        GameMap map = GameMap.getInstance();
        try {
            map.initialize(mapImporter, "src/test/resources/testMap0.txt");
        } catch (Exception e) {
            Assert.fail("Map failed to load: " + e.getMessage());
        }

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                try {
                    System.out.print(map.getCell(new Position2D(x, y)).shortName() + " ");
                } catch (PositionOutOfBoundsException e) {
                    Assert.fail("Position out of bounds: " + e.getMessage());
                }
            }
            System.out.println();
        }

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                try {
                    Cell c = map.getCell(new Position2D(x, y));
                    if (x == 0 || y == 0 || x == 7 || y == 7) {
                        Assert.assertEquals("Expected mountain at " + x + ", " + y + ".", Mountain.class.getSimpleName(), c.getClass().getSimpleName());
                    } else if (y == 1 || y == 2) {
                        Assert.assertEquals("Expected water at " + x + ", " + y + ".", Water.class.getSimpleName(), c.getClass().getSimpleName());
                    } else if (y == 3 || y == 4) {
                        Assert.assertEquals("Expected generic cell at " + x + ", " + y + ".", Cell.class.getSimpleName(), c.getClass().getSimpleName());
                    } else if (y == 5 || y == 6) {
                        if (x == 1 || x == 2) {
                            Assert.assertEquals("Expected acid at " + x + ", " + y + ".", Acid.class.getSimpleName(), c.getClass().getSimpleName());
                        } else if (x == 3 || x == 4) {
                            Assert.assertEquals("Expected generic cell at " + x + ", " + y + ".", Cell.class.getSimpleName(), c.getClass().getSimpleName());
                        } else if (x == 5 || x == 6) {
                            Assert.assertEquals("Expected building at " + x + ", " + y + ".", Building.class.getSimpleName(), c.getClass().getSimpleName());
                        }
                    }
                } catch (PositionOutOfBoundsException e) {
                    Assert.fail("Position out of bounds: " + e.getMessage());
                }
            }
        }
    }
}
