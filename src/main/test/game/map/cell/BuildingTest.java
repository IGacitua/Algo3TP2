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
        GenericCell building = new Building(new Position2D(0, 0));
        Unit unit = Mockito.mock(Unit.class);

        // Act
        boolean result = building.tryPlaceUnit(unit, false);

        // Assert
        Assertions.assertTrue(result, "Unit should be able to be placed.");
    }

    @Test
    public void testCannotPlaceMultipleUnitsInBuildingCell() {
        // Arrange
        GenericCell building = new Building(new Position2D(0, 0));
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
        GenericCell building = new Building(position);
        Unit unit = Mockito.mock(Unit.class);

        // Act
        building.tryPlaceUnit(unit, false);

        // Assert
        Mockito.verify(unit).moveTo(position);
    }

    @Test
    public void testPopUnitReturnsCorrectUnit() {
        // Arrange
        GenericCell building = new Building(new Position2D(0, 0));
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
        GenericCell building = new Building(new Position2D(0, 0));
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
        GenericCell building = new Building(null);

        // Act
        boolean canDamage = building.canDamage();

        // Assert
        Assertions.assertTrue(canDamage, "Building cell should be able to be damaged.");
    }

    @Test
    public void testDestroyedBuildingWithoutUnitCannotBeDamaged() {
        // Arrange
        GenericCell building = new Building(new Position2D(0, 0));

        // Act
        building.tryDamage(); // This destroys the building
        boolean canDamage = building.canDamage();

        // Assert
        Assertions.assertFalse(canDamage, "Destroyed building cell should not be able to be damaged.");
    }

    @Test
    public void testBuildingWithUnitCanBeDamaged() {
        // Arrange
        Unit unit = Mockito.mock(Unit.class);
        GenericCell building = new Building(new Position2D(0, 0));

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
        GenericCell building = new Building(new Position2D(0, 0));
        building.tryPlaceUnit(unit, false);

        // Act
        building.tryDamage();

        // Assert
        Mockito.verify(unit).damage();
    }

    @Test
    public void testDamageBuildingDestroysIt() {
        // Arrange
        Building building = new Building(new Position2D(0, 0));

        // Act
        building.tryDamage(); // This should destroy the building

        // Assert
        Assertions.assertTrue(building.isDestroyed(), "Building cell should be destroyed.");
    }

    @Test // TODO: Change ElectricalNetwork to NOT Singleton so this test can be isolated.
    public void testDestroyingBuildingReducesElectricalNetwork() {
        // Arrange
        Building building = new Building(new Position2D(0, 0));
        ElectricalNetwork.getInstance().reset();

        // Act
        int initialPower = ElectricalNetwork.getInstance().getPower();
        building.tryDamage(); // This should destroy the building
        int finalPower = ElectricalNetwork.getInstance().getPower();

        // Assert
        Assertions.assertTrue(finalPower < initialPower, "Destroying the building should reduce power.");
    }

    @Test
    public void testDamagingDestroyedBuildingDoesNotReduceElectricalNetwork() {
        // Arrange
        Building building = new Building(new Position2D(0, 0));
        building.tryDamage(); // Destroy the building
        ElectricalNetwork.getInstance().reset();

        // Act
        int initialPower = ElectricalNetwork.getInstance().getPower();
        building.tryDamage(); // Try to damage again
        int finalPower = ElectricalNetwork.getInstance().getPower();

        // Assert
        Assertions.assertEquals(initialPower, finalPower, "Damaging a destroyed building should not reduce power.");
    }


    @Test
    public void testHasPositionReturnsTrueForMatchingPosition() {
        // Arrange
        Position2D position = new Position2D(1, 1);
        GenericCell cell = new Building(position);

        // Act
        boolean hasPosition = cell.hasPosition(new Position2D(1, 1));

        // Assert
        Assertions.assertTrue(hasPosition, "Building should return true for matching position.");
    }
}