package game.map.cell;

import game.position.Position;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CellTest {

    // Placement

    @Test
    public void testCanPlaceOneUnit() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = cell.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertTrue(result, "Unit should be able to be placed.");
    }

    @Test
    public void testCanPushOneUnit() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = cell.tryPlaceUnit(unit, true);

        // Assert
        Assertions.assertTrue(result, "Pushed unit should be able to be placed.");
    }

    @Test
    public void testCannotPlaceMultipleUnits() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);

        // Act
        boolean mainResult = cell.tryPlaceUnit(mainUnit, false);
        boolean secondaryResult = cell.tryPlaceUnit(secondaryUnit, false);

        // Assert
        Unit remainingUnit = cell.peekUnit();
        Assertions.assertTrue(mainResult, "Main unit should be placed successfully.");
        Assertions.assertFalse(secondaryResult, "Secondary unit should not be placed successfully.");
        Assertions.assertSame(mainUnit, remainingUnit, "Main unit should remain in the cell.");
    }

    @Test
    public void testCanPlaceUnitIsTrueWhenEmpty() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = cell.canPlaceUnit(unit, false);

        // Assert
        Assertions.assertTrue(result, "Empty cell should accept a unit.");
    }

    @Test
    public void testCanPlaceUnitIsFalseWhenFull() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(mainUnit, false);

        // Act
        boolean result = cell.canPlaceUnit(secondaryUnit, false);

        // Assert
        Assertions.assertFalse(result, "Full cell should not accept a unit.");
    }


    // Peek

    @Test
    public void testPeekUnitReturnsNullWhenEmpty() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));

        // Act
        Unit result = cell.peekUnit();

        // Assert
        Assertions.assertNull(result, "Empty cell should have no unit to peek.");
    }

    @Test
    public void testPeekUnitReturnsUnitWithoutRemovingIt() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(unit, false);

        // Act
        Unit peekedUnit = cell.peekUnit();
        boolean hasUnit = cell.hasUnit();

        // Assert
        Assertions.assertSame(unit, peekedUnit, "Peeked unit should be the placed unit.");
        Assertions.assertTrue(hasUnit, "Peeking should not remove the unit.");
    }

    @Test
    public void testPopUnitReturnsNullWhenEmpty() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));

        // Act
        Unit result = cell.popUnit();

        // Assert
        Assertions.assertNull(result, "Empty cell should have no unit to pop.");
    }

    @Test
    public void testPopUnitReturnsCorrectUnit() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(unit, false);

        // Act
        Unit poppedUnit = cell.popUnit();
        boolean hasUnit = cell.hasUnit();

        // Assert
        Assertions.assertSame(unit, poppedUnit, "Popped unit should be the same as the placed unit.");
        Assertions.assertFalse(hasUnit, "Cell should be empty after popping.");
    }

    @Test
    public void testPopUnitAllowsNewUnitPlacement() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit firstUnit = Mockito.mock(Unit.class);
        Unit secondUnit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(firstUnit, false);

        // Act
        cell.popUnit();
        boolean result = cell.tryPlaceUnit(secondUnit, false);

        // Assert
        Assertions.assertTrue(result, "Should be able to place a new unit after popping the first one.");
    }

    // Fullness

    @Test
    public void testIsFullIsFalseWhenEmpty() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));

        // Act
        boolean result = cell.isFull();

        // Assert
        Assertions.assertFalse(result, "Empty cell should not be full.");
    }

    @Test
    public void testIsFullIsTrueAfterPlacement() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(unit, false);

        // Act
        boolean result = cell.isFull();

        // Assert
        Assertions.assertTrue(result, "Cell should be full after placing a unit.");
    }

    @Test
    public void testHasUnitIsFalseWhenEmpty() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));

        // Act
        boolean result = cell.hasUnit();

        // Assert
        Assertions.assertFalse(result, "Empty cell should not report a unit.");
    }

    @Test
    public void testHasUnitAfterPlacement() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        cell.tryPlaceUnit(unit, false);
        boolean result = cell.hasUnit();

        // Assert
        Assertions.assertTrue(result, "Cell should report having a unit after placement.");
    }

    @Test
    public void testHasUnitWithPredicateIsTrueWhenUnitMatches() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(unit, false);

        // Act
        boolean result = cell.hasUnit(contained -> contained == unit);

        // Assert
        Assertions.assertTrue(result, "Cell should match when its unit satisfies the predicate.");
    }

    @Test
    public void testHasUnitWithPredicateIsFalseWhenUnitDoesNotMatch() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(unit, false);

        // Act
        boolean result = cell.hasUnit(contained -> false);

        // Assert
        Assertions.assertFalse(result, "Cell should not match when its unit fails the predicate.");
    }

    @Test
    public void testHasUnitWithPredicateIsFalseWhenEmpty() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));

        // Act
        boolean result = cell.hasUnit(contained -> true);

        // Assert
        Assertions.assertFalse(result, "Empty cell cannot match a predicate.");
    }

    @Test
    public void testHasUnitThatCanAttackIsFalseWhenEmpty() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));

        // Act
        boolean result = cell.hasUnitThatCanAttack();

        // Assert
        Assertions.assertFalse(result, "Empty cell has no unit that can attack.");
    }

    @Test
    public void testHasUnitThatCanAttackIsTrueWithUnit() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(unit, false);

        // Act
        boolean result = cell.hasUnitThatCanAttack();

        // Assert
        Assertions.assertTrue(result, "Unit on ground should be able to attack.");
    }

    // Damage

    @Test
    public void testCellWithoutUnitCannotBeDamaged() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));

        // Act
        boolean result = cell.canDamage();

        // Assert
        Assertions.assertFalse(result, "Cell without a unit should not be damageable.");
    }

    @Test
    public void testCellWithUnitCanBeDamaged() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(unit, false);

        // Act
        boolean result = cell.canDamage();

        // Assert
        Assertions.assertTrue(result, "Cell with a unit should be damageable.");
    }

    @Test
    public void testCellDamagesUnitWhenDamaged() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(unit, false);

        // Act
        cell.tryDamage(2);

        // Assert
        Mockito.verify(unit).dealDamage(2);
    }

    // Contamination

    @Test
    public void testContaminateUnitContaminatesPresentUnit() {
        // Arrange
        Cell cell = new Cell(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        cell.tryPlaceUnit(unit, false);

        // Act
        cell.contaminateUnit();

        // Assert
        Mockito.verify(unit).contaminate();
    }

    // Movement

    @Test
    public void testMoveUnitMovesUnitToEmptyDestination() {
        // Arrange
        Cell source = new Cell(new Position(0, 0));
        Cell destination = new Cell(new Position(1, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMove(Mockito.anyBoolean(), Mockito.anyDouble())).thenReturn(true);
        source.tryPlaceUnit(unit, false);

        // Act
        boolean result = source.moveUnit(destination, false, 1.0);
        boolean sourceHasUnit = source.hasUnit();
        Unit destinationUnit = destination.peekUnit();

        // Assert
        Assertions.assertTrue(result, "Move should succeed.");
        Assertions.assertFalse(sourceHasUnit, "Source should be empty after the move.");
        Assertions.assertSame(unit, destinationUnit, "Destination should contain the moved unit.");
    }

    @Test
    public void testMoveUnitReducesMovementWhenNotPushed() {
        // Arrange
        Cell source = new Cell(new Position(0, 0));
        Cell destination = new Cell(new Position(1, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMove(Mockito.anyBoolean(), Mockito.anyDouble())).thenReturn(true);
        source.tryPlaceUnit(unit, false);

        // Act
        source.moveUnit(destination, false, 2.0);

        // Assert
        Mockito.verify(unit).reduceMovement(2.0);
    }

    @Test
    public void testMoveUnitDoesNotReduceMovementWhenPushed() {
        // Arrange
        Cell source = new Cell(new Position(0, 0));
        Cell destination = new Cell(new Position(1, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMove(Mockito.anyBoolean(), Mockito.anyDouble())).thenReturn(true);
        source.tryPlaceUnit(unit, false);

        // Act
        boolean result = source.moveUnit(destination, true, 2.0);
        Unit destinationUnit = destination.peekUnit();

        // Assert
        Assertions.assertTrue(result, "Pushed move should succeed.");
        Assertions.assertSame(unit, destinationUnit, "Pushed unit should arrive at destination.");
        Mockito.verify(unit, Mockito.never()).reduceMovement(Mockito.anyDouble());
    }

    @Test
    public void testMoveUnitFailsWhenSourceIsEmpty() {
        // Arrange
        Cell source = new Cell(new Position(0, 0));
        Cell destination = new Cell(new Position(1, 0));

        // Act
        boolean result = source.moveUnit(destination, false, 1.0);
        boolean destinationHasUnit = destination.hasUnit();

        // Assert
        Assertions.assertFalse(result, "Move without a unit should fail.");
        Assertions.assertFalse(destinationHasUnit, "Destination should remain empty.");
    }

    @Test
    public void testMoveUnitFailsWhenDestinationIsFull() {
        // Arrange
        Cell source = new Cell(new Position(0, 0));
        Cell destination = new Cell(new Position(1, 0));
        Unit mover = Mockito.mock(Unit.class);
        Unit occupant = Mockito.mock(Unit.class);
        Mockito.when(mover.canMove(Mockito.anyBoolean(), Mockito.anyDouble())).thenReturn(true);
        source.tryPlaceUnit(mover, false);
        destination.tryPlaceUnit(occupant, false);

        // Act
        boolean result = source.moveUnit(destination, false, 1.0);
        Unit sourceUnit = source.peekUnit();
        Unit destinationUnit = destination.peekUnit();

        // Assert
        Assertions.assertFalse(result, "Move into a full cell should fail.");
        Assertions.assertSame(mover, sourceUnit, "Moving unit should stay in the source.");
        Assertions.assertSame(occupant, destinationUnit, "Occupant should remain in the destination.");
        Mockito.verify(mover, Mockito.never()).reduceMovement(Mockito.anyDouble());
    }

    @Test
    public void testMoveUnitFailsWhenUnitCannotMove() {
        // Arrange
        Cell source = new Cell(new Position(0, 0));
        Cell destination = new Cell(new Position(1, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMove(Mockito.anyBoolean(), Mockito.anyDouble())).thenReturn(false);
        source.tryPlaceUnit(unit, false);

        // Act
        boolean result = source.moveUnit(destination, false, 1.0);
        Unit sourceUnit = source.peekUnit();
        boolean destinationHasUnit = destination.hasUnit();

        // Assert
        Assertions.assertFalse(result, "Move should fail when the unit cannot move.");
        Assertions.assertSame(unit, sourceUnit, "Unit should be returned to the source.");
        Assertions.assertFalse(destinationHasUnit, "Destination should remain empty.");
        Mockito.verify(unit, Mockito.never()).reduceMovement(Mockito.anyDouble());
    }

    @Test
    public void testMoveUnitFailsAndRestoresUnitWhenDestinationRejectsIt() {
        // Arrange
        Cell source = new Cell(new Position(0, 0));
        Cell destination = Mockito.mock(Cell.class);
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMove(Mockito.anyBoolean(), Mockito.anyDouble())).thenReturn(true);
        Mockito.when(destination.isFull()).thenReturn(false);
        Mockito.when(destination.tryPlaceUnit(unit, false)).thenReturn(false);
        source.tryPlaceUnit(unit, false);

        // Act
        boolean result = source.moveUnit(destination, false, 1.0);
        Unit sourceUnit = source.peekUnit();

        // Assert
        Assertions.assertFalse(result, "Move should fail when the destination rejects the unit.");
        Assertions.assertSame(unit, sourceUnit, "Unit should be returned to the source.");
        Mockito.verify(unit, Mockito.never()).reduceMovement(Mockito.anyDouble());
    }

    // Position

    @Test
    public void testHasPositionReturnsTrueForMatchingPosition() {
        // Arrange
        Cell cell = new Cell(new Position(1, 1));

        // Act
        boolean result = cell.hasPosition(new Position(1, 1));

        // Assert
        Assertions.assertTrue(result, "Cell should match its own position.");
    }

    @Test
    public void testHasPositionReturnsFalseForDifferentPosition() {
        // Arrange
        Cell cell = new Cell(new Position(1, 1));

        // Act
        boolean result = cell.hasPosition(new Position(2, 2));

        // Assert
        Assertions.assertFalse(result, "Cell should not match another position.");
    }
}