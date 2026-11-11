package game.map.creation;

import game.map.Position2D;
import game.map.cell.*;

public class CellFactory {

    // I could use a Dictionary<ID, Class> and use reflection to get the constructor... but that's overkill.
    // These could be instance variables, and initialized in the class, but again, no reason to.
    // I could also set the IDs in the Cell class as static, but I prefer having everything in one place.
    private static final String ACID_ID = "A";
    private static final String BUILDING_ID = "B";
    private static final String GROUND_ID = "G";
    private static final String MOUNTAIN_ID = "M";
    private static final String WATER_ID = "W";

    public Cell createFromId(String cellID, Position2D cellPosition) throws InvalidMapFileException {
        switch (cellID) {
            case ACID_ID:
                return new Acid(cellPosition);
            case BUILDING_ID:
                return new Building(cellPosition);
            case GROUND_ID:
                return new Cell(cellPosition);
            case MOUNTAIN_ID:
                return new Mountain(cellPosition);
            case WATER_ID:
                return new Water(cellPosition);
            default:
                throw new InvalidMapFileException("Invalid cell ID: " + cellID);
        }
    }
}
