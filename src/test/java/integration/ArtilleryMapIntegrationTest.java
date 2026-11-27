package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.cell.Building;
import game.map.creation.CellFactory;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Team;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.movement.Grounded;
import game.units.weapons.Artillery;
import game.units.weapons.Weapon;
import game.utils.FileHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArtilleryMapIntegrationTest {

    private static final String TEST_MAP_PATH = "src/test/resources/testMap2.txt";

    private ElectricalNetwork electricalNetwork;
    private GameMap map;
    private Weapon artillery;

    @BeforeEach
    public void setUp() throws InvalidMapFileException {
        electricalNetwork = new ElectricalNetwork(5);
        MapImporter importer = new MapImporter(FileHandler.getInstance(), new CellFactory(electricalNetwork));
        map = new GameMap(importer, TEST_MAP_PATH);
        artillery = new Artillery(map);
    }

    @Test
    public void artilleryDamagesTargetUnitAndPushesAdjacentUnitIntoFreeTile() throws PositionOutOfBoundsException {
        // Arrange
        Position mechaPosition = new Position(2, 4);
        Position targetPosition = new Position(2, 2);
        Position eastUnitPosition = new Position(3, 2);
        Position eastExpectedPosition = new Position(4, 2);

        Unit mecha = new Unit(Team.Player, artillery, 5, new Grounded(3));
        Unit targetVek = new Unit(Team.Enemy, null, 5, new Grounded(2));
        Unit eastVek = new Unit(Team.Enemy, null, 5, new Grounded(2));

        map.tryPlaceUnit(mecha, mechaPosition);
        map.tryPlaceUnit(targetVek, targetPosition);
        map.tryPlaceUnit(eastVek, eastUnitPosition);

        UnitAction action = mecha.getAttackAction(mechaPosition, targetPosition);

        // Act
        action.tryExecute(map);

        // Assert
        Assertions.assertTrue(targetVek.isDamaged(), "Expected target unit to take damage.");
        Assertions.assertTrue(map.hasUnitAt(targetPosition), "Expected target unit to stay in place.");
        Assertions.assertTrue(map.hasUnitAt(eastExpectedPosition), "Expected east unit to be pushed to (4, 2).");
        Assertions.assertFalse(map.hasUnitAt(eastUnitPosition), "Expected east unit to leave (3, 2).");
        Assertions.assertEquals(4, electricalNetwork.getPower(), "Expected building at (2, 0) on testMap2 to be destroyed by northern push.");
    }

    @Test
    public void artilleryPushIntoIntactBuildingDamagesUnitDestroysBuildingAndReducesPower() throws PositionOutOfBoundsException {
        // Arrange
        Position mechaPosition = new Position(2, 4);
        Position targetPosition = new Position(2, 2);
        Position northUnitPosition = new Position(2, 1);
        Position northBuildingPosition = new Position(2, 0);

        Unit mecha = new Unit(Team.Player, artillery, 5, new Grounded(3));
        Unit targetVek = new Unit(Team.Enemy, null, 5, new Grounded(2));
        Unit northVek = new Unit(Team.Enemy, null, 5, new Grounded(2));

        map.tryPlaceUnit(mecha, mechaPosition);
        map.tryPlaceUnit(targetVek, targetPosition);
        map.tryPlaceUnit(northVek, northUnitPosition);

        UnitAction action = mecha.getAttackAction(mechaPosition, targetPosition);

        // Act
        action.tryExecute(map);

        // Assert
        Assertions.assertTrue(map.hasUnitAt(northUnitPosition), "Expected north unit to remain at (2, 1) after collision.");
        Assertions.assertFalse(map.hasUnitAt(northBuildingPosition), "Expected north unit not to enter intact building at (2, 0).");
        Assertions.assertTrue(northVek.isDamaged(), "Expected north unit to take collision damage.");
        Assertions.assertTrue(((Building) map.getCellAt(northBuildingPosition)).isDestroyed(), "Expected building at (2, 0) to be destroyed.");
        Assertions.assertEquals(4, electricalNetwork.getPower(), "Expected electrical network to drop from 5 to 4.");
    }
}