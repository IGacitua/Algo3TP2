package game.map;

import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.*;
import game.units.Unit;

import java.util.ArrayList;
import java.util.List;

public class GameMap {

    private static final OrthogonalDirection[] DIRECTIONS =
            {new North(), new South(), new East(), new West()};

    private final CellGrid grid;

    public GameMap(MapImporter importer, String mapFilePath) throws InvalidMapFileException {
        this.grid = new CellGrid(importer.importMap(mapFilePath));
    }

    // Cell Access

    public Cell getCellAt(Position position) throws PositionOutOfBoundsException {
        return grid.getCell(position);
    }

    public boolean hasMechAt(Position position) throws PositionOutOfBoundsException {
        return getCellAt(position).hasMech();
    }

    public boolean hasVekTargetAt(Position position) throws PositionOutOfBoundsException {
        return getCellAt(position).isVekTarget();
    }

    public boolean hasWaterAt(Position position) throws PositionOutOfBoundsException {
        return getCellAt(position).isWater();
    }

    public boolean hasUnitAt(Position position) throws PositionOutOfBoundsException {
        return getCellAt(position).hasUnit();
    }

    // Units

    public void damageCellAt(Position target, int damage) throws PositionOutOfBoundsException {
        getCellAt(target).tryDamage(damage);
    }

    public boolean placeUnitAt(Unit unit, Position position) throws PositionOutOfBoundsException {
        return getCellAt(position).tryPlaceUnit(unit, false);
    }

    public boolean moveUnitTo(Position origin, Position destination, double consumptionMultiplier) throws PositionOutOfBoundsException {
        Cell originCell = getCellAt(origin);
        Unit unit = originCell.peekUnit();
        if (unit == null) {
            return false;
        }

        List<Position> path;
        try {
            path = grid.getPath(origin, destination, Cell::isFull);
        } catch (NoPathException e) {
            return false;
        }

        // Check the whole trip up front so a unit never stops halfway for lack of movement.
        if (path.isEmpty()) {
            return false;
        }

        Cell current = originCell;
        for (Position step : path) {
            Cell next = getCellAt(step);
            if (!current.moveUnit(next, false, consumptionMultiplier)) {
                return false; // Ran out of movement. The unit stays on the last cell it reached.
            }
            current = next;
        }
        return true;
    }

    public void pushUnitTowards(Position unitPosition, OrthogonalDirection direction) throws PositionOutOfBoundsException {
        Cell fromCell = getCellAt(unitPosition);
        Cell toCell = getCellAt(unitPosition.getAdjacent(direction));

        Unit unit = fromCell.popUnit();
        if (unit == null) {
            return;
        }

        if (!toCell.tryPlaceUnit(unit, true)) {
            fromCell.tryPlaceUnit(unit, false);
            unit.damage(1);
            toCell.tryDamage(1);
        }
    }

    public void pushAllAdjacent(Position target) {
        for (OrthogonalDirection direction : DIRECTIONS) {
            pushAway(target, direction);
        }
    }

    private void pushAway(Position target, OrthogonalDirection direction) {
        try {
            pushUnitTowards(target.getAdjacent(direction), direction);
        } catch (PositionOutOfBoundsException e) {
            // Skip cells that are out of bounds.
        }
    }

    // Search

    public List<Position> getReachablePositions(Position unitPosition, int radius) throws PositionOutOfBoundsException {
        Unit unit = getCellAt(unitPosition).peekUnit();
        if (unit == null) {
            return new ArrayList<>();
        }
        return grid.getCellsInRadius(unitPosition, radius, Cell::isFull);
    }

    public List<Position> getPath(Position origin, Position destination) throws PositionOutOfBoundsException {
        return grid.getPath(origin, destination, Cell::isFull);
    }

    public Position findClosestVekTarget(Position origin) {
        return grid.getNearestCell(origin, cell -> cell.isVekTarget() && cell.canDamage());
    }

    public Position findClosestPlayerUnit(Position origin) {
        return grid.getNearestCell(origin, cell -> cell.hasMech() && cell.canDamage());
    }

    public double distanceToClosestVekTarget(Position origin) {
        return grid.getDistanceToNearestCell(origin, Cell::isVekTarget);
    }
}