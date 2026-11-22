package game.units.weapons;

import game.map.GameMap;
import game.map.cell.Building;
import game.map.cell.Cell;
import game.map.cell.Mountain;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.*;

import java.util.List;

public class Cannon extends Weapon {

    @Override
    public boolean attack(Position target, GameMap map) {
        OrthogonalDirection directionToShooter = getDirectionToShooter(target, map);

        if (directionToShooter instanceof NoDirection) {
            throw new IllegalArgumentException("Cannon can only fire in a straight orthogonal line.");
        }

        Position shooterOrigin = findFurthestUnitPosition(target, directionToShooter, map);
        OrthogonalDirection fireDirection = directionToShooter.invert();

        return fireAlongLine(shooterOrigin, fireDirection, map);
    }

    private boolean fireAlongLine(Position origin, OrthogonalDirection fireDirection, GameMap map) {
        Position current = origin.getAdjacent(fireDirection);

        while (true) {
            Cell cell;
            try {
                cell = map.getCellAt(current);
            } catch (PositionOutOfBoundsException e) {
                return false;
            }

            if (isObstacle(cell)) {
                impactCell(current, cell, fireDirection, map);
                return true;
            }

            current = current.getAdjacent(fireDirection);
        }
    }

    private void impactCell(Position position, Cell cell, OrthogonalDirection fireDirection, GameMap map) {
        try {
            map.damageCellAt(position, 1);
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e);
        }

        if (cell.hasUnit()) {
            try {
                map.pushUnitTowards(position, fireDirection);
            } catch (PositionOutOfBoundsException ignored) {
                // Si el empuje cae fuera del tablero, la unidad permanece en el borde
            }
        }
    }

    private OrthogonalDirection getDirectionToShooter(Position target, GameMap map) {
        List<OrthogonalDirection> directions = List.of(new North(), new East(), new South(), new West());

        for (OrthogonalDirection direction : directions) {
            if (findFurthestUnitPosition(target, direction, map) != null) {
                return direction;
            }
        }

        return new NoDirection();
    }

    private Position findFurthestUnitPosition(Position start, OrthogonalDirection direction, GameMap map) {
        Position current = start.getAdjacent(direction);
        Position furthestUnitPosition = null;

        while (true) {
            try {
                Cell cell = map.getCellAt(current);
                if (cell.hasUnit()) {
                    furthestUnitPosition = current;
                }
            } catch (PositionOutOfBoundsException e) {
                break;
            }
            current = current.getAdjacent(direction);
        }

        return furthestUnitPosition;
    }

    private boolean isObstacle(Cell cell) {
        // TODO: Change checks to check for non-forced movement availability for a new
        // TODO: MovementType that ignores water and such.
        if (cell == null) {
            return false;
        }

        if (cell.hasUnit()) {
            return true;
        }

        if (cell instanceof Building) {
            return true;
        }

        return cell instanceof Mountain;
    }
}
