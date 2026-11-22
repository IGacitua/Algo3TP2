package game.map.creation;

import game.electricalNetwork.ElectricalNetwork;
import game.map.cell.*;
import game.position.Position;

public class CellFactory {
    public final String ACID_ID;
    public final String BUILDING_ID;
    public final String GROUND_ID;
    public final String MOUNTAIN_ID;
    public final String WATER_ID;

    private final ElectricalNetwork network;

    public CellFactory(ElectricalNetwork network) {
        this.network = network;

        ACID_ID = "A";
        BUILDING_ID = "B";
        GROUND_ID = "G";
        MOUNTAIN_ID = "M";
        WATER_ID = "W";
    }

    public Cell createFromId(String cellId, Position cellPosition) throws InvalidMapFileException {
        if (cellId.equals(ACID_ID))
            return new Acid(cellPosition);

        if (cellId.equals(BUILDING_ID))
            return new Building(cellPosition, network);

        if (cellId.equals(GROUND_ID))
            return new Cell(cellPosition);

        if (cellId.equals(MOUNTAIN_ID))
            return new Mountain(cellPosition);

        if (cellId.equals(WATER_ID))
            return new Water(cellPosition);

        throw new InvalidMapFileException("Invalid cell ID: " + cellId);
    }
}
