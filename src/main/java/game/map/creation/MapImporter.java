package game.map.creation;

import game.map.cell.Cell;
import game.position.Position;
import game.units.UnitFactory;
import game.utils.FileHandler;

import java.io.IOException;

public class MapImporter {
    private final FileHandler fileHandler;
    private final CellFactory cellFactory;
    private final UnitFactory unitFactory;

    public MapImporter(FileHandler fileHandler, CellFactory cellFactory, UnitFactory unitFactory) {
        this.fileHandler = fileHandler;
        this.cellFactory = cellFactory;
        this.unitFactory = unitFactory;
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
                map[x][y] = cellFactory.createFromId(cellID, new Position(x, y));
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
