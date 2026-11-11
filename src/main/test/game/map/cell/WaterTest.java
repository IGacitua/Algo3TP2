package game.map.cell;

import game.map.Position2D;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class WaterTest {

    @Test
    public void testCanPlaceOneWaterUnitInWaterCell() {
        // Arrange
        Cell water = new Water(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater()).thenReturn(true);

        // Act
        boolean result = water.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertTrue(result, "Unit should be able to be placed.");
    }

    @Test
    public void testCannotPlaceNonWaterUnitInWaterCell() {
        // Arrange
        Cell water = new Water(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater()).thenReturn(false);

        // Act
        boolean result = water.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertFalse(result, "Non-water unit should not be able to be placed.");
    }

    @Test
    public void testCanForceNonWaterUnitPlacementInWaterCell() {
        // Arrange
        Cell water = new Water(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater()).thenReturn(false);

        // Act
        boolean result = water.tryPlaceUnit(unit, true);

        // Assert
        Assertions.assertTrue(result, "Non-water unit should be able to be placed when forced.");
    }

    @Test
    public void testCannotPlaceMultipleWaterUnitsInWaterCell() {
        // Arrange
        Cell water = new Water(new Position2D(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);
        Mockito.when(mainUnit.canMoveToWater()).thenReturn(true);
        Mockito.when(secondaryUnit.canMoveToWater()).thenReturn(true);

        // Act
        boolean mainResult = water.tryPlaceUnit(mainUnit, false);
        boolean secondaryResult = water.tryPlaceUnit(secondaryUnit, false);

        // Assert
        Assertions.assertTrue(mainResult, "Main unit should be placed successfully.");
        Assertions.assertFalse(secondaryResult, "Secondary unit should not be placed successfully.");
    }

    @Test
    public void testPlacingWaterUnitMovesItToPosition() {
        // Arrange
        Position2D position = new Position2D(0, 0);
        Cell water = new Water(position);
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater()).thenReturn(true);

        // Act
        water.tryPlaceUnit(unit, false);

        // Assert
        Mockito.verify(unit).moveTo(position);
    }

    @Test
    public void testPopWaterUnitReturnsCorrectUnit() {
        // Arrange
        Cell water = new Water(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater()).thenReturn(true);
        water.tryPlaceUnit(unit, false);

        // Act
        Unit poppedUnit = water.popUnit();

        // Assert
        Assertions.assertEquals(unit, poppedUnit, "Popped unit should be the same as the placed unit.");
    }

    @Test
    public void testPopWaterUnitAllowsNewWaterUnitPlacement() {
        // Arrange
        Cell water = new Water(new Position2D(0, 0));
        Unit firstUnit = Mockito.mock(Unit.class);
        Unit secondUnit = Mockito.mock(Unit.class);
        Mockito.when(firstUnit.canMoveToWater()).thenReturn(true);
        Mockito.when(secondUnit.canMoveToWater()).thenReturn(true);

        // Act
        water.tryPlaceUnit(firstUnit, false);
        water.popUnit();
        boolean canPlaceSecondUnit = water.tryPlaceUnit(secondUnit, false);

        // Assert
        Assertions.assertTrue(canPlaceSecondUnit, "Should be able to place a new unit after popping the first one.");
    }

    @Test
    public void testWaterWithoutUnitCannotBeDamaged() {
        // Arrange
        Cell water = new Water(null);

        // Act
        boolean canDamage = water.canDamage();

        // Assert
        Assertions.assertFalse(canDamage, "Water cell should not be able to be damaged.");
    }

    @Test
    public void testWaterWithUnitCanBeDamaged() {
        // Arrange
        Cell water = new Water(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater()).thenReturn(true);

        // Act
        water.tryPlaceUnit(unit, false);
        boolean canDamage = water.canDamage();

        // Assert
        Assertions.assertTrue(canDamage, "Water cell should be able to be damaged when a unit is present.");
    }

    @Test
    public void testWaterDamagesUnitWhenDamaged() {
        // Arrange
        Cell water = new Water(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater()).thenReturn(true);
        water.tryPlaceUnit(unit, false);

        // Act
        water.tryDamage();

        // Assert
        Mockito.verify(unit).damage();
    }
}