package integration;

import game.map.GameMap;
import game.map.Position2D;
import game.map.cell.Cell;
import game.map.cell.Water;
import game.map.creation.MapImporter;
import game.map.direction.East;
import game.units.Unit;
import game.units.movement.FlyingMovement;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class WaterIntegrationTest {

    @Test
    public void testGroundedUnitPushedIntoWaterDies() throws Exception {
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[2][1];
        cells[0][0] = new Cell(new Position2D(0, 0));
        cells[1][0] = new Water(new Position2D(1, 0));

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        GameMap.getInstance().initialize(importer, "");

        Unit unit = new Unit(
                new Position2D(0, 0),
                null,
                3
        );

        GameMap.getInstance().pushUnit(
                new Position2D(0, 0),
                new East()
        );

        assertFalse(unit.isAlive());
    }

    @Test
    public void testFlyingUnitPushedIntoWaterSurvives() throws Exception {
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[2][1];
        cells[0][0] = new Cell(new Position2D(0, 0));
        cells[1][0] = new Water(new Position2D(1, 0));

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        GameMap.getInstance().initialize(importer, "");

        Unit unit = new Unit(
                new Position2D(0, 0),
                null,
                3,
                new FlyingMovement()
        );

        GameMap.getInstance().pushUnit(
                new Position2D(0, 0),
                new East()
        );

        assertTrue(unit.isAlive());
        assertEquals(3, unit.getCurrentHealth());
        assertEquals(1, unit.getPosition().getX());
        assertEquals(0, unit.getPosition().getY());
    }

    @Test
    public void testGroundedUnitCannotBePlacedOnWater() throws Exception {
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[2][1];
        cells[0][0] = new Cell(new Position2D(0, 0));
        cells[1][0] = new Water(new Position2D(1, 0));

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        GameMap.getInstance().initialize(importer, "");

        Unit unit = new Unit(
                new Position2D(0, 0),
                null,
                3
        );

        boolean placed = GameMap.getInstance().tryPlaceUnit(
                unit,
                new Position2D(1, 0)
        );

        assertFalse(placed);
        assertTrue(unit.isAlive());
        assertEquals(0, unit.getPosition().getX());
        assertEquals(0, unit.getPosition().getY());
    }
}