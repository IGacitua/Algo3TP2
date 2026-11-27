package integration;

import game.map.GameMap;
import game.map.cell.Acid;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.West;
import game.units.Team;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.behavior.player.PlayerBehavior;
import game.units.behavior.player.TurnUndoTracker;
import game.units.movement.Flying;
import game.units.movement.Grounded;
import game.units.weapons.Weapon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

        Mockito.when(importer.importMap(Mockito.any(GameMap.class), Mockito.anyString()))
                .thenReturn(cells);

        gameMap = new GameMap(importer, "");
    }

    @Test
    public void testGroundedUnitOnAcidReceivesDoubleDamage() throws PositionOutOfBoundsException {
        // Arrange
        Unit unit = new Unit(Team.Player, null, 10, new Grounded(1));
        gameMap.tryPlaceUnit(unit, new Position(0, 0));

        // Act
        unit.dealDamage(2);

        // Assert
        Assertions.assertEquals(6, unit.getHealth());
    }

    @Test
    public void testFlyingUnitOnAcidReceivesNormalDamage() {
        // Arrange
        Unit unit = new Unit(Team.Player, null, 10, new Flying(1));

        // Act
        unit.dealDamage(2);

        // Assert
        Assertions.assertEquals(8, unit.getHealth());
    }

    @Test
    public void testGroundedUnitPushedIntoAcidReceivesDoubleDamage() throws Exception {
        // Arrange
        Position unitPosition = new Position(1, 0);
        Unit unit = new Unit(Team.Player, null, 10, new Grounded(1));

        // Act
        gameMap.tryPlaceUnit(unit, unitPosition);
        gameMap.pushUnit(unitPosition, new West());
        unit.dealDamage(2);

        // Assert
        Assertions.assertEquals(6, unit.getHealth());
    }

    @Test
    public void testFlyingUnitPushedIntoAcidReceivesNormalDamage() throws Exception {
        // Arrange
        Position unitPosition = new Position(1, 0);
        Unit unit = new Unit(Team.Player, null, 10, new Flying(1));

        // Act
        gameMap.tryPlaceUnit(unit, unitPosition);
        gameMap.pushUnit(unitPosition, new West());
        unit.dealDamage(2);

        // Assert
        Assertions.assertEquals(8, unit.getHealth());
    }

    @Test
    public void undoingMoveOntoAcidRemovesContamination() throws PositionOutOfBoundsException {
        // Arrange
        Unit mech = new Unit(Team.Player, Mockito.mock(Weapon.class), 5, new Grounded(1));
        Position origin = new Position(1, 0);
        Position moveDestination = new Position(0, 0);
        gameMap.tryPlaceUnit(mech, origin);

        PlayerBehavior player = new PlayerBehavior(mech, new TurnUndoTracker());
        UnitAction moveAction = mech.getMovementAction(origin, moveDestination);

        // Act
        player.executeAction(moveAction, gameMap);
        Assertions.assertTrue(mech.isContaminated());

        player.undoMovement(gameMap);

        // Assert
        Assertions.assertFalse(mech.isContaminated(), "Expected the mech to no longer be contaminated after undoing the move");
        Assertions.assertTrue(gameMap.hasUnitAt(origin), "Expected the mech to be back at its original position after undoing the move");
    }
}
