package game.map;

import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.*;
import game.units.Team;
import game.units.Unit;

import java.util.ArrayList;
import java.util.List;

public class GameMap {

    private static final int COLISSION_DAMAGE = 1;
    private static final OrthogonalDirection[] DIRECTIONS = {new North(), new South(), new East(), new West()};

    private final CellGrid grid;

    public GameMap(MapImporter importer, String mapFilePath) throws InvalidMapFileException {
        this.grid = new CellGrid(importer.importMap(mapFilePath));
    }

    // Cell Access

    // For ease of debugging.
    private static boolean isObstacle(Cell cell) {
        if (cell.isFull()) {
            return true;
        }

        if (cell.hasUnit()) {
            return true;
        }

        return cell.canDamage();
    }

    public Cell getCellAt(Position position) throws PositionOutOfBoundsException {
        return grid.getCell(position);
    }

    public void contaminateCell(Position position) throws PositionOutOfBoundsException {
        Cell cell = getCellAt(position);
        cell.contaminateUnit();
    }

    // Units

    public boolean hasUnitAt(Position position) throws PositionOutOfBoundsException {
        return getCellAt(position).hasUnit();
    }

    public boolean hasUnitAt(Position position, Team team) throws PositionOutOfBoundsException {
        Cell cell = getCellAt(position);
        return cell.hasUnit((u) -> u.isInTeam(team));
    }

    public void attackCell(Position target, int damage) throws PositionOutOfBoundsException {
        getCellAt(target).tryDamage(damage);
    }

    public boolean tryPlaceUnit(Unit unit, Position position) throws PositionOutOfBoundsException {
        return getCellAt(position).tryPlaceUnit(unit, false);
    }

    public boolean moveUnit(Position origin, Position destination, double consumptionMultiplier) throws PositionOutOfBoundsException {
        Cell originCell = getCellAt(origin);
        Unit unit = originCell.peekUnit();
        if (unit == null) {
            return false;
        }

        List<Position> path;
        try {
            path = grid.getPath(origin, destination, (c) -> !c.isFull());
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
                return false;
            }
            current = next;
        }
        return true;
    }

    public void pushUnit(Position unitPosition, OrthogonalDirection direction) throws PositionOutOfBoundsException {
        Cell fromCell = getCellAt(unitPosition);
        Cell toCell = getCellAt(unitPosition.getAdjacent(direction));

        if (!fromCell.moveUnit(toCell, true, 0)) {
            fromCell.tryDamage(COLISSION_DAMAGE);
            toCell.tryDamage(COLISSION_DAMAGE);
        }
    }

    public void pushAllAdjacent(Position target) {
        for (OrthogonalDirection direction : DIRECTIONS) {
            pushUnitAway(target, direction);
        }
    }

    // Search

    private void pushUnitAway(Position target, OrthogonalDirection direction) {
        try {
            pushUnit(target.getAdjacent(direction), direction);
        } catch (PositionOutOfBoundsException e) {
            // Skip cells that are out of bounds.
        }
    }

    // For game UI later.
    public List<Position> getReachablePositions(Position unitPosition, int radius) throws PositionOutOfBoundsException {
        Unit unit = getCellAt(unitPosition).peekUnit();
        if (unit == null) {
            return new ArrayList<>();
        }
        return grid.getCellsInRadius(unitPosition, radius, (c) -> !c.isFull());
    }

    public Position findClosestVekTarget(Position origin) {
        return grid.getNearestCell(origin, cell -> cell.isVekTarget() && cell.canDamage());
    }

    public Position findClosestPlayerUnit(Position origin) {
        return grid.getNearestCell(origin, cell -> cell.hasUnit((u) -> u.isInTeam(Team.Player)) && cell.canDamage());
    }

    public Position findFirstObstacle(Position origin, OrthogonalDirection direction) {
        // isFull() es necesario por celdas que 'bloquean' sin tener unidades.
        return grid.firstCellInLine(origin, direction, GameMap::isObstacle);
    }
}