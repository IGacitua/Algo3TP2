package game.map.cell;

import game.position.Position;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CellTest {

    @Test
    public void testCanPlaceOneUnitInCell() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = cell.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertTrue(result, "Unit should be able to be placed.");
    }

    @Test
    public void testCannotPlaceMultipleUnitsInCell() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);

        // Act
        boolean mainResult = cell.tryPlaceUnit(mainUnit, false);
        boolean secondaryResult = cell.tryPlaceUnit(secondaryUnit, false);

        // Assert
        Assertions.assertTrue(mainResult, "Main unit should be placed successfully.");
        Assertions.assertFalse(secondaryResult, "Secondary unit should not be placed successfully.");
    }

    @Test
    public void testPopUnitReturnsCorrectUnit() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(unit, false);

        // Act
        Unit poppedUnit = cell.popUnit();

        // Assert
        Assertions.assertEquals(unit, poppedUnit, "Popped unit should be the same as the placed unit.");
    }

    @Test
    public void testPopUnitAllowsNewUnitPlacement() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit firstUnit = Mockito.mock(Unit.class);
        Unit secondUnit = Mockito.mock(Unit.class);

        // Act
        cell.tryPlaceUnit(firstUnit, false);
        cell.popUnit();
        boolean canPlaceSecondUnit = cell.tryPlaceUnit(secondUnit, false);

        // Assert
        Assertions.assertTrue(canPlaceSecondUnit, "Should be able to place a new unit after popping the first one.");
    }

    @Test
    public void testCellWithoutUnitCannotBeDamaged() {
        // Arrange
        Cell cell = new Cell(null);

        // Act
        boolean canDamage = cell.canDamage();

        // Assert
        Assertions.assertFalse(canDamage, "GenericCell cell should not be able to be damaged.");
    }

    @Test
    public void testCellWithUnitCanBeDamaged() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        Cell cell = new Cell(new Position(0, 0));

        // Act
        cell.tryPlaceUnit(unit, false);
        boolean canDamage = cell.canDamage();

        // Assert
        Assertions.assertTrue(canDamage, "GenericCell cell should be able to be damaged when a unit is present.");
    }

    @Test
    public void testCellDamagesUnitWhenDamaged() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        Cell cell = new Cell(new Position(0, 0));
        cell.tryPlaceUnit(unit, false);

        // Act
        cell.tryDamage(1);

        // Assert
        Mockito.verify(unit).damage(1);
    }

    @Test
    public void testHasPositionReturnsTrueForMatchingPosition() {
        // Arrange
        Position position = new Position(1, 1);
        Cell cell = new Cell(position);

        // Act
        boolean hasPosition = cell.hasPosition(new Position(1, 1));

        // Assert
        Assertions.assertTrue(hasPosition, "Cell should return true for matching position.");
    }

    @Test
    public void testCellHasUnitAfterPlacement() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        Cell cell = new Cell(new Position(0, 0));

        // Act
        cell.tryPlaceUnit(unit, false);
        boolean hasUnit = cell.hasUnit();

        // Assert
        Assertions.assertTrue(hasUnit, "Cell should report having a unit after placement.");
    }

    @Test
    public void testCellHasMechAfterPlacement() {
        // Arrange
        Unit mechUnit = Mockito.mock(Unit.class);
        Mockito.when(mechUnit.isMech()).thenReturn(true);
        Cell cell = new Cell(new Position(0, 0));

        // Act
        cell.tryPlaceUnit(mechUnit, false);
        boolean hasMech = cell.hasMech();

        // Assert
        Assertions.assertTrue(hasMech, "Cell should report having a mech after placement.");
    }
}