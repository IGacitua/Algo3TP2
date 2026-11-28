package game.map.cell;

import game.position.Position;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AcidTest {

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
}