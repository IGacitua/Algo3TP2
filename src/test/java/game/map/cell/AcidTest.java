package game.map.cell;

import game.position.Position;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AcidTest {

    // Placement

    @Test
    public void testCanPlaceOneUnit() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = acid.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertTrue(result, "Unit should be able to be placed.");
    }

    @Test
    public void testCanPushOneUnit() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = acid.tryPlaceUnit(unit, true);

        // Assert
        Assertions.assertTrue(result, "Pushed unit should be able to be placed.");
    }

    @Test
    public void testCannotPlaceMultipleUnits() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);

        // Act
        boolean mainResult = acid.tryPlaceUnit(mainUnit, false);
        boolean secondaryResult = acid.tryPlaceUnit(secondaryUnit, false);

        // Assert
        Unit remainingUnit = acid.peekUnit();
        Assertions.assertTrue(mainResult, "Main unit should be placed successfully.");
        Assertions.assertFalse(secondaryResult, "Secondary unit should not be placed successfully.");
        Assertions.assertSame(mainUnit, remainingUnit, "Main unit should remain in the cell.");
    }

    @Test
    public void testPopUnitReturnsCorrectUnit() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        acid.tryPlaceUnit(unit, false);

        // Act
        Unit poppedUnit = acid.popUnit();

        // Assert
        Assertions.assertSame(unit, poppedUnit, "Popped unit should be the same as the placed unit.");
        Assertions.assertFalse(acid.hasUnit(), "Acid should be empty after popping.");
    }

    @Test
    public void testPopUnitAllowsNewUnitPlacement() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        acid.tryPlaceUnit(unit, false);

        // Act
        acid.popUnit();
        boolean result = acid.tryPlaceUnit(Mockito.mock(Unit.class), false);

        // Assert
        Assertions.assertTrue(result, "Should be able to place a new unit after popping the first one.");
    }

    @Test
    public void testHasUnitAfterPlacement() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        acid.tryPlaceUnit(unit, false);
        boolean result = acid.hasUnit();

        // Assert
        Assertions.assertTrue(result, "Acid should report having a unit after placement.");
    }

    // Contamination
    @Test
    public void testContaminatesUnitOnPlacement() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        acid.tryPlaceUnit(unit, false);

        // Assert
        Mockito.verify(unit).contaminate();
    }

    @Test
    public void testContaminatesUnitWhenPushedIn() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        acid.tryPlaceUnit(unit, true);

        // Assert
        Mockito.verify(unit).contaminate();
    }

    @Test
    public void testContaminatesUnitWhenMovingIn() {
        // Arrange
        Cell source = new Cell(new Position(0, 0));
        Cell acid = new Acid(new Position(0, 0));

        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMove(Mockito.anyBoolean(), Mockito.anyDouble())).thenReturn(true);

        source.tryPlaceUnit(unit, false);

        // Act
        boolean result = source.moveUnit(acid, false, 1.0);

        // Assert
        Assertions.assertTrue(result, "Unit should be able to move into acid.");
        Mockito.verify(unit).contaminate();
    }

    @Test
    public void testDoesNotContaminateRejectedUnit() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit rejectedUnit = Mockito.mock(Unit.class);
        acid.tryPlaceUnit(mainUnit, false);

        // Act
        boolean result = acid.tryPlaceUnit(rejectedUnit, false);

        // Assert
        Assertions.assertFalse(result, "Second unit should be rejected.");
        Mockito.verify(rejectedUnit, Mockito.never()).contaminate();
    }


    // Damage

    @Test
    public void testAcidWithoutUnitCannotBeDamaged() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));

        // Act
        boolean result = acid.canDamage();

        // Act & Assert
        Assertions.assertFalse(result, "Acid without a unit should not be damageable.");
    }

    @Test
    public void testAcidWithUnitCanBeDamaged() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        acid.tryPlaceUnit(unit, false);

        // Act
        boolean result = acid.canDamage();

        // Assert
        Assertions.assertTrue(result, "Acid with a unit should be damageable.");
    }

    @Test
    public void testAcidDamagesUnitWhenDamaged() {
        // Arrange
        Cell acid = new Acid(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        acid.tryPlaceUnit(unit, false);

        // Act
        acid.tryDamage(1);

        // Assert
        Mockito.verify(unit).dealDamage(1);
    }

}
