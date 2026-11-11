package game.map.cell;

import game.map.Position2D;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GroundTest {

    @Test
    public void testCanPlaceOneUnitInGroundCell() {
        // Arrange
        Cell ground = new Ground(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = ground.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertTrue(result, "Unit should be able to be placed.");
    }

    @Test
    public void testCannotPlaceMultipleUnitsInGroundCell() {
        // Arrange
        Cell ground = new Ground(new Position2D(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);

        // Act
        boolean mainResult = ground.tryPlaceUnit(mainUnit, false);
        boolean secondaryResult = ground.tryPlaceUnit(secondaryUnit, false);

        // Assert
        Assertions.assertTrue(mainResult, "Main unit should be placed successfully.");
        Assertions.assertFalse(secondaryResult, "Secondary unit should not be placed successfully.");
    }

    @Test
    public void testPlacingUnitMovesItToPosition() {
        // Arrange
        Position2D position = new Position2D(0, 0);
        Cell ground = new Ground(position);
        Unit unit = Mockito.mock(Unit.class);

        // Act
        ground.tryPlaceUnit(unit, false);

        // Assert
        Mockito.verify(unit).moveTo(position);
    }

    @Test
    public void testPopUnitReturnsCorrectUnit() {
        // Arrange
        Cell ground = new Ground(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        ground.tryPlaceUnit(unit, false);

        // Act
        Unit poppedUnit = ground.popUnit();

        // Assert
        Assertions.assertEquals(unit, poppedUnit, "Popped unit should be the same as the placed unit.");
    }

    @Test
    public void testPopUnitAllowsNewUnitPlacement() {
        // Arrange
        Cell ground = new Ground(new Position2D(0, 0));
        Unit firstUnit = Mockito.mock(Unit.class);
        Unit secondUnit = Mockito.mock(Unit.class);

        // Act
        ground.tryPlaceUnit(firstUnit, false);
        ground.popUnit();
        boolean canPlaceSecondUnit = ground.tryPlaceUnit(secondUnit, false);

        // Assert
        Assertions.assertTrue(canPlaceSecondUnit, "Should be able to place a new unit after popping the first one.");
    }

    @Test
    public void testGroundWithoutUnitCannotBeDamaged() {
        // Arrange
        Cell ground = new Ground(null);

        // Act
        boolean canDamage = ground.canDamage();

        // Assert
        Assertions.assertFalse(canDamage, "Ground cell should not be able to be damaged.");
    }

    @Test
    public void testGroundWithUnitCanBeDamaged() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        Cell ground = new Ground(new Position2D(0, 0));

        // Act
        ground.tryPlaceUnit(unit, false);
        boolean canDamage = ground.canDamage();

        // Assert
        Assertions.assertTrue(canDamage, "Ground cell should be able to be damaged when a unit is present.");
    }

    @Test
    public void testGroundDamagesUnitWhenDamaged() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        Cell ground = new Ground(new Position2D(0, 0));
        ground.tryPlaceUnit(unit, false);

        // Act
        ground.tryDamage();

        // Assert
        Mockito.verify(unit).damage();
    }
}