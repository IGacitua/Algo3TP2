package game.units.weapons;

import game.map.GameMap;
import game.map.cell.Cell;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.*;

public class Punch extends Weapon {

    private static final int DAMAGE = 2;

    @Override
    public boolean attack(Position origin, Position target, GameMap map) {
        if (origin.getDiagonalDistance(target) != 1.0) {
            throw new IllegalArgumentException("Punch can only target an orthogonally adjacent tile.");
        }

        OrthogonalDirection pushDirection = origin.getDirectionTo(target);
        if (!pushDirection.isValidDirection()) {
            throw new IllegalArgumentException("Punch can only target an orthogonally adjacent tile.");
        }

        return applyPunch(target, pushDirection, map);
    }

    @Override
    public boolean attack(Position target, GameMap map) {
        OrthogonalDirection pushDirection = getPushDirection(target, map);

        if (!pushDirection.isValidDirection()) {
            throw new IllegalArgumentException("Punch can only target an orthogonally adjacent tile.");
        }

        return applyPunch(target, pushDirection, map);
    }

    private boolean applyPunch(Position target, OrthogonalDirection pushDirection, GameMap map) {
        try {
            map.damageCellAt(target, DAMAGE);
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e);
        }

        try {
            map.pushUnitTowards(target, pushDirection);
        } catch (PositionOutOfBoundsException ignored) {
            // Si la casilla destino del empuje cae fuera del mapa, la unidad permanece en el borde
        }

        return true;
    }

    private OrthogonalDirection getPushDirection(Position target, GameMap map) {
        Position north = target.getAdjacent(new North());
        Position east = target.getAdjacent(new East());
        Position south = target.getAdjacent(new South());
        Position west = target.getAdjacent(new West());

        if (hasUnitAt(north, map)) {
            return new South();
        }
        if (hasUnitAt(east, map)) {
            return new West();
        }
        if (hasUnitAt(south, map)) {
            return new North();
        }
        if (hasUnitAt(west, map)) {
            return new East();
        }

        return new NoDirection();
    }

    private boolean hasUnitAt(Position position, GameMap map) {
        try {
            Cell cell = map.getCellAt(position);
            return cell != null && cell.hasUnit();
        } catch (PositionOutOfBoundsException e) {
            return false;
        }
    }
}
