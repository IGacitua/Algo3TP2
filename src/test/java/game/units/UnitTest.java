package game.units;

import game.units.movement.Flying;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {

    // TODO: Verify whether it's still needed
    /*
    private GameMap gameMap;

    @BeforeEach
    public void setUp() throws InvalidMapFileException {
        // Preparar un mapa de 1x1 antes de cada test
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[1][1];
        cells[0][0] = new Cell(new Position(0, 0));

        Mockito.when(importer.importMap(Mockito.anyString())).thenReturn(cells);

        gameMap = new GameMap(importer, "");
    }
    */

    @Test
    public void testDamageReducesUnitHealth() {
        // Arrange
        Unit unit = new Unit(null);

        // Act
        unit.damage();

        // Assert
        assertEquals(2, unit.getCurrentHealth());
    }

    @Test
    public void testUnitCanHaveCustomMaximumHealth() {
        // Arrange
        Unit unit = new Unit(null, 5);

        // Act
        unit.damage();

        // Assert
        assertEquals(4, unit.getCurrentHealth());
    }

    @Test
    public void testUnitCanReceiveDifferentAmountsOfDamage() {
        // Arrange
        Unit unit = new Unit(null, 5);

        // Act
        unit.damage(2);

        // Assert
        assertEquals(3, unit.getCurrentHealth());
    }

    @Test
    public void testUnitDiesWhenHealthReachesZero() {
        // Arrange
        Unit unit = new Unit(null, 3);

        // Act
        unit.damage(3);

        // Assert
        assertEquals(0, unit.getCurrentHealth());
        assertFalse(unit.isAlive());
    }

    @Test
    public void testContaminatedUnitReceivesDoubleDamage() {
        // Arrange
        Unit unit = new Unit(null, 10);

        // Act
        unit.contaminate();
        unit.damage(2);

        // Assert
        assertEquals(6, unit.getCurrentHealth());
    }

    @Test
    public void testContaminationDoesNotStack() {
        // Arrange
        Unit unit = new Unit(null, 10);

        // Act
        unit.contaminate();
        unit.contaminate();
        unit.damage(2);

        // Assert
        assertEquals(6, unit.getCurrentHealth());
    }

    @Test
    public void testRemovingContaminationRestoresNormalDamage() {
        // Arrange
        Unit unit = new Unit(null, 10);

        // Act
        unit.contaminate();
        unit.removeContamination();
        unit.damage(2);

        // Assert
        assertEquals(8, unit.getCurrentHealth());
    }

    @Test
    public void testFlyingUnitCannotBeContaminated() {
        Unit unit = new Unit(

                null,
                10,
                new Flying()
        );

        unit.contaminate();
        unit.damage(2);

        assertEquals(8, unit.getCurrentHealth());
    }

    @Test
    public void testFlyingUnitCanMoveToWater() {
        Unit unit = new Unit(

                null,
                3,
                new Flying()
        );

        assertTrue(unit.canMoveToWater());
    }

    @Test
    public void testGroundedUnitCannotMoveToWater() {
        Unit unit = new Unit(

                null,
                3
        );

        assertFalse(unit.canMoveToWater());
    }

    @Test
    public void testGroundedUnitCanBeContaminated() {
        Unit unit = new Unit(

                null,
                10
        );

        unit.contaminate();
        unit.damage(2);

        assertEquals(6, unit.getCurrentHealth());
    }

    @Test
    public void testUnitCanBeKilledInstantly() {
        Unit unit = new Unit(

                null,
                10
        );

        unit.kill();

        assertEquals(0, unit.getCurrentHealth());
        assertFalse(unit.isAlive());
    }
}
