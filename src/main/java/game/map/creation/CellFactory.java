package game.map.creation;

import game.map.cell.*;
import game.position.Position2D;

public class CellFactory {

    // I could use a Dictionary<ID, Class> and use reflection to get the constructor... but that's overkill.
    public final String ACID_ID;
    public final String BUILDING_ID;
    public final String GROUND_ID;
    public final String MOUNTAIN_ID;
    public final String WATER_ID;

    public CellFactory() {
        ACID_ID = new Acid(null).cellTypeId();
        BUILDING_ID = new Building(null).cellTypeId();
        GROUND_ID = new Cell(null).cellTypeId();
        MOUNTAIN_ID = new Mountain(null).cellTypeId();
        WATER_ID = new Water(null).cellTypeId();
    }

    public Cell createFromId(String cellId, Position2D cellPosition) throws InvalidMapFileException {
        if (cellId.equals(ACID_ID))
            return new Acid(cellPosition);

        if (cellId.equals(BUILDING_ID))
            return new Building(cellPosition);

        if (cellId.equals(GROUND_ID))
            return new Cell(cellPosition);

        if (cellId.equals(MOUNTAIN_ID))
            return new Mountain(cellPosition);

        if (cellId.equals(WATER_ID))
            return new Water(cellPosition);

        throw new InvalidMapFileException("Invalid cell ID: " + cellId);
    }
}
