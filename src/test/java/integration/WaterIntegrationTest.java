package integration;

import game.map.GameMap;
import game.map.cell.Cell;
import game.map.cell.Water;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.direction.East;
import game.units.Unit;
import game.units.movement.Flying;
import game.units.movement.Grounded;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class WaterIntegrationTest {

    private final Position landPosition = new Position(0, 0);
    private final Position waterPosition = new Position(1, 0);

    private GameMap gameMap;

    @BeforeEach
    public void setUp() {
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                cells[x][y] = new Cell(new Position(x, y));
            }
        }
        cells[1][0] = new Water(waterPosition);

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        gameMap = new GameMap(importer, "");
    }

    @Test
    public void testGroundedUnitPushedIntoWaterDies() throws Exception {
        // Arrange
        Unit unit = new Unit(null, 3, new Grounded(1));
        gameMap.tryPlaceUnit(unit, landPosition);

        // Act
        gameMap.pushUnit(landPosition, new East());

        // Assert
        assertFalse(unit.isAlive());
    }

    @Test
    public void testFlyingUnitPushedIntoWaterSurvives() {
        // Arrange
        Unit unit = new Unit(null, 3, new Flying(1));
        gameMap.tryPlaceUnit(unit, landPosition);

        // Act
        gameMap.pushUnit(landPosition, new East());

        // Assert
        assertTrue(unit.isAlive());
        assertEquals(3, unit.getCurrentHealth());
        assertTrue(gameMap.hasUnitAt(waterPosition), "Flying unit should end up over the water");
        assertFalse(gameMap.hasUnitAt(landPosition), "Flying unit should have left its original position");
    }

    @Test
    public void testGroundedUnitCannotBePlacedOnWater() throws Exception {
        // Arrange
        Unit unit = new Unit(null, 3, new Grounded(1));
        gameMap.tryPlaceUnit(unit, landPosition);

        // Act
        boolean placed = gameMap.tryPlaceUnit(unit, waterPosition);

        // Assert
        assertFalse(placed);
        assertTrue(unit.isAlive());
        assertTrue(gameMap.hasUnitAt(landPosition), "Unit should stay on its original position");
        assertFalse(gameMap.hasUnitAt(waterPosition), "Water cell should remain unoccupied");
    }
}