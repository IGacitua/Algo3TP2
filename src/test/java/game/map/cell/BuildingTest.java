package game.map.cell;

import game.electricalNetwork.ElectricalNetwork;
import game.position.Position;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BuildingTest {

    private static ElectricalNetwork mockNetwork() {
        return Mockito.mock(ElectricalNetwork.class);
    }

    private Unit getGroundedUnit() {
        Unit unit = Mockito.mock(Unit.class);
        Mockito.when(unit.canMove(Mockito.anyBoolean(), Mockito.anyDouble())).thenReturn(true);
        return unit;
    }

    @Test
    public void testCannotPlaceOneUnitInBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), mockNetwork());
        Unit unit = getGroundedUnit();

        // Act
        boolean result = building.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertFalse(result, "Unit should be able to be placed.");
    }

    @Test
    public void testCannotPushUnitInIntactBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), mockNetwork());
        Unit unit = getGroundedUnit();

        // Act
        boolean result = building.tryPlaceUnit(unit, true);

        // Assert
        Assertions.assertFalse(result, "Unit should not be able to be pushed into an intact building.");
    }

    @Test
    public void testCanPushUnitIntoDestroyedBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), mockNetwork());
        Unit unit = getGroundedUnit();
        building.tryDamage(1);

        // Act
        boolean result = building.tryPlaceUnit(unit, true);

        // Assert
        Assertions.assertTrue(result, "Pushed unit should be able to move onto a destroyed building.");
    }

    @Test
    public void testCannotPushMultipleUnitsIntoDestroyedBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), mockNetwork());
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);
        building.tryDamage(1);

        // Act
        boolean mainResult = building.tryPlaceUnit(mainUnit, true);
        boolean secondaryResult = building.tryPlaceUnit(secondaryUnit, true);

        // Assert
        Assertions.assertTrue(mainResult, "Main unit should be placed successfully on destroyed building.");
        Assertions.assertFalse(secondaryResult, "Secondary unit should not be placed successfully.");
    }

    @Test
    public void testPopUnitReturnsCorrectUnitFromDestroyedBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), mockNetwork());
        Unit unit = getGroundedUnit();
        building.tryDamage(1);
        building.tryPlaceUnit(unit, true);

        // Act
        Unit poppedUnit = building.popUnit();

        // Assert
        Assertions.assertEquals(unit, poppedUnit, "Popped unit should be the same as the placed unit.");
    }

    @Test
    public void testPopUnitAllowsNewUnitPlacementInDestroyedBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), mockNetwork());
        Unit firstUnit = Mockito.mock(Unit.class);
        Unit secondUnit = Mockito.mock(Unit.class);
        building.tryDamage(1);

        // Act
        building.tryPlaceUnit(firstUnit, true);
        building.popUnit();
        boolean canPlaceSecondUnit = building.tryPlaceUnit(secondUnit, true);

        // Assert
        Assertions.assertTrue(canPlaceSecondUnit, "Should be able to place a new unit after popping the first one.");
    }

    @Test
    public void testNotDestroyedBuildingWithoutUnitCanBeDamaged() {
        // Arrange
        Cell building = new Building(new Position(0, 0), mockNetwork());

        // Act
        boolean canDamage = building.canDamage();

        // Assert
        Assertions.assertTrue(canDamage, "Building cell should be able to be damaged.");
    }

    @Test
    public void testDestroyedBuildingWithoutUnitCannotBeDamaged() {
        // Arrange
        Cell building = new Building(new Position(0, 0), mockNetwork());

        // Act
        building.tryDamage(1); // This destroys the building
        boolean canDamage = building.canDamage();

        // Assert
        Assertions.assertFalse(canDamage, "Destroyed building cell should not be able to be damaged.");
    }

    @Test
    public void testBuildingWithUnitCanBeDamaged() {
        // Arrange
        Unit unit = getGroundedUnit();
        Cell building = new Building(new Position(0, 0), mockNetwork());

        // Act
        building.tryPlaceUnit(unit, false);
        boolean canDamage = building.canDamage();

        // Assert
        Assertions.assertTrue(canDamage, "Building cell should be able to be damaged when a unit is present.");
    }

    @Test
    public void testBuildingDamagesUnitWhenDamaged() {
        // Arrange
        Unit unit = getGroundedUnit();
        Building building = new Building(new Position(0, 0), mockNetwork());
        building.tryDamage(1); // Destruye el edificio para que permita alojar la unidad empujada
        building.tryPlaceUnit(unit, true);

        // Act
        building.tryDamage(1);

        // Assert
        Mockito.verify(unit).dealDamage(1);
    }

    @Test
    public void testDamageBuildingDestroysIt() {
        // Arrange
        Building building = new Building(new Position(0, 0), mockNetwork());

        // Act
        building.tryDamage(1); // This should destroy the building

        // Assert
        Assertions.assertTrue(building.isDestroyed(), "Building cell should be destroyed.");
    }

    @Test
    public void testDestroyingBuildingReducesElectricalNetwork() {
        // Arrange
        ElectricalNetwork network = Mockito.mock(ElectricalNetwork.class);
        Building building = new Building(new Position(0, 0), network);

        // Act
        building.tryDamage(1);

        // Assert
        Mockito.verify(network).modifyPower(Building.DAMAGE_POWER_MODIFICATION);
    }

    @Test
    public void testDamagingDestroyedBuildingDoesNotReduceElectricalNetwork() {
        // Arrange
        ElectricalNetwork network = Mockito.mock(ElectricalNetwork.class);
        Building building = new Building(new Position(0, 0), network);
        building.tryDamage(1); // Destroy the building

        // Act
        building.tryDamage(1); // Try to damage again

        // Assert (Should only be called once)
        Mockito.verify(network, Mockito.times(1)).modifyPower(Building.DAMAGE_POWER_MODIFICATION);
    }

    @Test
    public void testHasPositionReturnsTrueForMatchingPosition() {
        // Arrange
        Position position = new Position(1, 1);
        Cell cell = new Building(position, mockNetwork());

        // Act
        boolean hasPosition = cell.hasPosition(new Position(1, 1));

        // Assert
        Assertions.assertTrue(hasPosition, "Building should return true for matching position.");
    }

    @Test
    public void testIntactBuildingIsVekTarget() {
        // Arrange
        Cell building = new Building(new Position(0, 0), mockNetwork());

        // Act
        boolean isVekTarget = building.isVekTarget();

        // Assert
        Assertions.assertTrue(isVekTarget, "Building should be a Vek target.");
    }

    @Test
    public void testDestroyedBuildingIsNotVekTarget() {
        // Arrange
        Cell building = new Building(new Position(0, 0), mockNetwork());
        building.tryDamage(1);

        // Act
        boolean isVekTarget = building.isVekTarget();

        // Assert
        Assertions.assertFalse(isVekTarget, "Destroyed building should not be a Vek target.");
    }

    @Test
    public void testBuildingHasUnitAfterPushing() {
        // Arrange
        Unit unit = getGroundedUnit();
        Building building = new Building(new Position(0, 0), mockNetwork());
        building.tryDamage(1);

        // Act
        building.tryPlaceUnit(unit, true);
        boolean hasUnit = building.hasUnit();

        // Assert
        Assertions.assertTrue(hasUnit, "Building should report having a unit after placement.");
    }
}