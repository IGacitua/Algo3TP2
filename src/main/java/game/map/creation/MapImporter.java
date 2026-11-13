package game.map.creation;

import game.map.Position2D;
import game.map.cell.Cell;
import game.utils.FileHandler;

import java.io.IOException;

public class MapImporter {
    private final FileHandler fileHandler;
    private final CellFactory cellFactory;

    public MapImporter(FileHandler fileHandler, CellFactory cellFactory) {
        this.fileHandler = fileHandler;
        this.cellFactory = cellFactory;
    }

    public Cell[][] importMap(String filePath) throws NoMapFileException, InvalidMapFileException {
        try {
            String mapData = fileHandler.readFile(filePath);
            return parseData(mapData);
        } catch (IOException e) {
            throw new NoMapFileException(filePath);
        }
    }

    private Cell[][] parseData(String mapData) throws InvalidMapFileException {
        String[] rows = mapData.split("\\R"); // \\R matches any line break.
        int height = rows.length;
        int width = getColumnCount(rows);

        Cell[][] map = new Cell[width][height]; // Indexed [x][y]

        for (int y = 0; y < height; y++) {
            String[] cellIDs = rows[y].split(" ");
            for (int x = 0; x < width; x++) {
                String cellID = cellIDs[x];
                if (cellID.isEmpty()) {
                    throw new InvalidMapFileException("Empty cell ID found in the map file.");
                }
                map[x][y] = cellFactory.createFromId(cellID, new Position2D(x, y));
            }
        }

        return map;
    }

    private int getColumnCount(String[] rows) throws InvalidMapFileException {
        int expectedColumnCount = rows[0].split(" ").length;
        for (String row : rows) {
            if (row.split(" ").length != expectedColumnCount) {
                throw new InvalidMapFileException("Inconsistent number of columns in the map file.");
            }
        }
        return expectedColumnCount;
    }
}
