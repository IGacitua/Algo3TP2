package game.map.creation;

import game.map.Position2D;
import game.utils.FileHandler;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.fail;

public class MapImporterTest {

    @Test
    public void testIOExceptionThrowsNoMapFileException() throws IOException {
        // Arrange
        FileHandler fileHandler = Mockito.mock(FileHandler.class);
        Mockito.when(fileHandler.readFile(Mockito.anyString())) // Esa "llamada" hace necesario el 'throws IOException' del test.
                .thenThrow(new IOException("File not found"));
        CellFactory cellFactory = Mockito.mock(CellFactory.class);

        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory);

        // Act
        try {
            mapImporter.importMap("nonexistent_file.txt");
        }

        // Assert
        catch (NoMapFileException e) {
            return;
        } catch (InvalidMapFileException e) {
            fail("Expected NoMapFileException, but got InvalidMapFileException.");
        }

        fail("Expected NoMapFileException, got none.");
    }

    @Test
    public void testInvalidMapFileThrowsInvalidMapFileException() throws IOException {
        // Arrange
        String map = "A B C" + "\n" + "D E";
        FileHandler fileHandler = Mockito.mock(FileHandler.class);
        Mockito.when(fileHandler.readFile(Mockito.anyString()))
                .thenReturn(map);
        CellFactory cellFactory = Mockito.mock(CellFactory.class);

        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory);

        // Act
        try {
            mapImporter.importMap("invalid_map.txt");
        }

        // Assert
        catch (InvalidMapFileException e) {
            return;
        } catch (NoMapFileException e) {
            fail("Expected InvalidMapFileException, but got NoMapFileException.");
        }

        fail("Expected InvalidMapFileException, got none.");
    }

    @Test
    public void testEmptyMapFileThrowsInvalidMapFileException() throws IOException {
        // Arrange
        String map = "";
        FileHandler fileHandler = Mockito.mock(FileHandler.class);
        Mockito.when(fileHandler.readFile(Mockito.anyString()))
                .thenReturn(map);
        CellFactory cellFactory = Mockito.mock(CellFactory.class);

        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory);

        // Act
        try {
            mapImporter.importMap("empty_map.txt");
        }

        // Assert
        catch (InvalidMapFileException e) {
            return;
        } catch (NoMapFileException e) {
            fail("Expected InvalidMapFileException, but got NoMapFileException.");
        }

        fail("Expected InvalidMapFileException, got none.");
    }

    @Test
    public void validMapFileCallsWithCorrectIdsAndPositionsA() throws IOException, InvalidMapFileException {
        // Arrange
        String map = "A B C";
        FileHandler fileHandler = Mockito.mock(FileHandler.class);
        Mockito.when(fileHandler.readFile(Mockito.anyString()))
                .thenReturn(map);
        CellFactory cellFactory = Mockito.mock(CellFactory.class);
        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory);

        // Act
        try {
            mapImporter.importMap("valid_map.txt");
        } catch (InvalidMapFileException e) {
            fail("Expected valid map file, but got InvalidMapFileException.");
        }

        // Assert
        Mockito.verify(cellFactory).createFromId("A", new Position2D(0, 0));
        Mockito.verify(cellFactory).createFromId("B", new Position2D(1, 0));
        Mockito.verify(cellFactory).createFromId("C", new Position2D(2, 0));
    }

    @Test
    public void validMapFileCallsWithCorrectIdsAndPositionsB() throws IOException, InvalidMapFileException {
        // Arrange
        String map = "A B C" + "\n" + "D E F";
        FileHandler fileHandler = Mockito.mock(FileHandler.class);
        Mockito.when(fileHandler.readFile(Mockito.anyString()))
                .thenReturn(map);
        CellFactory cellFactory = Mockito.mock(CellFactory.class);
        MapImporter mapImporter = new MapImporter(fileHandler, cellFactory);

        // Act
        try {
            mapImporter.importMap("valid_map.txt");
        } catch (InvalidMapFileException e) {
            fail("Expected valid map file, but got InvalidMapFileException.");
        }

        // Assert
        Mockito.verify(cellFactory).createFromId("A", new Position2D(0, 0));
        Mockito.verify(cellFactory).createFromId("B", new Position2D(1, 0));
        Mockito.verify(cellFactory).createFromId("C", new Position2D(2, 0));
        Mockito.verify(cellFactory).createFromId("D", new Position2D(0, 1));
        Mockito.verify(cellFactory).createFromId("E", new Position2D(1, 1));
        Mockito.verify(cellFactory).createFromId("F", new Position2D(2, 1));
    }
}
