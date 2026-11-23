package integration;

import game.map.GameMap;
import game.map.cell.Acid;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.West;
import game.units.Mech;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.behavior.PlayerBehavior;
import game.units.behavior.TurnUndoTracker;
import game.units.movement.Flying;
import game.units.weapons.Weapon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcidIntegrationTest {

    private GameMap gameMap;

    @BeforeEach
    public void setUp() throws InvalidMapFileException {
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                cells[x][y] = new Cell(new Position(x, y));
            }
        }
        cells[0][0] = new Acid(new Position(0, 0));

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        gameMap = new GameMap(importer, "");
    }

    @Test
    public void testGroundedUnitOnAcidReceivesDoubleDamage() throws PositionOutOfBoundsException {
        // Arrange
        Unit unit = new Unit(null, 10);
        gameMap.placeUnitAt(unit, new Position(0, 0));

        // Act
        unit.damage(2);

        // Assert
        assertEquals(6, unit.getCurrentHealth());
    }

    @Test
    public void testFlyingUnitOnAcidReceivesNormalDamage() {
        // Arrange
        Unit unit = new Unit(null, 10, new Flying());

        // Act
        unit.damage(2);

        // Assert
        assertEquals(8, unit.getCurrentHealth());
    }

    @Test
    public void testGroundedUnitPushedIntoAcidReceivesDoubleDamage() throws Exception {
        // Arrange
        Position unitPosition = new Position(1, 0);
        Unit unit = new Unit(null, 10);

        // Act
        gameMap.placeUnitAt(unit, unitPosition);
        gameMap.pushUnitTowards(unitPosition, new West());
        unit.damage(2);

        // Assert
        assertEquals(6, unit.getCurrentHealth());
    }

    @Test
    public void testFlyingUnitPushedIntoAcidReceivesNormalDamage() throws Exception {
        // Arrange
        Position unitPosition = new Position(1, 0);
        Unit unit = new Unit(null, 10, new Flying());

        // Act
        gameMap.placeUnitAt(unit, unitPosition);
        gameMap.pushUnitTowards(unitPosition, new West());
        unit.damage(2);

        // Assert
        assertEquals(8, unit.getCurrentHealth());
    }

    @Test 
    public void undoingMoveOntoAcidRemovesContamination() throws PositionOutOfBoundsException {
        // Arrange
        Mech mech = new Mech(Mockito.mock(Weapon.class));
        Position origin = new Position(1, 0);
        Position moveDestination = new Position(0, 0); 
        gameMap.placeUnitAt(mech, origin);

        PlayerBehavior player = new PlayerBehavior(mech, new TurnUndoTracker());
        UnitAction moveAction = mech.getMove(origin, moveDestination);

        // Act
        player.executeAction(moveAction, gameMap);
        assertTrue(mech.isContaminated());

        player.undoMovement(gameMap);        

        // Assert
        assertFalse(mech.isContaminated(), "Expected the mech to no longer be contaminated after undoing the move");
        assertTrue(gameMap.hasMechAt(origin), "Expected the mech to be back at its original position after undoing the move");
    }
}
