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
import java.util.function.Predicate;

public class GameMap {
    private final CellGrid grid;

    public GameMap(MapImporter importer, String mapFilePath) throws InvalidMapFileException {
        grid = new CellGrid(importer.importMap(mapFilePath));
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

    // Unit

    public void damageCellAt(Position target, int damage) throws PositionOutOfBoundsException {
        getCellAt(target).tryDamage(damage);
    }

    public boolean hasUnitAt(Position position) throws PositionOutOfBoundsException {
        return getCellAt(position).hasUnit();
    }

    public boolean placeUnitAt(Unit unit, Position position) throws PositionOutOfBoundsException {
        return getCellAt(position).tryPlaceUnit(unit, false);
    }

    public boolean moveUnitTo(Position origin, Position destination) throws PositionOutOfBoundsException {
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

    public void pushUnitTowards(Position unit, OrthogonalDirection direction) throws PositionOutOfBoundsException {
        Cell fromCell = getCellAt(unit);
        Cell toCell = getCellAt(unit.getAdjacent(direction));

        Unit unitToPush = fromCell.popUnit();
        if (unitToPush == null) {
            return;
        }

        boolean placed = toCell.tryPlaceUnit(unitToPush, true);
        if (!placed) {
            fromCell.tryPlaceUnit(unitToPush, false);
            unitToPush.damage(1);
            toCell.tryDamage(1);
        }
    }

    public void pushAllAdjacent(Position target) {
        pushAway(target, new North());
        pushAway(target, new South());
        pushAway(target, new East());
        pushAway(target, new West());
    }

    private void pushAway(Position target, OrthogonalDirection direction) {
        try {
            Position adjacent = target.getAdjacent(direction);
            pushUnitTowards(adjacent, direction);
        } catch (PositionOutOfBoundsException e) {
            // Skip cells that are out of bounds.
        }
    }

    // Search

    public List<Position> getReachablePositions(Position unitPosition) throws PositionOutOfBoundsException {
        Cell position = getCellAt(unitPosition);
        if (!position.hasUnit()) {
            return new ArrayList<>(); // Empty
        }

        Unit unit = getCellAt(unitPosition).popUnit();
        int movementDistance = unit.getMovementDistance();
        Predicate<Cell> canMoveTo = (c) -> c.canPlaceUnit(unit, false);

        return grid.getCellsInRadius(unitPosition, movementDistance, canMoveTo);
    }

    public Position findClosestVekTarget(Position origin) {
        return grid.getNearestCell(origin, (c) -> c.isVekTarget() && c.canDamage());
    }

    public Position findClosestPlayerUnit(Position origin) {
        return grid.getNearestCell(origin, (c) -> c.hasMech() && c.canDamage());
    }

    public double distanceToClosestVekTarget(Position pos) {
        return grid.getDistanceToNearestCell(pos, Cell::isVekTarget);
    }
}