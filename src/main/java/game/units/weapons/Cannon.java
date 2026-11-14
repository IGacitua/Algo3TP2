package game.units.weapons;

import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.map.cell.Building;
import game.map.cell.Cell;
import game.map.cell.Mountain;
import game.map.direction.East;
import game.map.direction.NoDirection;
import game.map.direction.North;
import game.map.direction.OrthogonalDirection;
import game.map.direction.South;
import game.map.direction.West;

import java.util.List;

public class Cannon extends Weapon {

    @Override
    public void attack(Position2D target) {
        OrthogonalDirection directionToShooter = getDirectionToShooter(target);

        if (directionToShooter instanceof NoDirection) {
            throw new IllegalArgumentException("Cannon can only fire in a straight orthogonal line.");
        }

        Position2D shooterOrigin = findFurthestUnitPosition(target, directionToShooter);
        OrthogonalDirection fireDirection = directionToShooter.invert();

        fireAlongLine(shooterOrigin, fireDirection);
    }

    private void fireAlongLine(Position2D origin, OrthogonalDirection fireDirection) {
        Position2D current = origin.getAdjacent(fireDirection);

        while (true) {
            Cell cell;
            try {
                cell = GameMap.getInstance().getCell(current);
            } catch (PositionOutOfBoundsException e) {
                return;
            }

            if (isObstacleOrUnit(cell)) {
                impactCell(current, cell, fireDirection);
                return;
            }

            current = current.getAdjacent(fireDirection);
        }
    }

    private void impactCell(Position2D position, Cell cell, OrthogonalDirection fireDirection) {
        try {
            GameMap.getInstance().damage(position);
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e);
        }

        if (cell.hasUnit()) {
            try {
                GameMap.getInstance().pushUnit(position, fireDirection);
            } catch (PositionOutOfBoundsException ignored) {
                // Si el empuje cae fuera del tablero, la unidad permanece en el borde
            }
        }
    }

    private OrthogonalDirection getDirectionToShooter(Position2D target) {
        List<OrthogonalDirection> directions = List.of(
                new North(),
                new East(),
                new South(),
                new West()
        );

        for (OrthogonalDirection direction : directions) {
            if (findFurthestUnitPosition(target, direction) != null) {
                return direction;
            }
        }

        return new NoDirection();
    }

    private Position2D findFurthestUnitPosition(Position2D start, OrthogonalDirection direction) {
        Position2D current = start.getAdjacent(direction);
        Position2D furthestUnitPosition = null;

        while (true) {
            try {
                Cell cell = GameMap.getInstance().getCell(current);
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

    private boolean isObstacleOrUnit(Cell cell) {
        return cell != null && (cell.hasUnit() || cell instanceof Building || cell instanceof Mountain);
    }
}
