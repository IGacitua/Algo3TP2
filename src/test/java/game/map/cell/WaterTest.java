package game.map.cell;

import game.position.Position;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class WaterTest {

    // Placement

    @Test
    public void testCanPlaceUnitThatCanMoveToWater() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);

        // Act
        boolean result = water.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertTrue(result, "Unit should be able to be placed.");
    }

    @Test
    public void testCannotPlaceUnitThatCannotMoveToWater() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(false);

        // Act
        boolean result = water.tryPlaceUnit(unit, false);
        boolean hasUnit = water.hasUnit();

        // Assert
        Assertions.assertFalse(result, "Unit that cannot move to water should not be placed.");
        Assertions.assertFalse(hasUnit, "Water should remain empty.");
    }

    @Test
    public void testCanForceLandUnitIntoWaterWhenPushed() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        boolean result = water.tryPlaceUnit(unit, true);

        // Assert
        Assertions.assertTrue(result, "Land unit should be able to be placed when pushed.");
    }

    @Test
    public void testCanPlaceUnitIsFalseWhenUnitCannotMoveToWater() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(false);

        // Act
        boolean result = water.canPlaceUnit(unit, false);

        // Assert
        Assertions.assertFalse(result, "Water should reject units that cannot move to water.");
    }

    // Drowning

    @Test
    public void testUnitThatDiesOnWaterIsKilledWhenPlaced() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(unit.diesOnWater()).thenReturn(true);

        // Act
        water.tryPlaceUnit(unit, false);

        // Assert
        Mockito.verify(unit).kill();
    }

    @Test
    public void testUnitThatDiesOnWaterIsKilledWhenPushed() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(unit.diesOnWater()).thenReturn(true);

        // Act
        water.tryPlaceUnit(unit, true);

        // Assert
        Mockito.verify(unit).kill();
    }

    @Test
    public void testUnitThatDoesNotDieOnWaterIsNotKilledWhenPlaced() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(unit.diesOnWater()).thenReturn(false);

        // Act
        water.tryPlaceUnit(unit, false);

        // Assert
        Mockito.verify(unit, Mockito.never()).kill();
    }

    @Test
    public void testRejectedUnitIsNotKilled() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(false);
        Mockito.when(unit.diesOnWater()).thenReturn(true);

        // Act
        water.tryPlaceUnit(unit, false);

        // Assert
        Mockito.verify(unit, Mockito.never()).kill();
    }

    // Attacking

    @Test
    public void testHasUnitThatCanAttackIsFalseWhenEmpty() {
        // Arrange
        Cell water = new Water(new Position(0, 0));

        // Act
        boolean result = water.hasUnitThatCanAttack();

        // Assert
        Assertions.assertFalse(result, "Empty water has no unit that can attack.");
    }

    @Test
    public void testHasUnitThatCanAttackIsFalseWhenUnitCannotAttackFromWater() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(unit.canAttackFromWater()).thenReturn(false);
        water.tryPlaceUnit(unit, false);

        // Act
        boolean result = water.hasUnitThatCanAttack();

        // Assert
        Assertions.assertFalse(result, "Unit in water that cannot attack from water should not attack.");
    }

    @Test
    public void testHasUnitThatCanAttackIsTrueWhenUnitCanAttackFromWater() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(unit.canAttackFromWater()).thenReturn(true);
        water.tryPlaceUnit(unit, false);

        // Act
        boolean result = water.hasUnitThatCanAttack();

        // Assert
        Assertions.assertTrue(result, "Unit that can attack from water should be able to attack.");
    }
}