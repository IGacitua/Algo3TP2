package game.units.weapons;

import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.map.cell.Cell;
import game.map.direction.East;
import game.map.direction.NoDirection;
import game.map.direction.North;
import game.map.direction.OrthogonalDirection;
import game.map.direction.South;
import game.map.direction.West;

public class Punch extends Weapon {

    private static final int DAMAGE = 2;

    @Override
    public void attack(Position2D origin, Position2D target, GameMap map) {
        if (origin.getDistance(target) != 1.0) {
            throw new IllegalArgumentException("Punch can only target an orthogonally adjacent tile.");
        }

        OrthogonalDirection pushDirection = origin.getDirectionTo(target);
        if (pushDirection instanceof NoDirection) {
            throw new IllegalArgumentException("Punch can only target an orthogonally adjacent tile.");
        }

        applyPunch(target, pushDirection, map);
    }

    @Override
    public void attack(Position2D target, GameMap map) {
        OrthogonalDirection pushDirection = getPushDirection(target, map);

        if (pushDirection instanceof NoDirection) {
            throw new IllegalArgumentException("Punch can only target an orthogonally adjacent tile.");
        }

        applyPunch(target, pushDirection, map);
    }

    private void applyPunch(Position2D target, OrthogonalDirection pushDirection, GameMap map) {
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
    }

    private OrthogonalDirection getPushDirection(Position2D target, GameMap map) {
        Position2D north = target.getAdjacent(new North());
        Position2D east = target.getAdjacent(new East());
        Position2D south = target.getAdjacent(new South());
        Position2D west = target.getAdjacent(new West());

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

    private boolean hasUnitAt(Position2D position, GameMap map) {
        try {
            Cell cell = map.getCellAt(position);
            return cell != null && cell.hasUnit();
        } catch (PositionOutOfBoundsException e) {
            return false;
        }
    }
}
