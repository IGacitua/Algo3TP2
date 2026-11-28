package integration;

import game.map.GameMap;
import game.map.cell.Acid;
import game.map.cell.Cell;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.direction.West;
import game.units.Unit;
import game.units.UnitFactory;
import game.units.actions.UnitAction;
import game.units.behavior.player.PlayerBehavior;
import game.units.behavior.player.TurnUndoTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class AcidIntegrationTest {

    private GameMap gameMap;

    @BeforeEach
    public void setUp() {
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = createCells();
        Position acidPosition = new Position(0, 0);
        cells[acidPosition.getX()][acidPosition.getY()] = new Acid(acidPosition);

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        gameMap = new GameMap(importer, "");
    }

    private Cell[][] createCells() {
        Cell[][] cells = new Cell[3][3];

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                cells[x][y] = new Cell(new Position(x, y));
            }
        }

        return cells;
    }

    @Test
    public void mekOnAcidReceivesDoubleDamage() {
        // Arrange
        Unit unit = UnitFactory.createCombatMek();
        Position acidPosition = new Position(0, 0);
        int expectedHealth = 0;

        // Act
        gameMap.tryPlaceUnit(unit, acidPosition);
        unit.dealDamage(2);

        // Assert
        assertEquals(expectedHealth, unit.getHealth());
    }

    @Test
    public void flyingUnitOnAcidReceivesNormalDamage() {
        // Arrange
        Unit unit = UnitFactory.createHornetVek();

        // Act
        unit.dealDamage(2);

        // Assert
        assertEquals(0, unit.getHealth());
    }

    @Test
    public void groundedUnitPushedIntoAcidReceivesDoubleDamage() {
        // Arrange
        Unit unit = UnitFactory.createCombatMek();
        Position origin = new Position(1, 0);

        // Act
        gameMap.tryPlaceUnit(unit, origin);
        gameMap.pushUnit(origin, new West());
        unit.dealDamage(1);

        // Assert
        assertEquals(1, unit.getHealth());
    }

    @Test
    public void flyingUnitPushedIntoAcidReceivesNormalDamage() {
        // Arrange
        Unit unit = UnitFactory.createHornetVek();
        Position origin = new Position(1, 0);

        // Act
        gameMap.tryPlaceUnit(unit, origin);
        gameMap.pushUnit(origin, new West());
        unit.dealDamage(1);

        // Assert
        assertEquals(1, unit.getHealth());
    }

    @Test
    public void undoingMoveOntoAcidRemovesContamination() {
        // Arrange
        Unit mech = UnitFactory.createCombatMek();
        Position origin = new Position(1, 0);
        Position destination = new Position(0, 0);

        PlayerBehavior player = new PlayerBehavior(mech, new TurnUndoTracker());

        gameMap.tryPlaceUnit(mech, origin);
        UnitAction moveAction = mech.getMovementAction(origin, destination);

        // Act
        player.executeAction(moveAction, gameMap);
        player.undoMovement(gameMap);

        // Assert
        assertFalse(mech.isContaminated());
        assertTrue(gameMap.hasUnitAt(origin));
    }
}
