package game.map.cell;

import game.electricalNetwork.ElectricalNetwork;
import game.map.Position2D;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BuildingTest {

    @Test
    public void testCanPlaceOneUnitInBuildingCell() {
        // Arrange
        Cell building = new Building(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = building.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertTrue(result, "Unit should be able to be placed.");
    }

    @Test
    public void testCannotPlaceMultipleUnitsInBuildingCell() {
        // Arrange
        Cell building = new Building(new Position2D(0, 0));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);

        // Act
        boolean mainResult = building.tryPlaceUnit(mainUnit, false);
        boolean secondaryResult = building.tryPlaceUnit(secondaryUnit, false);

        // Assert
        Assertions.assertTrue(mainResult, "Main unit should be placed successfully.");
        Assertions.assertFalse(secondaryResult, "Secondary unit should not be placed successfully.");
    }

    @Test
    public void testPlacingUnitMovesItToPosition() {
        // Arrange
        Position2D position = new Position2D(0, 0);
        Cell building = new Building(position);
        Unit unit = Mockito.mock(Unit.class);

        // Act
        building.tryPlaceUnit(unit, false);

        // Assert
        Mockito.verify(unit).moveTo(position);
    }

    @Test
    public void testPopUnitReturnsCorrectUnit() {
        // Arrange
        Cell building = new Building(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);
        building.tryPlaceUnit(unit, false);

        // Act
        Unit poppedUnit = building.popUnit();

        // Assert
        Assertions.assertEquals(unit, poppedUnit, "Popped unit should be the same as the placed unit.");
    }

    @Test
    public void testPopUnitAllowsNewUnitPlacement() {
        // Arrange
        Cell building = new Building(new Position2D(0, 0));
        Unit firstUnit = Mockito.mock(Unit.class);
        Unit secondUnit = Mockito.mock(Unit.class);

        // Act
        building.tryPlaceUnit(firstUnit, false);
        building.popUnit();
        boolean canPlaceSecondUnit = building.tryPlaceUnit(secondUnit, false);

        // Assert
        Assertions.assertTrue(canPlaceSecondUnit, "Should be able to place a new unit after popping the first one.");
    }

    @Test
    public void testNotDestroyedBuildingWithoutUnitCanBeDamaged() {
        // Arrange
        Cell building = new Building(null);

        // Act
        boolean canDamage = building.canDamage();

        // Assert
        Assertions.assertTrue(canDamage, "Building cell should be able to be damaged.");
    }

    @Test
    public void testDestroyedBuildingWithoutUnitCannotBeDamaged() {
        // Arrange
        Cell building = new Building(new Position2D(0, 0));

        // Act
        building.tryDamage(1); // This destroys the building
        boolean canDamage = building.canDamage();

        // Assert
        Assertions.assertFalse(canDamage, "Destroyed building cell should not be able to be damaged.");
    }

    @Test
    public void testBuildingWithUnitCanBeDamaged() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        Cell building = new Building(new Position2D(0, 0));

        // Act
        building.tryPlaceUnit(unit, false);
        boolean canDamage = building.canDamage();

        // Assert
        Assertions.assertTrue(canDamage, "Building cell should be able to be damaged when a unit is present.");
    }

    @Test
    public void testBuildingDamagesUnitWhenDamaged() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        Cell building = new Building(new Position2D(0, 0));
        building.tryPlaceUnit(unit, false);

        // Act
        building.tryDamage(1);

        // Assert
        Mockito.verify(unit).damage(1);
    }

    @Test
    public void testDamageBuildingDestroysIt() {
        // Arrange
        Building building = new Building(new Position2D(0, 0));

        // Act
        building.tryDamage(1); // This should destroy the building

        // Assert
        Assertions.assertTrue(building.isDestroyed(), "Building cell should be destroyed.");
    }

    @Test
    public void testDestroyingBuildingReducesElectricalNetwork() {
        // Arrange
        Building building = new Building(new Position2D(0, 0));
        ElectricalNetwork.getInstance().reset();

        // Act
        int initialPower = ElectricalNetwork.getInstance().getPower();
        building.tryDamage(1); // This should destroy the building
        int finalPower = ElectricalNetwork.getInstance().getPower();

        // Assert
        Assertions.assertTrue(finalPower < initialPower, "Destroying the building should reduce power.");
    }

    @Test
    public void testDamagingDestroyedBuildingDoesNotReduceElectricalNetwork() {
        // Arrange
        Building building = new Building(new Position2D(0, 0));
        building.tryDamage(1); // Destroy the building
        ElectricalNetwork.getInstance().reset();

        // Act
        int initialPower = ElectricalNetwork.getInstance().getPower();
        building.tryDamage(1); // Try to damage again
        int finalPower = ElectricalNetwork.getInstance().getPower();

        // Assert
        Assertions.assertEquals(initialPower, finalPower, "Damaging a destroyed building should not reduce power.");
    }


    @Test
    public void testHasPositionReturnsTrueForMatchingPosition() {
        // Arrange
        Position2D position = new Position2D(1, 1);
        Cell cell = new Building(position);

        // Act
        boolean hasPosition = cell.hasPosition(new Position2D(1, 1));

        // Assert
        Assertions.assertTrue(hasPosition, "Building should return true for matching position.");
    }

    @Test
    public void testBuildingIsVekTarget() {
        // Arrange
        Cell building = new Building(new Position2D(0, 0));

        // Act
        boolean isVekTarget = building.isVekTarget();

        // Assert
        Assertions.assertTrue(isVekTarget, "Building should be a Vek target.");
    }

    @Test
    public void testDestroyedBuildingIsNotVekTarget() {
        // Arrange
        Cell building = new Building(new Position2D(0, 0));
        building.tryDamage(1);

        // Act
        boolean isVekTarget = building.isVekTarget();

        // Assert
        Assertions.assertFalse(isVekTarget, "Destroyed building should not be a Vek target.");
    }

    @Test
    public void testBuildingIsNotWater() {
        // Arrange
        Cell building = new Building(new Position2D(0, 0));

        // Act
        boolean isWater = building.isWater();

        // Assert
        Assertions.assertFalse(isWater, "Building should not be water.");
    }

    @Test
    public void testBuildingHasUnitAfterPlacement() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        Cell building = new Building(new Position2D(0, 0));

        // Act
        building.tryPlaceUnit(unit, false);
        boolean hasUnit = building.hasUnit();

        // Assert
        Assertions.assertTrue(hasUnit, "Building should report having a unit after placement.");
    }

    @Test
    public void testBuildingHasMechAfterPlacement() {
        // Arrange
        Unit mechUnit = Mockito.mock(Unit.class);
        Mockito.when(mechUnit.isMech()).thenReturn(true);
        Cell building = new Acid(new Position2D(0, 0));

        // Act
        building.tryPlaceUnit(mechUnit, false);
        boolean hasMech = building.hasMech();

        // Assert
        Assertions.assertTrue(hasMech, "Building should report having a mech after placement.");
    }
}