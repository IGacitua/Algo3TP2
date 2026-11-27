package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.creation.CellFactory;
import game.map.creation.MapImporter;
import game.position.Position;
import game.units.Team;
import game.units.Unit;
import game.units.UnitFactory;
import game.units.actions.UnitAction;
import game.units.movement.Grounded;
import game.utils.FileHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MovementIntegrationTest {

    private final Position startingPosition = new Position(3, 3);

    private final ElectricalNetwork dummyNetwork = Mockito.mock(ElectricalNetwork.class);
    private GameMap map;
    private Unit unit;

    @BeforeEach
    public void rebuildMap() {
        FileHandler fileHandler = FileHandler.getInstance();

        CellFactory cellFactory = new CellFactory(dummyNetwork);
        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory, Mockito.mock(UnitFactory.class));

        String mapPath = "src/test/resources/testMap1.txt";
        map = new GameMap(mapImporter, mapPath);

        unit = new Unit(Team.Player, null, 10, new Grounded(3));
        map.tryPlaceUnit(unit, startingPosition);
    }

    // Row 0: nothing is reachable. (3,0) is at distance 3 but blocked by the obstacle at (3,1).
    @Test
    public void testMoveTo_0_0_Fails() {
        assertMove(new Position(0, 0), false);
    }

    @Test
    public void testMoveTo_1_0_Fails() {
        assertMove(new Position(1, 0), false);
    }

    @Test
    public void testMoveTo_2_0_Fails() {
        assertMove(new Position(2, 0), false);
    }

    @Test
    public void testMoveTo_3_0_Fails() {
        assertMove(new Position(3, 0), false);
    }

    @Test
    public void testMoveTo_4_0_Fails() {
        assertMove(new Position(4, 0), false);
    }

    @Test
    public void testMoveTo_5_0_Fails() {
        assertMove(new Position(5, 0), false);
    }

    @Test
    public void testMoveTo_6_0_Fails() {
        assertMove(new Position(6, 0), false);
    }

    // Row 1
    @Test
    public void testMoveTo_0_1_Fails() {
        assertMove(new Position(0, 1), false);
    }

    @Test
    public void testMoveTo_1_1_Fails() {
        assertMove(new Position(1, 1), false);
    }

    @Test
    public void testMoveTo_2_1_Fails() {
        // Distance 3, but both approaches are blocked by obstacles at (3,1) and (2,2).
        assertMove(new Position(2, 1), false);
    }

    @Test
    public void testMoveTo_3_1_Fails() {
        // Obstacle.
        assertMove(new Position(3, 1), false);
    }

    @Test
    public void testMoveTo_4_1_Succeeds() {
        assertMove(new Position(4, 1), true);
    }

    @Test
    public void testMoveTo_5_1_Fails() {
        assertMove(new Position(5, 1), false);
    }

    @Test
    public void testMoveTo_6_1_Fails() {
        assertMove(new Position(6, 1), false);
    }

    // Row 2
    @Test
    public void testMoveTo_0_2_Fails() {
        assertMove(new Position(0, 2), false);
    }

    @Test
    public void testMoveTo_1_2_Succeeds() {
        assertMove(new Position(1, 2), true);
    }

    @Test
    public void testMoveTo_2_2_Fails() {
        // Obstacle.
        assertMove(new Position(2, 2), false);
    }

    @Test
    public void testMoveTo_3_2_Succeeds() {
        assertMove(new Position(3, 2), true);
    }

    @Test
    public void testMoveTo_4_2_Succeeds() {
        assertMove(new Position(4, 2), true);
    }

    @Test
    public void testMoveTo_5_2_Succeeds() {
        assertMove(new Position(5, 2), true);
    }

    @Test
    public void testMoveTo_6_2_Fails() {
        assertMove(new Position(6, 2), false);
    }

    // Row 3 (unit starts at (3,3))
    @Test
    public void testMoveTo_0_3_Succeeds() {
        assertMove(new Position(0, 3), true);
    }

    @Test
    public void testMoveTo_1_3_Succeeds() {
        assertMove(new Position(1, 3), true);
    }

    @Test
    public void testMoveTo_2_3_Succeeds() {
        assertMove(new Position(2, 3), true);
    }

    @Test
    public void testMoveTo_4_3_Succeeds() {
        assertMove(new Position(4, 3), true);
    }

    @Test
    public void testMoveTo_5_3_Fails() {
        // Obstacle.
        assertMove(new Position(5, 3), false);
    }

    @Test
    public void testMoveTo_6_3_Fails() {
        // Distance 3, but the straight path is blocked by the obstacle at (5,3).
        assertMove(new Position(6, 3), false);
    }

    // Row 4
    @Test
    public void testMoveTo_0_4_Fails() {
        assertMove(new Position(0, 4), false);
    }

    @Test
    public void testMoveTo_1_4_Fails() {
        // Obstacle.
        assertMove(new Position(1, 4), false);
    }

    @Test
    public void testMoveTo_2_4_Succeeds() {
        assertMove(new Position(2, 4), true);
    }

    @Test
    public void testMoveTo_3_4_Succeeds() {
        assertMove(new Position(3, 4), true);
    }

    @Test
    public void testMoveTo_4_4_Succeeds() {
        assertMove(new Position(4, 4), true);
    }

    @Test
    public void testMoveTo_5_4_Succeeds() {
        assertMove(new Position(5, 4), true);
    }

    @Test
    public void testMoveTo_6_4_Fails() {
        assertMove(new Position(6, 4), false);
    }

    // Row 5
    @Test
    public void testMoveTo_0_5_Fails() {
        assertMove(new Position(0, 5), false);
    }

    @Test
    public void testMoveTo_1_5_Fails() {
        assertMove(new Position(1, 5), false);
    }

    @Test
    public void testMoveTo_2_5_Succeeds() {
        assertMove(new Position(2, 5), true);
    }

    @Test
    public void testMoveTo_3_5_Succeeds() {
        assertMove(new Position(3, 5), true);
    }

    @Test
    public void testMoveTo_4_5_Fails() {
        // Obstacle.
        assertMove(new Position(4, 5), false);
    }

    @Test
    public void testMoveTo_5_5_Fails() {
        assertMove(new Position(5, 5), false);
    }

    @Test
    public void testMoveTo_6_5_Fails() {
        assertMove(new Position(6, 5), false);
    }

    // Row 6
    @Test
    public void testMoveTo_0_6_Fails() {
        assertMove(new Position(0, 6), false);
    }

    @Test
    public void testMoveTo_1_6_Fails() {
        assertMove(new Position(1, 6), false);
    }

    @Test
    public void testMoveTo_2_6_Fails() {
        assertMove(new Position(2, 6), false);
    }

    @Test
    public void testMoveTo_3_6_Succeeds() {
        assertMove(new Position(3, 6), true);
    }

    @Test
    public void testMoveTo_4_6_Fails() {
        assertMove(new Position(4, 6), false);
    }

    @Test
    public void testMoveTo_5_6_Fails() {
        assertMove(new Position(5, 6), false);
    }

    @Test
    public void testMoveTo_6_6_Fails() {
        assertMove(new Position(6, 6), false);
    }

    private void assertMove(Position destination, boolean shouldSucceed) {
        UnitAction moveAction = unit.getMovementAction(startingPosition, destination);
        boolean success = moveAction.tryExecute(map);

        if (shouldSucceed) {
            Assertions.assertTrue(success, "Move action to " + destination + " should have succeeded but failed.");
        } else {
            Assertions.assertFalse(success, "Move action to " + destination + " should have failed but succeeded.");
        }
    }
}