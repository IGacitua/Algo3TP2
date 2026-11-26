package integration;

import game.map.GameMap;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Team;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.movement.Grounded;
import game.units.weapons.AcidShot;
import game.units.weapons.Punch;
import game.units.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import game.units.movement.Flying;
import game.map.cell.Mountain;
import static org.junit.jupiter.api.Assertions.*;

public class AcidShotIntegrationTest {

    private GameMap gameMap;

    @BeforeEach
    public void setUp() throws InvalidMapFileException {
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[8][8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                cells[2][3] = new Mountain(new Position(2, 3));
                cells[x][y] = new Cell(new Position(x, y));
            }
        }

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        gameMap = new GameMap(importer, "");
    }

    @Test
    public void acidShotDamagesAndContaminatesGroundedUnit()
            throws PositionOutOfBoundsException {

        // Arrange
        Weapon acidShot = new AcidShot(gameMap);

        Unit firefly = new Unit(
                Team.Enemy, acidShot, 3, new Grounded(2));

        Unit mech = new Unit(
                Team.Player, new Punch(gameMap), 3, new Grounded(3));

        Position fireflyPosition = new Position(1, 1);
        Position mechPosition = new Position(1, 4);

        assertTrue(gameMap.tryPlaceUnit(firefly, fireflyPosition));
        assertTrue(gameMap.tryPlaceUnit(mech, mechPosition));

        UnitAction attack = firefly.getAttackAction(
                fireflyPosition, mechPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit);
        assertEquals(2, mech.getHealth());
        assertTrue(mech.isContaminated());
    }

    @Test
    public void acidShotDoesNotStackContamination()
            throws PositionOutOfBoundsException {

        // Arrange
        Weapon acidShot = new AcidShot(gameMap);

        Unit firefly = new Unit(
                Team.Enemy, acidShot, 3, new Grounded(2));

        Unit mech = new Unit(
                Team.Player, new Punch(gameMap), 3, new Grounded(3));

        Position fireflyPosition = new Position(1, 1);
        Position mechPosition = new Position(1, 4);

        assertTrue(gameMap.tryPlaceUnit(firefly, fireflyPosition));
        assertTrue(gameMap.tryPlaceUnit(mech, mechPosition));

        mech.contaminate();

        UnitAction attack = firefly.getAttackAction(
                fireflyPosition, mechPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit);
        assertTrue(mech.isContaminated());
        assertEquals(1, mech.getHealth());
        assertTrue(mech.isAlive());
    }

    @Test
    public void acidShotDoesNotContaminateFlyingUnit()
            throws PositionOutOfBoundsException {

        // Arrange
        Weapon acidShot = new AcidShot(gameMap);

        Unit firefly = new Unit(
                Team.Enemy, acidShot, 3, new Grounded(2));

        Unit hornet = new Unit(
                Team.Enemy, Mockito.mock(Weapon.class), 2, new Flying(4));

        Position fireflyPosition = new Position(1, 1);
        Position hornetPosition = new Position(1, 4);

        assertTrue(gameMap.tryPlaceUnit(firefly, fireflyPosition));
        assertTrue(gameMap.tryPlaceUnit(hornet, hornetPosition));

        UnitAction attack = firefly.getAttackAction(
                fireflyPosition, hornetPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit);
        assertEquals(1, hornet.getHealth());
        assertFalse(hornet.isContaminated());
    }

    @Test
    public void acidShotHitsOnlyFirstUnit()
            throws PositionOutOfBoundsException {

        // Arrange
        Unit firefly = new Unit(
                Team.Enemy, new AcidShot(gameMap), 3, new Grounded(2));

        Unit firstMech = new Unit(
                Team.Player, new Punch(gameMap), 3, new Grounded(3));

        Unit secondMech = new Unit(
                Team.Player, new Punch(gameMap), 3, new Grounded(3));

        Position fireflyPosition = new Position(1, 1);
        Position firstMechPosition = new Position(1, 4);
        Position secondMechPosition = new Position(1, 7);

        assertTrue(gameMap.tryPlaceUnit(firefly, fireflyPosition));
        assertTrue(gameMap.tryPlaceUnit(firstMech, firstMechPosition));
        assertTrue(gameMap.tryPlaceUnit(secondMech, secondMechPosition));

        UnitAction attack = firefly.getAttackAction(
                fireflyPosition, secondMechPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit);

        assertEquals(2, firstMech.getHealth());
        assertTrue(firstMech.isContaminated());

        assertEquals(3, secondMech.getHealth());
        assertFalse(secondMech.isContaminated());
    }

    @Test
    public void acidShotDoesNotPushTarget()
            throws PositionOutOfBoundsException {

        // Arrange
        Unit firefly = new Unit(
                Team.Enemy, new AcidShot(gameMap), 3, new Grounded(2));

        Unit mech = new Unit(
                Team.Player, new Punch(gameMap), 3, new Grounded(3));

        Position fireflyPosition = new Position(1, 1);
        Position mechPosition = new Position(1, 4);
        Position behindMech = new Position(1, 5);

        assertTrue(gameMap.tryPlaceUnit(firefly, fireflyPosition));
        assertTrue(gameMap.tryPlaceUnit(mech, mechPosition));

        UnitAction attack = firefly.getAttackAction(
                fireflyPosition, mechPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit);
        assertTrue(gameMap.hasUnitAt(mechPosition));
        assertFalse(gameMap.hasUnitAt(behindMech));
        assertEquals(2, mech.getHealth());
        assertTrue(mech.isContaminated());
    }

    @Test
    public void acidShotStopsAtMountain()
            throws PositionOutOfBoundsException {

        // Arrange
        Unit firefly = new Unit(
                Team.Enemy, new AcidShot(gameMap), 3, new Grounded(2));

        Unit mech = new Unit(
                Team.Player, new Punch(gameMap), 3, new Grounded(3));

        Position fireflyPosition = new Position(2, 1);
        Position mechPosition = new Position(2, 5);

        assertTrue(gameMap.tryPlaceUnit(firefly, fireflyPosition));
        assertTrue(gameMap.tryPlaceUnit(mech, mechPosition));

        UnitAction attack = firefly.getAttackAction(
                fireflyPosition, mechPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit);
        assertEquals(3, mech.getHealth());
        assertFalse(mech.isContaminated());
        assertTrue(gameMap.hasUnitAt(mechPosition));
    }
}
