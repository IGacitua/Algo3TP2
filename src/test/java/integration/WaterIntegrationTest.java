package integration;

import game.map.GameMap;
import game.map.cell.Cell;
import game.map.cell.Water;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.East;
import game.units.Team;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.movement.Flying;
import game.units.movement.Grounded;
import game.units.movement.Hover;
import game.units.weapons.mekWeapons.Punch;

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

        Cell[][] cells = new Cell[5][5];
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                cells[x][y] = new Cell(new Position(x, y));
            }
        }
        cells[1][0] = new Water(waterPosition);

        Mockito.when(importer.importMap(Mockito.any(GameMap.class), Mockito.anyString()))
                .thenReturn(cells);

        gameMap = new GameMap(importer, "");
    }

    @Test
    public void testGroundedUnitPushedIntoWaterDies() throws Exception {
        // Arrange
        Unit unit = new Unit(Team.Player, null, 3, new Grounded(1));
        gameMap.tryPlaceUnit(unit, landPosition);

        // Act
        gameMap.pushUnit(landPosition, new East());

        // Assert
        assertFalse(unit.isAlive());
    }

    @Test
    public void testFlyingUnitPushedIntoWaterSurvives() {
        // Arrange
        Unit unit = new Unit(Team.Player, null, 3, new Flying(1));
        gameMap.tryPlaceUnit(unit, landPosition);

        // Act
        gameMap.pushUnit(landPosition, new East());

        // Assert
        assertTrue(unit.isAlive());
        assertEquals(3, unit.getHealth());
        assertTrue(gameMap.hasUnitAt(waterPosition), "Flying unit should end up over the water");
        assertFalse(gameMap.hasUnitAt(landPosition), "Flying unit should have left its original position");
    }

    @Test
    public void testGroundedUnitCannotBePlacedOnWater() throws Exception {
        // Arrange
        Unit unit = new Unit(Team.Player, null, 3, new Grounded(1));
        gameMap.tryPlaceUnit(unit, landPosition);

        // Act
        boolean placed = gameMap.tryPlaceUnit(unit, waterPosition);

        // Assert
        assertFalse(placed);
        assertTrue(unit.isAlive());
        assertTrue(gameMap.hasUnitAt(landPosition), "Unit should stay on its original position");
        assertFalse(gameMap.hasUnitAt(waterPosition), "Water cell should remain unoccupied");
    }

    @Test
    public void testMechInWaterCannotAttack() throws PositionOutOfBoundsException {
        // Arrange
        Unit mech = new Unit(Team.Player, new Punch(gameMap), 3, new Hover(1));
        Position targetPos = new Position(2, 0);
        Unit enemy = new Unit(Team.Enemy, null, 2, new Grounded(1));

        gameMap.tryPlaceUnit(mech, waterPosition);
        gameMap.tryPlaceUnit(enemy, targetPos);

        UnitAction attackAction = mech.getAttackAction(waterPosition, targetPos);

        // Act
        boolean executed = attackAction.tryExecute(gameMap);

        // Assert
        assertFalse(executed, "Mech standing on water should not be able to attack");
        assertFalse(enemy.isDamaged(), "Target should not receive damage when attacker is in water");
    }
}