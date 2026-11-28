package game.map.cell;

import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UnitSlotTest {

    // Placement

    @Test
    public void testTryPlaceSucceedsWhenSlotHasSpace() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = slot.tryPlace(unit);

        // Assert
        Assertions.assertTrue(result, "Unit should be placed when there is space.");
    }

    @Test
    public void testTryPlaceFailsWhenSlotIsFull() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);
        slot.tryPlace(mainUnit);

        // Act
        boolean result = slot.tryPlace(secondaryUnit);
        Unit remainingUnit = slot.peekUnit();

        // Assert
        Assertions.assertFalse(result, "Unit should not be placed when the slot is full.");
        Assertions.assertSame(mainUnit, remainingUnit, "The original unit should remain in the slot.");
    }

    @Test
    public void testTryPlaceFailsWhenCapacityIsZero() {
        // Arrange
        UnitSlot slot = new UnitSlot(0);
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = slot.tryPlace(unit);
        boolean hasUnit = slot.hasUnit();

        // Assert
        Assertions.assertFalse(result, "No unit can be placed in a zero-capacity slot.");
        Assertions.assertFalse(hasUnit, "Slot should remain empty.");
    }

    // Peek

    @Test
    public void testPeekUnitReturnsNullWhenEmpty() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);

        // Act
        Unit result = slot.peekUnit();

        // Assert
        Assertions.assertNull(result, "Peeking an empty slot should return null.");
    }

    @Test
    public void testPeekUnitReturnsPlacedUnitWithoutRemovingIt() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);
        Unit unit = Mockito.mock(Unit.class);
        slot.tryPlace(unit);

        // Act
        Unit firstPeek = slot.peekUnit();
        Unit secondPeek = slot.peekUnit();
        boolean hasUnit = slot.hasUnit();

        // Assert
        Assertions.assertSame(unit, firstPeek, "Peek should return the placed unit.");
        Assertions.assertSame(unit, secondPeek, "Peek should not remove the unit.");
        Assertions.assertTrue(hasUnit, "Slot should still contain the unit.");
    }

    // Pop

    @Test
    public void testPopUnitReturnsNullWhenEmpty() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);

        // Act
        Unit result = slot.popUnit();

        // Assert
        Assertions.assertNull(result, "Popping an empty slot should return null.");
    }

    @Test
    public void testPopUnitReturnsPlacedUnitAndRemovesIt() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);
        Unit unit = Mockito.mock(Unit.class);
        slot.tryPlace(unit);

        // Act
        Unit poppedUnit = slot.popUnit();
        boolean hasUnit = slot.hasUnit();

        // Assert
        Assertions.assertSame(unit, poppedUnit, "Popped unit should be the placed unit.");
        Assertions.assertFalse(hasUnit, "Slot should be empty after popping.");
    }

    @Test
    public void testPopUnitFreesSpaceForNewUnit() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);
        Unit firstUnit = Mockito.mock(Unit.class);
        Unit secondUnit = Mockito.mock(Unit.class);
        slot.tryPlace(firstUnit);

        // Act
        slot.popUnit();
        boolean result = slot.tryPlace(secondUnit);

        // Assert
        Assertions.assertTrue(result, "A new unit should be placeable after popping.");
    }

    // Fullness

    @Test
    public void testIsFullIsFalseWhenEmpty() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);

        // Act
        boolean result = slot.isFull();

        // Assert
        Assertions.assertFalse(result, "Empty slot with capacity should not be full.");
    }

    @Test
    public void testIsFullIsTrueWhenAtCapacity() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);
        Unit unit = Mockito.mock(Unit.class);
        slot.tryPlace(unit);

        // Act
        boolean result = slot.isFull();

        // Assert
        Assertions.assertTrue(result, "Slot at capacity should be full.");
    }

    @Test
    public void testIsFullIsTrueWhenCapacityIsZero() {
        // Arrange
        UnitSlot slot = new UnitSlot(0);

        // Act
        boolean result = slot.isFull();

        // Assert
        Assertions.assertTrue(result, "Zero-capacity slot should always be full.");
    }

    // Unit queries

    @Test
    public void testHasUnitIsFalseWhenEmpty() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);

        // Act
        boolean result = slot.hasUnit();

        // Assert
        Assertions.assertFalse(result, "Empty slot should not have a unit.");
    }

    @Test
    public void testHasUnitIsTrueAfterPlacement() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);
        Unit unit = Mockito.mock(Unit.class);

        // Act
        slot.tryPlace(unit);
        boolean result = slot.hasUnit();

        // Assert
        Assertions.assertTrue(result, "Slot should have a unit after placement.");
    }

    @Test
    public void testHasUnitWithPredicateIsFalseWhenEmpty() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);

        // Act
        boolean result = slot.hasUnit(unit -> true);

        // Assert
        Assertions.assertFalse(result, "Empty slot cannot match any predicate.");
    }

    @Test
    public void testHasUnitWithPredicateIsTrueWhenUnitMatches() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canAttackFromWater()).thenReturn(true);
        slot.tryPlace(unit);

        // Act
        boolean result = slot.hasUnit(Unit::canAttackFromWater);

        // Assert
        Assertions.assertTrue(result, "Slot should match when a unit satisfies the predicate.");
    }

    @Test
    public void testHasUnitWithPredicateIsFalseWhenNoUnitMatches() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canAttackFromWater()).thenReturn(false);
        slot.tryPlace(unit);

        // Act
        boolean result = slot.hasUnit(Unit::canAttackFromWater);

        // Assert
        Assertions.assertFalse(result, "Slot should not match when no unit satisfies the predicate.");
    }

    // Damage

    @Test
    public void testDamageDealsDamageToContainedUnit() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);
        Unit unit = Mockito.mock(Unit.class);
        slot.tryPlace(unit);

        // Act
        slot.damage(3);

        // Assert
        Mockito.verify(unit).dealDamage(3);
    }

    @Test
    public void testDamageDoesNotAffectRemovedUnits() {
        // Arrange
        UnitSlot slot = new UnitSlot(1);
        Unit unit = Mockito.mock(Unit.class);
        slot.tryPlace(unit);
        slot.popUnit();

        // Act
        slot.damage(1);

        // Assert
        Mockito.verify(unit, Mockito.never()).dealDamage(Mockito.anyInt());
    }
}