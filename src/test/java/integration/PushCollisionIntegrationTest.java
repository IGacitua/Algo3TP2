package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.cell.Building;
import game.map.cell.Cell;
import game.map.cell.Mountain;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.East;
import game.position.direction.North;
import game.units.Team;
import game.units.Unit;
import game.units.movement.Movement;
import game.units.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class PushCollisionIntegrationTest {
    private ElectricalNetwork electricalNetwork;
    private GameMap gameMap;
    private Building building;

    @BeforeEach
    public void setUp() throws InvalidMapFileException {
        electricalNetwork = Mockito.mock(ElectricalNetwork.class);
        MapImporter mockMapImporter = Mockito.mock(MapImporter.class);
        Cell[][] mockCells = new Cell[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                mockCells[x][y] = new Cell(new Position(x, y));
            }
        }

        building = new Building(new Position(3, 2), electricalNetwork);

        mockCells[3][2] = building;
        mockCells[5][2] = new Mountain(new Position(5, 2));

        Mockito.when(mockMapImporter.importMap(Mockito.any(String.class))).thenReturn(mockCells);
        gameMap = new GameMap(mockMapImporter, "");
    }

    @Test
    public void pushingUnitIntoAnotherUnitStopsMovementAndDamagesBothUnits() throws PositionOutOfBoundsException {
        // Arrange
        Position pushedOrigin = new Position(1, 1);
        Position blockerPosition = new Position(2, 1);
        Unit pushedVek = new Unit(Team.Player, Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));
        Unit blockingVek = new Unit(Team.Player, Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));
        gameMap.tryPlaceUnit(pushedVek, pushedOrigin);
        gameMap.tryPlaceUnit(blockingVek, blockerPosition);

        // Act
        gameMap.pushUnit(pushedOrigin, new East());

        // Assert
        assertTrue(gameMap.hasUnitAt(pushedOrigin), "Pushed unit should remain in its original position");
        assertTrue(pushedVek.isDamaged(), "Pushed unit should take collision damage");
        assertEquals(2, pushedVek.getHealth(), "Pushed unit should lose 1 health from collision");

        assertTrue(gameMap.hasUnitAt(blockerPosition), "Blocking unit should remain in its original position");
        assertTrue(blockingVek.isDamaged(), "Blocking unit should take collision damage");
        assertEquals(2, blockingVek.getHealth(), "Blocking unit should lose 1 health from collision");
    }

    @Test
    public void pushingUnitIntoBuildingStopsMovementDamagesUnitDestroysBuildingAndReducesPower() throws PositionOutOfBoundsException {
        // Arrange
        Position pushedOrigin = new Position(2, 2);
        Unit pushedVek = new Unit(Team.Player, Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));
        gameMap.tryPlaceUnit(pushedVek, pushedOrigin);

        // Act
        gameMap.pushUnit(pushedOrigin, new East());

        // Assert
        assertTrue(gameMap.hasUnitAt(pushedOrigin), "Pushed unit should remain in its original position");
        assertTrue(pushedVek.isDamaged(), "Pushed unit should take collision damage against building");
        assertEquals(2, pushedVek.getHealth(), "Pushed unit should lose 1 health from collision");

        assertTrue(building.isDestroyed(), "Building should be destroyed by collision");
        Mockito.verify(electricalNetwork, Mockito.times(1)).modifyPower(-1);
    }

    @Test
    public void pushingUnitIntoDestroyedBuildingMovesUnitWithoutCollisionDamage() throws PositionOutOfBoundsException {
        // Arrange
        Position pushedOrigin = new Position(2, 5);
        Position destination = new Position(3, 5);
        Unit pushedVek = new Unit(Team.Player, Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));
        gameMap.tryPlaceUnit(pushedVek, pushedOrigin);

        // Act
        gameMap.pushUnit(pushedOrigin, new East());

        // Assert
        assertTrue(gameMap.hasUnitAt(destination), "Pushed unit should move onto the destroyed building");
        assertFalse(pushedVek.isDamaged(), "Pushed unit should not take collision damage on a destroyed building");

        Mockito.verify(electricalNetwork, Mockito.never()).modifyPower(Mockito.anyInt());
    }

    @Test
    public void pushingUnitIntoMountainStopsMovementAndDamagesUnitWithoutDamagingMountain() throws PositionOutOfBoundsException {
        // Arrange
        Position pushedOrigin = new Position(4, 2);
        Position mountainPosition = new Position(5, 2);
        Unit pushedVek = new Unit(Team.Player, Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));
        gameMap.tryPlaceUnit(pushedVek, pushedOrigin);

        // Act
        gameMap.pushUnit(pushedOrigin, new East());

        // Assert
        assertTrue(gameMap.hasUnitAt(pushedOrigin), "Pushed unit should remain in its original position");
        assertTrue(pushedVek.isDamaged(), "Pushed unit should take collision damage against mountain");
        assertEquals(2, pushedVek.getHealth(), "Pushed unit should lose 1 health from collision");

        assertFalse(gameMap.getCellAt(mountainPosition).hasUnit(), "Mountain should remain unoccupied");
    }

    @Test
    public void pushingUnitOutOfBoundsThrowsExceptionAndKeepsUnitUninjuredInPlace() throws PositionOutOfBoundsException {
        // Arrange
        Position edgePosition = new Position(0, 0);
        Unit edgeVek = new Unit(Team.Player, Mockito.mock(Weapon.class), 3, Mockito.mock(Movement.class));
        gameMap.tryPlaceUnit(edgeVek, edgePosition);

        // Act & Assert
        assertThrows(PositionOutOfBoundsException.class, () -> gameMap.pushUnit(edgePosition, new North()));
        assertTrue(gameMap.hasUnitAt(edgePosition), "Unit at board edge should remain in its original position");
        assertFalse(edgeVek.isDamaged(), "Unit pushed against board edge should not take collision damage");
    }
}