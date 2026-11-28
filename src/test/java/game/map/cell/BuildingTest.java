package game.map.cell;

import game.electricalNetwork.ElectricalNetwork;
import game.position.Position;
import game.units.Unit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BuildingTest {

    // Placement on intact building

    @Test
    public void testCannotPlaceUnitInIntactBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), Mockito.mock(ElectricalNetwork.class));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = building.tryPlaceUnit(unit, false);
        boolean hasUnit = building.hasUnit();

        // Assert
        Assertions.assertFalse(result, "Unit should not be able to be placed in an intact building.");
        Assertions.assertFalse(hasUnit, "Intact building should remain empty.");
    }

    @Test
    public void testCannotPushUnitIntoIntactBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), Mockito.mock(ElectricalNetwork.class));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = building.tryPlaceUnit(unit, true);
        boolean hasUnit = building.hasUnit();

        // Assert
        Assertions.assertFalse(result, "Unit should not be able to be pushed into an intact building.");
        Assertions.assertFalse(hasUnit, "Intact building should remain empty.");
    }

    // Damage / destruction

    @Test
    public void testIntactBuildingCanBeDamaged() {
        // Arrange
        Cell building = new Building(new Position(0, 0), Mockito.mock(ElectricalNetwork.class));

        // Act
        boolean result = building.canDamage();

        // Assert
        Assertions.assertTrue(result, "Intact building should be damageable.");
    }

    @Test
    public void testBuildingIsDamagedAfterDamaging() {
        // Arrange
        ElectricalNetwork network = Mockito.mock(ElectricalNetwork.class);
        Cell building = new Building(new Position(0, 0), network);

        // Act
        building.tryDamage(5);
        boolean canDamage = building.canDamage();

        // Assert
        Assertions.assertFalse(canDamage, "Building should be destroyed after any damage.");
        Mockito.verify(network).modifyPower(Building.DAMAGE_POWER_MODIFICATION);
    }

    @Test
    public void testDestroyingBuildingReducesElectricalNetworkPower() {
        // Arrange
        ElectricalNetwork network = Mockito.mock(ElectricalNetwork.class);
        Cell building = new Building(new Position(0, 0), network);

        // Act
        building.tryDamage(1);

        // Assert
        Mockito.verify(network).modifyPower(Building.DAMAGE_POWER_MODIFICATION);
    }

    @Test
    public void testDamagingDestroyedBuildingDoesNotReduceElectricalNetworkAgain() {
        // Arrange
        ElectricalNetwork network = Mockito.mock(ElectricalNetwork.class);
        Cell building = new Building(new Position(0, 0), network);
        building.tryDamage(1);

        // Act
        building.tryDamage(1);

        // Assert
        Mockito.verify(network, Mockito.times(1)).modifyPower(Building.DAMAGE_POWER_MODIFICATION);
    }

    @Test
    public void testDestroyedBuildingWithoutUnitCannotBeDamaged() {
        // Arrange
        Cell building = new Building(new Position(0, 0), Mockito.mock(ElectricalNetwork.class));
        building.tryDamage(1);

        // Act
        boolean result = building.canDamage();

        // Assert
        Assertions.assertFalse(result, "Destroyed building without a unit should not be damageable.");
    }

    @Test
    public void testDestroyedBuildingWithUnitCanBeDamaged() {
        // Arrange
        Cell building = new Building(new Position(0, 0), Mockito.mock(ElectricalNetwork.class));
        Unit unit = Mockito.mock(Unit.class);
        building.tryDamage(1);
        building.tryPlaceUnit(unit, true);

        // Act
        boolean result = building.canDamage();

        // Assert
        Assertions.assertTrue(result, "Destroyed building with a unit should be damageable.");
    }

    @Test
    public void testDamagingDestroyedBuildingDamagesUnitInside() {
        // Arrange
        ElectricalNetwork network = Mockito.mock(ElectricalNetwork.class);
        Cell building = new Building(new Position(0, 0), network);
        Unit unit = Mockito.mock(Unit.class);
        building.tryDamage(1);
        building.tryPlaceUnit(unit, true);

        // Act
        building.tryDamage(1);

        // Assert
        Mockito.verify(unit).dealDamage(1);
        Mockito.verify(network, Mockito.times(1)).modifyPower(Building.DAMAGE_POWER_MODIFICATION);
    }

    // Destroyed building behaves like ground

    @Test
    public void testCanPushUnitIntoDestroyedBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), Mockito.mock(ElectricalNetwork.class));
        Unit unit = Mockito.mock(Unit.class);
        building.tryDamage(1);

        // Act
        boolean result = building.tryPlaceUnit(unit, true);

        // Assert
        Assertions.assertTrue(result, "Pushed unit should be able to move onto a destroyed building.");
    }

    @Test
    public void testCanPlaceUnitInDestroyedBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), Mockito.mock(ElectricalNetwork.class));
        Unit unit = Mockito.mock(Unit.class);
        building.tryDamage(1);

        // Act
        boolean result = building.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertTrue(result, "Unit should be able to walk onto a destroyed building.");
    }

    @Test
    public void testCannotPlaceMultipleUnitsInDestroyedBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), Mockito.mock(ElectricalNetwork.class));
        Unit mainUnit = Mockito.mock(Unit.class);
        Unit secondaryUnit = Mockito.mock(Unit.class);
        building.tryDamage(1);

        // Act
        boolean mainResult = building.tryPlaceUnit(mainUnit, true);
        boolean secondaryResult = building.tryPlaceUnit(secondaryUnit, true);

        // Assert
        Unit remainingUnit = building.peekUnit();
        Assertions.assertTrue(mainResult, "Main unit should be placed successfully on destroyed building.");
        Assertions.assertFalse(secondaryResult, "Secondary unit should not be placed successfully.");
        Assertions.assertSame(mainUnit, remainingUnit, "Main unit should remain in the building.");
    }

    @Test
    public void testPlacingUnitInDestroyedBuildingDoesNotReduceElectricalNetworkAgain() {
        // Arrange
        ElectricalNetwork network = Mockito.mock(ElectricalNetwork.class);
        Cell building = new Building(new Position(0, 0), network);
        Unit unit = Mockito.mock(Unit.class);
        building.tryDamage(1);

        // Act
        building.tryPlaceUnit(unit, true);

        // Assert
        Mockito.verify(network, Mockito.times(1)).modifyPower(Building.DAMAGE_POWER_MODIFICATION);
    }

    @Test
    public void testPopUnitReturnsCorrectUnitFromDestroyedBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), Mockito.mock(ElectricalNetwork.class));
        Unit unit = Mockito.mock(Unit.class);
        building.tryDamage(1);
        building.tryPlaceUnit(unit, true);

        // Act
        Unit poppedUnit = building.popUnit();
        boolean hasUnit = building.hasUnit();

        // Assert
        Assertions.assertSame(unit, poppedUnit, "Popped unit should be the same as the placed unit.");
        Assertions.assertFalse(hasUnit, "Building should be empty after popping.");
    }

    @Test
    public void testPopUnitAllowsNewUnitPlacementInDestroyedBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), Mockito.mock(ElectricalNetwork.class));
        Unit firstUnit = Mockito.mock(Unit.class);
        Unit secondUnit = Mockito.mock(Unit.class);
        building.tryDamage(1);
        building.tryPlaceUnit(firstUnit, true);

        // Act
        building.popUnit();
        boolean result = building.tryPlaceUnit(secondUnit, true);

        // Assert
        Assertions.assertTrue(result, "Should be able to place a new unit after popping the first one.");
    }

    @Test
    public void testHasUnitAfterPlacementInDestroyedBuilding() {
        // Arrange
        Cell building = new Building(new Position(0, 0), Mockito.mock(ElectricalNetwork.class));
        Unit unit = Mockito.mock(Unit.class);
        building.tryDamage(1);

        // Act
        building.tryPlaceUnit(unit, true);
        boolean result = building.hasUnit();

        // Assert
        Assertions.assertTrue(result, "Building should report having a unit after placement.");
    }
}