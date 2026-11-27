package game.map.creation;

import game.map.GameMap;
import game.position.Position;
import game.units.UnitFactory;
import game.utils.FileHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class MapImporterTest {

    @Test
    public void testIOExceptionThrowsNoMapFileException() throws IOException {
        // Arrange
        FileHandler fileHandler = Mockito.mock(FileHandler.class);
        Mockito.when(fileHandler.readFile(Mockito.anyString())) // Esa "llamada" hace necesario el 'throws IOException' del test.
                .thenThrow(new IOException("File not found"));
        CellFactory cellFactory = Mockito.mock(CellFactory.class);
        UnitFactory unitFactory = Mockito.mock(UnitFactory.class);

        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory, unitFactory);

        // Act & Assert

        Assertions.assertThrows(NoMapFileException.class, () -> mapImporter.importMap(Mockito.mock(GameMap.class), "nonexistent_file.txt"));
    }

    @Test
    public void testInvalidMapFileThrowsInvalidMapFileException() throws IOException {
        // Arrange
        String map = "A B C" + "\n" + "D E";
        FileHandler fileHandler = Mockito.mock(FileHandler.class);
        Mockito.when(fileHandler.readFile(Mockito.anyString()))
                .thenReturn(map);
        CellFactory cellFactory = Mockito.mock(CellFactory.class);
        UnitFactory unitFactory = Mockito.mock(UnitFactory.class);

        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory, unitFactory);

        // Act & Assert
        Assertions.assertThrows(InvalidMapFileException.class, () -> mapImporter.importMap(Mockito.mock(GameMap.class), "invalid_map.txt"));
    }

    @Test
    public void testEmptyMapFileThrowsInvalidMapFileException() throws IOException {
        // Arrange
        String map = "";
        FileHandler fileHandler = Mockito.mock(FileHandler.class);
        Mockito.when(fileHandler.readFile(Mockito.anyString()))
                .thenReturn(map);
        CellFactory cellFactory = Mockito.mock(CellFactory.class);
        UnitFactory unitFactory = Mockito.mock(UnitFactory.class);

        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory, unitFactory);

        // Act & Assert
        Assertions.assertThrows(InvalidMapFileException.class, () -> mapImporter.importMap(Mockito.mock(GameMap.class), "empty_map.txt"));
    }

    @Test
    public void validMapFileCallsWithCorrectIdsAndPositionsA() throws IOException, InvalidMapFileException {
        // Arrange
        String map = "A B C";
        FileHandler fileHandler = Mockito.mock(FileHandler.class);
        Mockito.when(fileHandler.readFile(Mockito.anyString()))
                .thenReturn(map);
        CellFactory cellFactory = Mockito.mock(CellFactory.class);
        UnitFactory unitFactory = Mockito.mock(UnitFactory.class);

        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory, unitFactory);

        // Act
        Assertions.assertDoesNotThrow(() -> mapImporter.importMap(Mockito.mock(GameMap.class), "valid_map.txt"));

        // Assert
        Mockito.verify(cellFactory).createFromId("A", new Position(0, 0));
        Mockito.verify(cellFactory).createFromId("B", new Position(1, 0));
        Mockito.verify(cellFactory).createFromId("C", new Position(2, 0));
    }

    @Test
    public void validMapFileCallsWithCorrectIdsAndPositionsB() throws IOException, InvalidMapFileException {
        // Arrange
        String map = "A B C" + "\n" + "D E F";
        FileHandler fileHandler = Mockito.mock(FileHandler.class);
        Mockito.when(fileHandler.readFile(Mockito.anyString()))
                .thenReturn(map);
        CellFactory cellFactory = Mockito.mock(CellFactory.class);
        UnitFactory unitFactory = Mockito.mock(UnitFactory.class);

        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory, unitFactory);
        // Act
        Assertions.assertDoesNotThrow(() -> mapImporter.importMap(Mockito.mock(GameMap.class), "valid_map.txt"));

        // Assert
        Mockito.verify(cellFactory).createFromId("A", new Position(0, 0));
        Mockito.verify(cellFactory).createFromId("B", new Position(1, 0));
        Mockito.verify(cellFactory).createFromId("C", new Position(2, 0));
        Mockito.verify(cellFactory).createFromId("D", new Position(0, 1));
        Mockito.verify(cellFactory).createFromId("E", new Position(1, 1));
        Mockito.verify(cellFactory).createFromId("F", new Position(2, 1));
    }
}
