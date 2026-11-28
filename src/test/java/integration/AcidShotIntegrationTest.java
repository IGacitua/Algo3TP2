package integration;

import game.map.GameMap;
import game.map.cell.Cell;
import game.map.cell.Mountain;
import game.map.creation.MapImporter;
import game.position.Position;
import game.units.Unit;
import game.units.UnitFactory;
import game.units.actions.UnitAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class AcidShotIntegrationTest {

    private GameMap gameMap;

    @BeforeEach
    public void setUp() {
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = createCells();
        cells[2][3] = new Mountain(new Position(2, 3));

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        gameMap = new GameMap(importer, "");
    }

    private Cell[][] createCells() {
        Cell[][] cells = new Cell[8][8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                cells[x][y] = new Cell(new Position(x, y));
            }
        }

        return cells;
    }

    @Test
    public void acidShotDamagesAndContaminatesGroundedUnit() {
        // Arrange
        Unit firefly = UnitFactory.createFireflyVek();
        Unit mech = UnitFactory.createCombatMek();

        Position fireflyPosition = new Position(1, 1);
        Position mechPosition = new Position(1, 4);

        gameMap.tryPlaceUnit(firefly, fireflyPosition);
        gameMap.tryPlaceUnit(mech, mechPosition);

        UnitAction attack = firefly.getAttackAction(fireflyPosition, mechPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit, "Acid shot should hit the mech.");
        assertEquals(2, mech.getHealth(), "Mech should take 1 damage from the acid shot.");
        assertTrue(mech.isContaminated(), "Mech should be contaminated by the acid shot.");
    }

    @Test
    public void acidShotDoesNotStackContamination() {
        // Arrange
        Unit firefly = UnitFactory.createFireflyVek();
        Unit mech = UnitFactory.createCombatMek();

        Position fireflyPosition = new Position(1, 1);
        Position mechPosition = new Position(1, 4);

        mech.contaminate();

        gameMap.tryPlaceUnit(firefly, fireflyPosition);
        gameMap.tryPlaceUnit(mech, mechPosition);

        UnitAction attack = firefly.getAttackAction(fireflyPosition, mechPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit, "Acid shot should hit the already contaminated mech.");
        assertEquals(1, mech.getHealth(), "Contaminated mech should take double damage.");
        assertTrue(mech.isContaminated(), "Mech should remain contaminated.");
        assertTrue(mech.isAlive(), "Mech should survive a single contaminated acid shot.");
    }

    @Test
    public void acidShotDoesNotContaminateFlyingUnit() {
        // Arrange
        Unit firefly = UnitFactory.createFireflyVek();
        Unit hornet = UnitFactory.createHornetVek();

        Position fireflyPosition = new Position(1, 1);
        Position hornetPosition = new Position(1, 4);

        gameMap.tryPlaceUnit(firefly, fireflyPosition);
        gameMap.tryPlaceUnit(hornet, hornetPosition);

        UnitAction attack = firefly.getAttackAction(fireflyPosition, hornetPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit, "Acid shot should hit the flying unit.");
        assertEquals(1, hornet.getHealth(), "Flying unit should still take normal damage from the acid shot.");
        assertFalse(hornet.isContaminated(), "Flying unit should not be contaminated.");
    }

    @Test
    public void acidShotHitsOnlyFirstUnit() {
        // Arrange
        Unit firefly = UnitFactory.createFireflyVek();
        Unit firstMech = UnitFactory.createCombatMek();
        Unit secondMech = UnitFactory.createCombatMek();

        Position fireflyPosition = new Position(1, 1);
        Position firstMechPosition = new Position(1, 4);
        Position secondMechPosition = new Position(1, 7);

        gameMap.tryPlaceUnit(firefly, fireflyPosition);
        gameMap.tryPlaceUnit(firstMech, firstMechPosition);
        gameMap.tryPlaceUnit(secondMech, secondMechPosition);

        UnitAction attack = firefly.getAttackAction(fireflyPosition, secondMechPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit, "Acid shot should hit the first unit in its path.");

        assertEquals(2, firstMech.getHealth(), "First mech should take 1 damage from the acid shot.");
        assertTrue(firstMech.isContaminated(), "First mech should be contaminated by the acid shot.");

        assertEquals(3, secondMech.getHealth(), "Second mech should not be damaged.");
        assertFalse(secondMech.isContaminated(), "Second mech should not be contaminated.");
    }

    @Test
    public void acidShotDoesNotPushTarget() {
        // Arrange
        Unit firefly = UnitFactory.createFireflyVek();
        Unit mech = UnitFactory.createCombatMek();

        Position fireflyPosition = new Position(1, 1);
        Position mechPosition = new Position(1, 4);
        Position behindMech = new Position(1, 5);

        gameMap.tryPlaceUnit(firefly, fireflyPosition);
        gameMap.tryPlaceUnit(mech, mechPosition);

        UnitAction attack = firefly.getAttackAction(fireflyPosition, mechPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit, "Acid shot should hit the mech.");
        assertTrue(gameMap.hasUnitAt(mechPosition), "Mech should stay in its original cell.");
        assertFalse(gameMap.hasUnitAt(behindMech), "Mech should not be pushed to the cell behind it.");
        assertEquals(2, mech.getHealth(), "Mech should take 1 damage from the acid shot.");
        assertTrue(mech.isContaminated(), "Mech should be contaminated by the acid shot.");
    }

    @Test
    public void acidShotStopsAtMountain() {
        // Arrange
        Unit firefly = UnitFactory.createFireflyVek();
        Unit mech = UnitFactory.createCombatMek();

        Position fireflyPosition = new Position(2, 1);
        Position mechPosition = new Position(2, 5);

        gameMap.tryPlaceUnit(firefly, fireflyPosition);
        gameMap.tryPlaceUnit(mech, mechPosition);

        UnitAction attack = firefly.getAttackAction(fireflyPosition, mechPosition);

        // Act
        boolean hit = attack.tryExecute(gameMap);

        // Assert
        assertTrue(hit, "Attack should be executed.");
        assertEquals(3, mech.getHealth(), "Mech behind the mountain should not be damaged.");
        assertFalse(mech.isContaminated(), "Mech behind the mountain should not be contaminated.");
        assertTrue(gameMap.hasUnitAt(mechPosition), "Mech should remain in its cell.");
    }
}