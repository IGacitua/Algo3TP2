package game.map;

import game.SingletonNotInitializedException;
import game.map.cell.Building;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.map.direction.*;
import game.units.Unit;

public class GameMap {
    private static final GameMap instance = new GameMap();

    private boolean initialized = false; // TODO: Temporary solution. Eventually I'd like to get rid of the singleton access.
    private CellGrid grid;

    private GameMap() {
    }

    public static GameMap getInstance() {
        return instance;
    }

    public void initialize(MapImporter importer, String mapFilePath) throws InvalidMapFileException {
        grid = new CellGrid(importer.importMap(mapFilePath));
        initialized = true;
    }

    public boolean tryPlaceUnit(Unit unit, Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return getCell(position).tryPlaceUnit(unit, false);
    }

    /// Temporary method for testing.
    public void placeBuilding(Position2D position) {
        ensureInitialized();
        grid.replaceCell(new Building(position), position);
    }

    public Cell getCell(Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return grid.getCell(position);
    }

    public void damage(Position2D target) throws PositionOutOfBoundsException {
        ensureInitialized();
        getCell(target).tryDamage();
    }

    public void pushUnit(Position2D unit, OrthogonalDirection direction) throws PositionOutOfBoundsException {
        ensureInitialized();

        Cell fromCell = getCell(unit);
        Cell toCell = getCell(unit.getAdjacent(direction));

        Unit unitToPush = fromCell.popUnit();
        if (unitToPush != null) {
            toCell.tryPlaceUnit(unitToPush, true);
        }
    }

    public void pushAllAdjacent(Position2D target) {
        ensureInitialized();

        pushAway(target, new North());
        pushAway(target, new South());
        pushAway(target, new East());
        pushAway(target, new West());
    }

    private void pushAway(Position2D target, OrthogonalDirection direction) {
        try {
            Position2D adjacent = target.getAdjacent(direction);
            pushUnit(adjacent, direction);
        } catch (PositionOutOfBoundsException e) {
            // Skip cells that are out of bounds.
        }
    }

    private void ensureInitialized() {
        if (!initialized) {
            throw new SingletonNotInitializedException(GameMap.class);
        }
    }
}
