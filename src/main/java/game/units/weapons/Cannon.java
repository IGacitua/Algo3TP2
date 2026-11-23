package game.units.weapons;

import game.map.GameMap;
import game.map.cell.Cell;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.OrthogonalDirection;

public class Cannon extends Weapon {

    @Override
    public boolean attack(Position origin, Position target, GameMap map) {
        OrthogonalDirection fireDirection = origin.getDirectionTo(target);

        if (!fireDirection.isValidDirection()) {
            throw new IllegalArgumentException("Cannon can only fire in a straight orthogonal line.");
        }

        return fireAlongLine(origin, fireDirection, map);
    }

    @Override
    public boolean attack(Position target, GameMap map) {
        throw new UnsupportedOperationException("Cannon requires an origin position to determine the firing direction.");
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

            if (cell != null && cell.blocksShot()) {
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
}
