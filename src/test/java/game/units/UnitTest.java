package game.units;

import game.units.movement.Flying;
import game.units.movement.Grounded;
import game.units.movement.Movement;
import game.units.weapons.Weapon;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {
    @Test
    public void testDamageReducesUnitHealth() {
        // Arrange
        Unit unit = new Unit(Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));

        // Act
        unit.damage();

        // Assert
        assertEquals(2, unit.getCurrentHealth());
    }

    @Test
    public void testUnitCanHaveCustomMaximumHealth() {
        // Arrange
        Unit unit = new Unit(Mockito.mock(Weapon.class), 5, Mockito.mock(Movement.class));

        // Act
        unit.damage();

        // Assert
        assertEquals(4, unit.getCurrentHealth());
    }

    @Test
    public void testUnitCanReceiveDifferentAmountsOfDamage() {
        // Arrange
        Unit unit = new Unit(Mockito.mock(Weapon.class), 5, Mockito.mock(Movement.class));

        // Act
        unit.damage(2);

        // Assert
        assertEquals(3, unit.getCurrentHealth());
    }

    @Test
    public void testUnitDiesWhenHealthReachesZero() {
        // Arrange
        Unit unit = new Unit(Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));

        // Act
        unit.damage(3);

        // Assert
        assertEquals(0, unit.getCurrentHealth());
        assertFalse(unit.isAlive());
    }

    @Test
    public void testContaminatedUnitReceivesDoubleDamage() {
        // Arrange
        Unit unit = new Unit(Mockito.mock(Weapon.class), 10, new Grounded(1));

        // Act
        unit.contaminate();
        unit.damage(2);

        // Assert
        assertEquals(6, unit.getCurrentHealth());
    }

    @Test
    public void testContaminationDoesNotStack() {
        // Arrange
        Unit unit = new Unit(Mockito.mock(Weapon.class), 10, new Grounded(1));

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
        Unit unit = new Unit(Mockito.mock(Weapon.class), 10, Mockito.mock(Movement.class));

        // Act
        unit.contaminate();
        unit.removeContamination();
        unit.damage(2);

        // Assert
        assertEquals(8, unit.getCurrentHealth());
    }

    @Test
    public void testFlyingUnitCannotBeContaminated() {
        Unit unit = new Unit(null, 10, new Flying(1));

        unit.contaminate();
        unit.damage(2);

        assertEquals(8, unit.getCurrentHealth());
    }

    @Test
    public void testFlyingUnitCanMoveToWater() {
        Unit unit = new Unit(null, 3, new Flying(1));
        assertTrue(unit.canMoveToWater());
    }

    @Test
    public void testGroundedUnitCannotMoveToWater() {
        Unit unit = new Unit(Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));

        assertFalse(unit.canMoveToWater());
    }

    @Test
    public void testGroundedUnitCanBeContaminated() {
        Unit unit = new Unit(Mockito.mock(Weapon.class), 10, new Grounded(1));

        unit.contaminate();
        unit.damage(2);

        assertEquals(6, unit.getCurrentHealth());
    }

    @Test
    public void testUnitCanBeKilledInstantly() {
        Unit unit = new Unit(Mockito.mock(Weapon.class), 10, Mockito.mock(Movement.class));

        unit.kill();

        assertEquals(0, unit.getCurrentHealth());
        assertFalse(unit.isAlive());
    }
}
