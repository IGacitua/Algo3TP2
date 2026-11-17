package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.position.Position2D;
import game.position.PositionOutOfBoundsException;
import game.map.cell.Building;
import game.map.cell.Cell;
import game.map.cell.Mountain;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.direction.East;
import game.position.direction.North;
import game.units.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PushCollisionIntegrationTest {

    private Building intactBuilding;
    private Building destroyedBuilding;

    @BeforeEach
    public void setUp() throws InvalidMapFileException {
        ElectricalNetwork.getInstance().reset();

        MapImporter mockMapImporter = Mockito.mock(MapImporter.class);
        Cell[][] mockCells = new Cell[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                mockCells[x][y] = new Cell(new Position2D(x, y));
            }
        }

        intactBuilding = new Building(new Position2D(3, 2));
        destroyedBuilding = new Building(new Position2D(4, 2));
        destroyedBuilding.tryDamage(1);

        mockCells[3][2] = intactBuilding;
        mockCells[4][2] = destroyedBuilding;
        mockCells[5][2] = new Mountain(new Position2D(5, 2));

        Mockito.when(mockMapImporter.importMap(Mockito.any(String.class))).thenReturn(mockCells);
        GameMap.getInstance().initialize(mockMapImporter, "");
    }

    @Test
    public void pushingUnitIntoAnotherUnitStopsMovementAndDamagesBothUnits() throws PositionOutOfBoundsException {
        // Arrange
        Position2D pushedOrigin = new Position2D(1, 1);
        Position2D blockerPosition = new Position2D(2, 1);
        Unit pushedVek = new Unit(pushedOrigin, null);
        Unit blockingVek = new Unit(blockerPosition, null);

        // Act
        GameMap.getInstance().pushUnitTowards(pushedOrigin, new East());

        // Assert
        assertEquals(pushedOrigin, pushedVek.getPosition(), "Pushed unit should remain in its original position");
        assertTrue(pushedVek.isDamaged(), "Pushed unit should take collision damage");
        assertEquals(2, pushedVek.getCurrentHealth(), "Pushed unit should lose 1 health from collision");
        assertEquals(blockerPosition, blockingVek.getPosition(), "Blocking unit should remain in its position");
        assertTrue(blockingVek.isDamaged(), "Blocking unit should take collision damage");
        assertEquals(2, blockingVek.getCurrentHealth(), "Blocking unit should lose 1 health from collision");
    }

    @Test
    public void pushingUnitIntoBuildingStopsMovementDamagesUnitDestroysBuildingAndReducesPower() throws PositionOutOfBoundsException {
        // Arrange
        Position2D pushedOrigin = new Position2D(2, 2);
        Unit pushedVek = new Unit(pushedOrigin, null);
        int initialPower = ElectricalNetwork.getInstance().getPower();

        // Act
        GameMap.getInstance().pushUnitTowards(pushedOrigin, new East());

        // Assert
        assertEquals(pushedOrigin, pushedVek.getPosition(), "Pushed unit should not move onto an intact building");
        assertTrue(pushedVek.isDamaged(), "Pushed unit should take collision damage against building");
        assertEquals(2, pushedVek.getCurrentHealth(), "Pushed unit should lose 1 health from collision");
        assertTrue(intactBuilding.isDestroyed(), "Building should be destroyed by collision");
        assertEquals(initialPower - 1, ElectricalNetwork.getInstance().getPower(), "Electrical network should decrease by 1");
    }

    @Test
    public void pushingUnitIntoDestroyedBuildingMovesUnitWithoutCollisionDamage() throws PositionOutOfBoundsException {
        // Arrange
        Position2D pushedOrigin = new Position2D(2, 5);
        Position2D destination = new Position2D(3, 5);
        Unit pushedVek = new Unit(pushedOrigin, null);
        int initialPower = ElectricalNetwork.getInstance().getPower();

        // Act
        GameMap.getInstance().pushUnitTowards(pushedOrigin, new East());

        // Assert
        assertEquals(destination, pushedVek.getPosition(), "Pushed unit should move onto a destroyed building");
        assertFalse(pushedVek.isDamaged(), "Pushed unit should not take collision damage on a destroyed building");
        assertEquals(initialPower, ElectricalNetwork.getInstance().getPower(), "Electrical network should not decrease again");
    }

    @Test
    public void pushingUnitIntoMountainStopsMovementAndDamagesUnitWithoutDamagingMountain() throws PositionOutOfBoundsException {
        // Arrange
        Position2D pushedOrigin = new Position2D(4, 2);
        Position2D mountainPosition = new Position2D(5, 2);
        Unit pushedVek = new Unit(pushedOrigin, null);

        // Act
        GameMap.getInstance().pushUnitTowards(pushedOrigin, new East());

        // Assert
        assertEquals(pushedOrigin, pushedVek.getPosition(), "Pushed unit should not move onto a mountain");
        assertTrue(pushedVek.isDamaged(), "Pushed unit should take collision damage against mountain");
        assertEquals(2, pushedVek.getCurrentHealth(), "Pushed unit should lose 1 health from collision");
        assertFalse(GameMap.getInstance().getCellAt(mountainPosition).hasUnit(), "Mountain should remain unoccupied");
    }

    @Test
    public void pushingUnitOutOfBoundsThrowsExceptionAndKeepsUnitUninjuredInPlace() {
        // Arrange
        Position2D edgePosition = new Position2D(0, 0);
        Unit edgeVek = new Unit(edgePosition, null);

        // Act & Assert
        assertThrows(PositionOutOfBoundsException.class, () -> GameMap.getInstance().pushUnitTowards(edgePosition, new North()));
        assertEquals(edgePosition, edgeVek.getPosition(), "Unit at board edge should stay in place");
        assertFalse(edgeVek.isDamaged(), "Unit pushed against board edge should not take collision damage");
    }
}
