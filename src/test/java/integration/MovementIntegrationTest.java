package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.creation.CellFactory;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Mech;
import game.units.actions.UnitAction;
import game.units.movement.Grounded;
import game.utils.FileHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MovementIntegrationTest {

    private final Position mechPosition = new Position(1, 0);

    private final ElectricalNetwork mockNetwork = Mockito.mock(ElectricalNetwork.class);
    private GameMap gameMap;
    private Mech mech;

    @BeforeEach
    public void rebuildMap() throws PositionOutOfBoundsException {
        // Fresh map and mech before every test, so no state carries over.
        FileHandler fileHandler = FileHandler.getInstance();

        CellFactory cellFactory = new CellFactory(mockNetwork);
        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory);

        String mapPath = "src/test/resources/testMap1.txt";
        gameMap = new GameMap(mapImporter, mapPath);

        mech = new Mech(null, 10, new Grounded(2));
        gameMap.placeUnitAt(mech, mechPosition);
    }

    // Verificar que un Mech solo pueda moverse a casillas dentro de su rango de movimiento, sin atravesar obstáculos ni otras unidades.

    // Row 1
    @Test
    public void testMoveTo_0_0_Fails() throws PositionOutOfBoundsException {
        assertMove(new Position(0, 0), false);
    }

    @Test
    public void testMoveTo_2_0_Succeeds() throws PositionOutOfBoundsException {
        assertMove(new Position(2, 0), true);
    }

    @Test
    public void testMoveTo_3_0_Fails() throws PositionOutOfBoundsException {
        assertMove(new Position(3, 0), false);
    }

    // Row 2
    @Test
    public void testMoveTo_0_1_Fails() throws PositionOutOfBoundsException {
        assertMove(new Position(0, 1), false);
    }

    @Test
    public void testMoveTo_1_1_Succeeds() throws PositionOutOfBoundsException {
        assertMove(new Position(1, 1), true);
    }

    @Test
    public void testMoveTo_2_1_Succeeds() throws PositionOutOfBoundsException {
        assertMove(new Position(2, 1), true);
    }

    @Test
    public void testMoveTo_3_1_Fails() throws PositionOutOfBoundsException {
        assertMove(new Position(3, 1), false);
    }

    // Row 3
    @Test
    public void testMoveTo_0_2_Fails() throws PositionOutOfBoundsException {
        assertMove(new Position(0, 2), false);
    }

    @Test
    public void testMoveTo_1_2_Succeeds() throws PositionOutOfBoundsException {
        assertMove(new Position(1, 2), true);
    }

    @Test
    public void testMoveTo_2_2_Fails() throws PositionOutOfBoundsException {
        assertMove(new Position(2, 2), false);
    }

    @Test
    public void testMoveTo_3_2_Fails() throws PositionOutOfBoundsException {
        assertMove(new Position(3, 2), false);
    }

    // Row 4
    @Test
    public void testMoveTo_0_3_Fails() throws PositionOutOfBoundsException {
        assertMove(new Position(0, 3), false);
    }

    @Test
    public void testMoveTo_1_3_Fails() throws PositionOutOfBoundsException {
        assertMove(new Position(1, 3), false);
    }

    @Test
    public void testMoveTo_2_3_Fails() throws PositionOutOfBoundsException {
        assertMove(new Position(2, 3), false);
    }

    @Test
    public void testMoveTo_3_3_Fails() throws PositionOutOfBoundsException {
        assertMove(new Position(3, 3), false);
    }

    private void assertMove(Position destination, boolean shouldSucceed) throws PositionOutOfBoundsException {
        UnitAction moveAction = mech.getMovementAction(mechPosition, destination);
        boolean success = moveAction.tryExecute(gameMap);

        if (shouldSucceed) {
            Assertions.assertTrue(success, "Move action to " + destination + " should have succeeded but failed.");
        } else {
            Assertions.assertFalse(success, "Move action to " + destination + " should have failed but succeeded.");
        }
    }
}