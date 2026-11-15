package game.map.creation;

import game.map.cell.Acid;
import game.map.cell.Cell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CellFactoryTest {
    @Test
    public void testCreateAcid() {
        // Arrange
        CellFactory cellFactory = new CellFactory();
        String acidId = new Acid(null).cellTypeId();

        // Act
        Cell acidCell = cellFactory.createFromId(acidId, null);
        String cellId = acidCell.cellTypeId();

        // Assert
        Assertions.assertEquals(acidId, cellId, "Expected acid, got: " + cellId);
    }

    @Test
    public void testCreateGround() {
        // Arrange
        CellFactory cellFactory = new CellFactory();
        String groundId = new Cell(null).cellTypeId();

        // Act
        Cell groundCell = cellFactory.createFromId(groundId, null);
        String cellId = groundCell.cellTypeId();

        // Assert
        Assertions.assertEquals(groundId, cellId, "Expected ground, got: " + cellId);
    }

    @Test
    public void testCreateMountain() {
        // Arrange
        CellFactory cellFactory = new CellFactory();
        String mountainId = new game.map.cell.Mountain(null).cellTypeId();

        // Act
        Cell mountainCell = cellFactory.createFromId(mountainId, null);
        String cellId = mountainCell.cellTypeId();

        // Assert
        Assertions.assertEquals(mountainId, cellId, "Expected mountain, got: " + cellId);
    }

    @Test
    public void testCreateWater() {
        // Arrange
        CellFactory cellFactory = new CellFactory();
        String waterId = new game.map.cell.Water(null).cellTypeId();

        // Act
        Cell waterCell = cellFactory.createFromId(waterId, null);
        String cellId = waterCell.cellTypeId();

        // Assert
        Assertions.assertEquals(waterId, cellId, "Expected water, got: " + cellId);
    }

    @Test
    public void testCreateBuilding() {
        // Arrange
        CellFactory cellFactory = new CellFactory();
        String buildingId = new game.map.cell.Building(null).cellTypeId();

        // Act
        Cell buildingCell = cellFactory.createFromId(buildingId, null);
        String cellId = buildingCell.cellTypeId();

        // Assert
        Assertions.assertEquals(buildingId, cellId, "Expected building, got: " + cellId);
    }

    @Test
    public void testCreateInvalidCell() {
        // Arrange
        CellFactory cellFactory = new CellFactory();
        String invalidId = "INVALID ID!!!";

        // Act & Assert
        Assertions.assertThrows(InvalidMapFileException.class, () -> {
            cellFactory.createFromId(invalidId, null);
        });
    }
}
