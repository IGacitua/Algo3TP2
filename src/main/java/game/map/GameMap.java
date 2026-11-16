package game.map;

import game.SingletonNotInitializedException;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position2D;
import game.position.PositionOutOfBoundsException;
import game.position.direction.*;
import game.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

    private void ensureInitialized() {
        if (!initialized) {
            throw new SingletonNotInitializedException(GameMap.class);
        }
    }

    // Cell Access

    public Cell getCellAt(Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return grid.getCell(position);
    }

    public boolean hasMechAt(Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return getCellAt(position).hasMech();
    }

    public boolean hasVekTargetAt(Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return getCellAt(position).isVekTarget();
    }

    public boolean hasWaterAt(Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return getCellAt(position).isWater();
    }

    // Unit

    public void damageCellAt(Position2D target, int damage) throws PositionOutOfBoundsException {
        ensureInitialized();
        getCellAt(target).tryDamage(damage);
    }

    public boolean placeUnitAt(Unit unit, Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return getCellAt(position).tryPlaceUnit(unit, false);
    }

    public boolean moveUnitTo(Position2D origin, Position2D destination) throws PositionOutOfBoundsException {
        ensureInitialized();
        Cell originCell = getCellAt(origin);
        Cell destinationCell = getCellAt(destination);

        Unit unitToMove = originCell.popUnit();
        if (unitToMove == null) {
            return false;
        }

        int movementDistance = unitToMove.getMovementDistance();
        if (origin.getOrthogonalDistance(destination) > movementDistance) {
            // The destination is too far away.
            originCell.tryPlaceUnit(unitToMove, true); // Put the unit back.
            return false;
        }

        return destinationCell.tryPlaceUnit(unitToMove, true);
    }

    public void pushUnitTowards(Position2D unit, OrthogonalDirection direction) throws PositionOutOfBoundsException {
        ensureInitialized();

        Cell fromCell = getCellAt(unit);
        Cell toCell = getCellAt(unit.getAdjacent(direction));

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
            pushUnitTowards(adjacent, direction);
        } catch (PositionOutOfBoundsException e) {
            // Skip cells that are out of bounds.
        }
    }

    // Search

    public List<Position2D> getReachablePositions(Position2D unitPosition) throws PositionOutOfBoundsException {
        Cell position = getCellAt(unitPosition);
        if (!position.hasUnit()) {
            return new ArrayList<>(); // Empty
        }

        Unit unit = getCellAt(unitPosition).popUnit();
        int movementDistance = unit.getMovementDistance();
        Predicate<Cell> canMoveTo = (c) -> c.canPlaceUnit(unit, false);

        return grid.getCellsInRadius(unitPosition, movementDistance, canMoveTo);
    }

    public Position2D findClosestVekTarget(Position2D origin) {
        return grid.getNearestCell(origin, (c) -> c.isVekTarget() && c.canDamage());
    }

    public Position2D findAttackableMechFrom(Position2D origin) {
        return grid.getNearestCell(origin, (c) -> c.hasMech() && c.canDamage());
    }

    public double distanceToClosestVekTarget(Position2D pos) {
        return grid.getDistanceToNearestCell(pos, Cell::isVekTarget);
    }
}