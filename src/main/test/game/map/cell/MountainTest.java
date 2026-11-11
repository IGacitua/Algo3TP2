package game.map.cell;

import game.map.Position2D;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MountainTest {

    @Test
    public void testCannotPlaceUnitInMountainCell() {
        // Arrange
        GenericCell mountain = new Mountain(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = mountain.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertFalse(result, "Unit should not be able to be placed.");
    }

    @Test // Just in case first placement has unexpected consequences.
    public void testCannotPlaceMultipleUnitsInMountainCell() {
        // Arrange
        GenericCell mountain = new Mountain(new Position2D(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);

        // Act
        boolean mainResult = mountain.tryPlaceUnit(mainUnit, false);
        boolean secondaryResult = mountain.tryPlaceUnit(secondaryUnit, false);

        // Assert
        Assertions.assertFalse(mainResult, "Main unit should not be placed successfully.");
        Assertions.assertFalse(secondaryResult, "Secondary unit should not be placed successfully.");
    }

    @Test
    public void testMountainCannotBeDamaged() {
        // Arrange
        GenericCell mountain = new Mountain(null);

        // Act
        boolean canDamage = mountain.canDamage();

        // Assert
        Assertions.assertFalse(canDamage, "Mountain cell should not be able to be damaged.");
    }
}