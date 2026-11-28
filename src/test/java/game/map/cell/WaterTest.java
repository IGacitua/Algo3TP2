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
    public void testCannotPlaceMultipleUnits() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);
        Mockito.when(mainUnit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(secondaryUnit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);

        // Act
        boolean mainResult = water.tryPlaceUnit(mainUnit, false);
        boolean secondaryResult = water.tryPlaceUnit(secondaryUnit, false);

        // Assert
        Unit remainingUnit = water.peekUnit();
        Assertions.assertTrue(mainResult, "Main unit should be placed successfully.");
        Assertions.assertFalse(secondaryResult, "Secondary unit should not be placed successfully.");
        Assertions.assertSame(mainUnit, remainingUnit, "Main unit should remain in the cell.");
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

    @Test
    public void testCanPlaceUnitIsFalseWhenFull() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);
        Mockito.when(mainUnit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(secondaryUnit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        water.tryPlaceUnit(mainUnit, false);

        // Act
        boolean result = water.canPlaceUnit(secondaryUnit, false);

        // Assert
        Assertions.assertFalse(result, "Full water cell should reject further units.");
    }

    // Peek / pop

    @Test
    public void testPopUnitReturnsCorrectUnit() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        water.tryPlaceUnit(unit, false);

        // Act
        Unit poppedUnit = water.popUnit();
        boolean hasUnit = water.hasUnit();

        // Assert
        Assertions.assertSame(unit, poppedUnit, "Popped unit should be the same as the placed unit.");
        Assertions.assertFalse(hasUnit, "Water should be empty after popping.");
    }

    @Test
    public void testPopUnitAllowsNewUnitPlacement() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit firstUnit = Mockito.mock(Unit.class);
        Unit secondUnit = Mockito.mock(Unit.class);
        Mockito.when(firstUnit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(secondUnit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        water.tryPlaceUnit(firstUnit, false);

        // Act
        water.popUnit();
        boolean result = water.tryPlaceUnit(secondUnit, false);

        // Assert
        Assertions.assertTrue(result, "Should be able to place a new unit after popping the first one.");
    }

    @Test
    public void testHasUnitAfterPlacement() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);

        // Act
        water.tryPlaceUnit(unit, false);
        boolean result = water.hasUnit();

        // Assert
        Assertions.assertTrue(result, "Water should report having a unit after placement.");
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

    // Damage

    @Test
    public void testWaterWithoutUnitCannotBeDamaged() {
        // Arrange
        Cell water = new Water(new Position(0, 0));

        // Act
        boolean result = water.canDamage();

        // Assert
        Assertions.assertFalse(result, "Water without a unit should not be damageable.");
    }

    @Test
    public void testWaterWithUnitCanBeDamaged() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        water.tryPlaceUnit(unit, false);

        // Act
        boolean result = water.canDamage();

        // Assert
        Assertions.assertTrue(result, "Water with a unit should be damageable.");
    }

    @Test
    public void testWaterDamagesUnitWhenDamaged() {
        // Arrange
        Cell water = new Water(new Position(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMoveToWater(Mockito.anyBoolean())).thenReturn(true);
        water.tryPlaceUnit(unit, false);

        // Act
        water.tryDamage(1);

        // Assert
        Mockito.verify(unit).dealDamage(1);
    }
}