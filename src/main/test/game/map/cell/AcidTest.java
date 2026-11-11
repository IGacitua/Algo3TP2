package game.map.cell;

import game.map.Position2D;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AcidTest {

    @Test
    public void testCanPlaceOneUnitInAcidCell() {
        // Arrange
        GenericCell acid = new Acid(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = acid.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertTrue(result, "Unit should be able to be placed.");
    }

    @Test
    public void testCannotPlaceMultipleUnitsInAcidCell() {
        // Arrange
        GenericCell acid = new Acid(new Position2D(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);

        // Act
        boolean mainResult = acid.tryPlaceUnit(mainUnit, false);
        boolean secondaryResult = acid.tryPlaceUnit(secondaryUnit, false);

        // Assert
        Assertions.assertTrue(mainResult, "Main unit should be placed successfully.");
        Assertions.assertFalse(secondaryResult, "Secondary unit should not be placed successfully.");
    }

    @Test
    public void testPlacingUnitMovesItToPosition() {
        // Arrange
        Position2D position = new Position2D(0, 0);
        GenericCell acid = new Acid(position);
        Unit unit = Mockito.mock(Unit.class);

        // Act
        acid.tryPlaceUnit(unit, false);

        // Assert
        Mockito.verify(unit).moveTo(position);
    }

    @Test
    public void testPopUnitReturnsCorrectUnit() {
        // Arrange
        GenericCell acid = new Acid(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        acid.tryPlaceUnit(unit, false);

        // Act
        Unit poppedUnit = acid.popUnit();

        // Assert
        Assertions.assertEquals(unit, poppedUnit, "Popped unit should be the same as the placed unit.");
    }

    @Test
    public void testPopUnitAllowsNewUnitPlacement() {
        // Arrange
        GenericCell acid = new Acid(new Position2D(0, 0));
        Unit firstUnit = Mockito.mock(Unit.class);
        Unit secondUnit = Mockito.mock(Unit.class);

        // Act
        acid.tryPlaceUnit(firstUnit, false);
        acid.popUnit();
        boolean canPlaceSecondUnit = acid.tryPlaceUnit(secondUnit, false);

        // Assert
        Assertions.assertTrue(canPlaceSecondUnit, "Should be able to place a new unit after popping the first one.");
    }

    @Test
    public void testAcidWithoutUnitCannotBeDamaged() {
        // Arrange
        GenericCell acid = new Acid(null);

        // Act
        boolean canDamage = acid.canDamage();

        // Assert
        Assertions.assertFalse(canDamage, "Acid cell should not be able to be damaged.");
    }

    @Test
    public void testAcidWithUnitCanBeDamaged() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        GenericCell acid = new Acid(new Position2D(0, 0));

        // Act
        acid.tryPlaceUnit(unit, false);
        boolean canDamage = acid.canDamage();

        // Assert
        Assertions.assertTrue(canDamage, "Acid cell should be able to be damaged when a unit is present.");
    }

    @Test
    public void testAcidDamagesUnitWhenDamaged() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        GenericCell acid = new Acid(new Position2D(0, 0));
        acid.tryPlaceUnit(unit, false);

        // Act
        acid.tryDamage();

        // Assert
        Mockito.verify(unit).damage();
    }

    @Test
    public void testAcidContaminatesUnitOnPlacement() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        GenericCell acid = new Acid(new Position2D(0, 0));

        // Act
        acid.tryPlaceUnit(unit, false);

        // Assert
        Mockito.verify(unit).contaminate();
    }

    @Test
    public void testHasPositionReturnsTrueForMatchingPosition() {
        // Arrange
        Position2D position = new Position2D(1, 1);
        GenericCell cell = new Acid(position);

        // Act
        boolean hasPosition = cell.hasPosition(new Position2D(1, 1));

        // Assert
        Assertions.assertTrue(hasPosition, "Acid should return true for matching position.");
    }
}
