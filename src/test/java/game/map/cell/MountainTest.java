package game.map.cell;

import game.position.Position;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MountainTest {

    // Placement

    @Test
    public void testCannotPlaceUnit() {
        // Arrange
        Cell mountain = new Mountain(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = mountain.tryPlaceUnit(unit, false);
        boolean hasUnit = mountain.hasUnit();

        // Assert
        Assertions.assertFalse(result, "Unit should not be able to be placed.");
        Assertions.assertFalse(hasUnit, "Mountain should remain empty.");
    }

    @Test
    public void testCannotPlacePushedUnit() {
        // Arrange
        Cell mountain = new Mountain(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = mountain.tryPlaceUnit(unit, true);
        boolean hasUnit = mountain.hasUnit();

        // Assert
        Assertions.assertFalse(result, "Pushed unit should not be able to be placed.");
        Assertions.assertFalse(hasUnit, "Mountain should remain empty.");
    }

    @Test
    public void testCanPlaceUnitIsFalse() {
        // Arrange
        Cell mountain = new Mountain(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = mountain.canPlaceUnit(unit, false);

        // Assert
        Assertions.assertFalse(result, "Mountain should never accept units.");
    }

    @Test
    public void testIsAlwaysFull() {
        // Arrange
        Cell mountain = new Mountain(new Position(0, 0));

        // Act
        boolean result = mountain.isFull();

        // Assert
        Assertions.assertTrue(result, "Mountain blocks movement, so it should always be full.");
    }

    // Damage

    @Test
    public void testCannotBeDamaged() {
        // Arrange
        Cell mountain = new Mountain(new Position(0, 0));

        // Act
        boolean result = mountain.canDamage();

        // Assert
        Assertions.assertFalse(result, "Mountain is indestructible.");
    }
}