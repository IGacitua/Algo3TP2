package integration;

import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.map.creation.CellFactory;
import game.map.creation.MapImporter;
import game.units.Mech;
import game.units.actions.UnitAction;
import game.units.movement.Grounded;
import game.utils.FileHandler;
import org.junit.Assert;
import org.junit.Test;

public class MovementIntegrationTest {

    private static void rebuildMap() {
        FileHandler fileHandler = FileHandler.getInstance();
        CellFactory cellFactory = new CellFactory();
        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory);
        GameMap map = GameMap.getInstance();
        try {
            String mapPath = "src/test/resources/testMap1.txt";
            map.initialize(mapImporter, mapPath);
        } catch (Exception e) {
            Assert.fail("Map failed to load: " + e.getMessage());
        }
    }

    private static Mech buildMech() {
        return new Mech(new Position2D(1, 0), null, 10, new Grounded(2));
    }

    // Verificar que un Mech solo pueda moverse a casillas dentro de su rango de movimiento,
    // sin atravesar obstáculos ni otras unidades.
    @Test
    public void testMovementWithinRange() {
        // Row 1
        tryMoveUnit(new Position2D(0, 0), false);
        tryMoveUnit(new Position2D(2, 0), true);
        tryMoveUnit(new Position2D(3, 0), false);

        // Row 2
        tryMoveUnit(new Position2D(0, 1), false);
        tryMoveUnit(new Position2D(1, 1), true);
        tryMoveUnit(new Position2D(2, 1), true);
        tryMoveUnit(new Position2D(3, 1), false);

        // Row 3
        tryMoveUnit(new Position2D(0, 2), false);
        tryMoveUnit(new Position2D(1, 2), true);
        tryMoveUnit(new Position2D(2, 2), false);
        tryMoveUnit(new Position2D(3, 2), false);

        // Row 4
        tryMoveUnit(new Position2D(0, 3), false);
        tryMoveUnit(new Position2D(1, 3), false);
        tryMoveUnit(new Position2D(2, 3), false);
        tryMoveUnit(new Position2D(3, 3), false);
    }

    private void tryMoveUnit(Position2D position, boolean shouldSucceed) {
        // Fresh map and mech for every attempt, so no state carries over.
        rebuildMap();
        Mech mech = buildMech();
        UnitAction moveAction = mech.getMove(position);

        try {
            boolean success = moveAction.tryExecute(GameMap.getInstance());
            if (success && !shouldSucceed) {
                Assert.fail("Move action to " + position + " should have failed but succeeded.");
            } else if (!success && shouldSucceed) {
                Assert.fail("Move action to " + position + " should have succeeded but failed.");
            }
        } catch (PositionOutOfBoundsException e) {
            Assert.fail("Move action to " + position + " threw PositionOutOfBoundsException: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Move action to " + position + " failed to execute: " + e.getMessage());
        }
    }
}