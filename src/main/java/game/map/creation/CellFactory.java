package game.map.creation;

import game.map.cell.GenericCell;

import java.util.Dictionary;

public class CellFactory {

    // Could be replaced by a switch in createFromId, but I like this way.
    private final Dictionary<String, Class<? extends GenericCell>> cellIDs;

    public CellFactory() {
        cellIDs = new java.util.Hashtable<>();
        cellIDs.put("ACID", game.map.cell.Acid.class); // Probably never in a map file, no reason not to add it tho.
        cellIDs.put("BUILDING", game.map.cell.Building.class);
        cellIDs.put("GROUND", GenericCell.class);
        cellIDs.put("MOUNTAIN", game.map.cell.Mountain.class);
        cellIDs.put("WATER", game.map.cell.Water.class);
    }

    public GenericCell createFromId(String cellID) throws InvalidMapFileException {
        Class<? extends GenericCell> cellClass = cellIDs.get(cellID);
        if (cellClass != null) {
            try {
                return cellClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new InvalidMapFileException(
                        "Error creating cell instance for ID: " + cellID + "\n" +
                                "Error: " + e.getMessage());
            }
        }
        throw new InvalidMapFileException("Invalid cell ID: " + cellID);
    }
}
