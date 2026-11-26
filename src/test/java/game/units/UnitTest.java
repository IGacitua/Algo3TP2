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
        Unit unit = new Unit(Team.Player, Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));

        // Act
        unit.dealDamage(1);

        // Assert
        assertEquals(2, unit.getHealth());
    }

    @Test
    public void testUnitCanHaveCustomMaximumHealth() {
        // Arrange
        Unit unit = new Unit(Team.Player, Mockito.mock(Weapon.class), 5, Mockito.mock(Movement.class));

        // Act
        unit.dealDamage(1);

        // Assert
        assertEquals(4, unit.getHealth());
    }

    @Test
    public void testUnitCanReceiveDifferentAmountsOfDamage() {
        // Arrange
        Unit unit = new Unit(Team.Player, Mockito.mock(Weapon.class), 5, Mockito.mock(Movement.class));

        // Act
        unit.dealDamage(2);

        // Assert
        assertEquals(3, unit.getHealth());
    }

    @Test
    public void testUnitDiesWhenHealthReachesZero() {
        // Arrange
        Unit unit = new Unit(Team.Player, Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));

        // Act
        unit.dealDamage(3);

        // Assert
        assertEquals(0, unit.getHealth());
        assertFalse(unit.isAlive());
    }

    @Test
    public void testContaminatedUnitReceivesDoubleDamage() {
        // Arrange
        Unit unit = new Unit(Team.Player, Mockito.mock(Weapon.class), 10, new Grounded(1));

        // Act
        unit.contaminate();
        unit.dealDamage(2);

        // Assert
        assertEquals(6, unit.getHealth());
    }

    @Test
    public void testContaminationDoesNotStack() {
        // Arrange
        Unit unit = new Unit(Team.Player, Mockito.mock(Weapon.class), 10, new Grounded(1));

        // Act
        unit.contaminate();
        unit.contaminate();
        unit.dealDamage(2);

        // Assert
        assertEquals(6, unit.getHealth());
    }

    @Test
    public void testRemovingContaminationRestoresNormalDamage() {
        // Arrange
        Unit unit = new Unit(Team.Player, Mockito.mock(Weapon.class), 10, Mockito.mock(Movement.class));

        // Act
        unit.contaminate();
        unit.removeContamination();
        unit.dealDamage(2);

        // Assert
        assertEquals(8, unit.getHealth());
    }

    @Test
    public void testFlyingUnitCannotBeContaminated() {
        Unit unit = new Unit(Team.Player, null, 10, new Flying(1));

        unit.contaminate();
        unit.dealDamage(2);

        assertEquals(8, unit.getHealth());
    }

    @Test
    public void testFlyingUnitCanMoveToWater() {
        Unit unit = new Unit(Team.Player, null, 3, new Flying(1));
        assertTrue(unit.canMoveToWater());
    }

    @Test
    public void testGroundedUnitCannotMoveToWater() {
        Unit unit = new Unit(Team.Player, Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));

        assertFalse(unit.canMoveToWater());
    }

    @Test
    public void testGroundedUnitCanBeContaminated() {
        Unit unit = new Unit(Team.Player, Mockito.mock(Weapon.class), 10, new Grounded(1));

        unit.contaminate();
        unit.dealDamage(2);

        assertEquals(6, unit.getHealth());
    }

    @Test
    public void testUnitCanBeKilledInstantly() {
        Unit unit = new Unit(Team.Player, Mockito.mock(Weapon.class), 10, Mockito.mock(Movement.class));

        unit.kill();

        assertEquals(0, unit.getHealth());
        assertFalse(unit.isAlive());
    }
}
