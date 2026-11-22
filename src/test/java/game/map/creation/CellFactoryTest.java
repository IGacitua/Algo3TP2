package game.map.creation;

import game.electricalNetwork.ElectricalNetwork;
import game.map.cell.Acid;
import game.map.cell.Cell;
import game.position.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CellFactoryTest {

    private static ElectricalNetwork mockNetwork() {
        return Mockito.mock(ElectricalNetwork.class);
    }


    @Test
    public void testCreateAcid() {
        // Arrange
        CellFactory cellFactory = new CellFactory(mockNetwork());
        String acidId = new Acid(new Position(0, 0)).cellTypeId();

        // Act
        Cell acidCell = cellFactory.createFromId(acidId, null);
        String cellId = acidCell.cellTypeId();

        // Assert
        Assertions.assertEquals(acidId, cellId, "Expected acid, got: " + cellId);
    }

    @Test
    public void testCreateGround() {
        // Arrange
        CellFactory cellFactory = new CellFactory(mockNetwork());
        String groundId = new Cell(new Position(0, 0)).cellTypeId();

        // Act
        Cell groundCell = cellFactory.createFromId(groundId, null);
        String cellId = groundCell.cellTypeId();

        // Assert
        Assertions.assertEquals(groundId, cellId, "Expected ground, got: " + cellId);
    }

    @Test
    public void testCreateMountain() {
        // Arrange
        CellFactory cellFactory = new CellFactory(mockNetwork());
        String mountainId = new game.map.cell.Mountain(new Position(0, 0)).cellTypeId();

        // Act
        Cell mountainCell = cellFactory.createFromId(mountainId, null);
        String cellId = mountainCell.cellTypeId();

        // Assert
        Assertions.assertEquals(mountainId, cellId, "Expected mountain, got: " + cellId);
    }

    @Test
    public void testCreateWater() {
        // Arrange
        CellFactory cellFactory = new CellFactory(mockNetwork());
        String waterId = new game.map.cell.Water(new Position(0, 0)).cellTypeId();

        // Act
        Cell waterCell = cellFactory.createFromId(waterId, null);
        String cellId = waterCell.cellTypeId();

        // Assert
        Assertions.assertEquals(waterId, cellId, "Expected water, got: " + cellId);
    }

    @Test
    public void testCreateBuilding() {
        // Arrange
        CellFactory cellFactory = new CellFactory(mockNetwork());
        String buildingId = new game.map.cell.Building(new Position(0, 0), mockNetwork()).cellTypeId();

        // Act
        Cell buildingCell = cellFactory.createFromId(buildingId, null);
        String cellId = buildingCell.cellTypeId();

        // Assert
        Assertions.assertEquals(buildingId, cellId, "Expected building, got: " + cellId);
    }

    @Test
    public void testCreateInvalidCell() {
        // Arrange
        CellFactory cellFactory = new CellFactory(mockNetwork());
        String invalidId = "INVALID ID!!!";

        // Act & Assert
        Assertions.assertThrows(InvalidMapFileException.class, () -> {
            cellFactory.createFromId(invalidId, null);
        });
    }
}
